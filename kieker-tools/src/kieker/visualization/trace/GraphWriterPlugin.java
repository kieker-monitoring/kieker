/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package kieker.visualization.trace;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import kieker.tools.trace.analysis.filter.visualization.AbstractGraphFormatter;
import kieker.tools.trace.analysis.filter.visualization.exception.GraphFormattingException;
import kieker.tools.trace.analysis.filter.visualization.graph.AbstractGraph;
import kieker.visualization.trace.dependency.graph.ComponentAllocationDependencyGraph;
import kieker.visualization.trace.dependency.graph.ComponentAllocationDependencyGraphFormatter;
import kieker.visualization.trace.dependency.graph.ComponentAssemblyDependencyGraph;
import kieker.visualization.trace.dependency.graph.ComponentAssemblyDependencyGraphFormatter;
import kieker.visualization.trace.dependency.graph.ContainerDependencyGraph;
import kieker.visualization.trace.dependency.graph.ContainerDependencyGraphFormatter;
import kieker.visualization.trace.dependency.graph.OperationAllocationDependencyGraph;
import kieker.visualization.trace.dependency.graph.OperationAllocationDependencyGraphFormatter;
import kieker.visualization.trace.dependency.graph.OperationAssemblyDependencyGraph;
import kieker.visualization.trace.dependency.graph.OperationAssemblyDependencyGraphFormatter;

import teetime.framework.AbstractConsumerStage;

/**
 * Generic graph writer plugin to generate graph specifications and save them to disk. This plugin uses
 * a formatter registry (see {@link #FORMATTER_REGISTRY}) to determine the appropriate formatter for a
 * given graph.
 *
 * @author Holger Knoche
 *
 * @since 1.6
 */
public class GraphWriterPlugin extends AbstractConsumerStage<AbstractGraph<?, ?, ?>> {

	private static final String NO_SUITABLE_FORMATTER_MESSAGE_TEMPLATE = "No formatter type defined for graph type %s.";
	private static final String INSTANTIATION_ERROR_MESSAGE_TEMPLATE = "Could not instantiate formatter type %s for graph type %s.";
	private static final String WRITE_ERROR_MESSAGE_TEMPLATE = "Graph could not be written to file %s.";

	private static final ConcurrentMap<Class<? extends AbstractGraph<?, ?, ?>>, Class<? extends AbstractGraphFormatter<?>>> FORMATTER_REGISTRY = new ConcurrentHashMap<>();

	private final String outputPathName;
	private final String outputFileName;
	private final boolean includeWeights;
	private final boolean useShortLabels;
	private final boolean plotLoops;

	static {
		FORMATTER_REGISTRY.put(ComponentAllocationDependencyGraph.class, ComponentAllocationDependencyGraphFormatter.class);
		FORMATTER_REGISTRY.put(ComponentAssemblyDependencyGraph.class, ComponentAssemblyDependencyGraphFormatter.class);
		FORMATTER_REGISTRY.put(OperationAllocationDependencyGraph.class, OperationAllocationDependencyGraphFormatter.class);
		FORMATTER_REGISTRY.put(OperationAssemblyDependencyGraph.class, OperationAssemblyDependencyGraphFormatter.class);
		FORMATTER_REGISTRY.put(ContainerDependencyGraph.class, ContainerDependencyGraphFormatter.class);
	}

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param outputPathName
	 *            base path name for the output directory
	 * @param outputFileName
	 *            filename to be used within the directory
	 * @param includeWeights
	 *            include weights in plotting
	 * @param useShortLabels
	 *            use short labels
	 * @param plotLoops
	 *            plot loops
	 */
	public GraphWriterPlugin(final String outputPathName, final String outputFileName, final boolean includeWeights, final boolean useShortLabels,
			final boolean plotLoops) {
		super();

		this.outputPathName = outputPathName;
		this.outputFileName = outputFileName;
		this.includeWeights = includeWeights;
		this.useShortLabels = useShortLabels;
		this.plotLoops = plotLoops;
	}

	private static void handleInstantiationException(final Class<?> graphClass, final Class<?> formatterClass, final Exception exception) {
		throw new GraphFormattingException(String.format(INSTANTIATION_ERROR_MESSAGE_TEMPLATE, formatterClass.getName(), graphClass.getName()), exception);
	}

	private static AbstractGraphFormatter<?> createFormatter(final AbstractGraph<?, ?, ?> graph) {
		final Class<? extends AbstractGraphFormatter<?>> formatterClass = FORMATTER_REGISTRY.get(graph.getClass());

		if (formatterClass == null) {
			throw new GraphFormattingException(String.format(NO_SUITABLE_FORMATTER_MESSAGE_TEMPLATE, graph.getClass().getName()));
		}

		try {
			final Constructor<? extends AbstractGraphFormatter<?>> constructor = formatterClass.getConstructor();
			return constructor.newInstance();
		} catch (final SecurityException | NoSuchMethodException | IllegalArgumentException | InstantiationException | IllegalAccessException
				| InvocationTargetException e) {
			GraphWriterPlugin.handleInstantiationException(graph.getClass(), formatterClass, e);
		}

		// This should never happen, because all catch clauses indirectly throw exceptions
		return null;
	}

	private String getOutputFileName(final AbstractGraphFormatter<?> formatter) {
		if (this.outputFileName.length() == 0) { // outputFileName cannot be null
			return formatter.getDefaultFileName();
		} else {
			return this.outputFileName;
		}
	}

	/**
	 * Formats a given graph and saves the generated specification to disk. The file name to save the output to is specified by a the configuration options
	 * {@link #CONFIG_PROPERTY_NAME_OUTPUT_PATH_NAME} and {@link #CONFIG_PROPERTY_NAME_OUTPUT_FILE_NAME}.
	 *
	 * @param graph
	 *            The graph to save
	 */
	@Override
	protected void execute(final AbstractGraph<?, ?, ?> graph) throws Exception {
		final AbstractGraphFormatter<?> graphFormatter = GraphWriterPlugin.createFormatter(graph);
		final String specification = graphFormatter.createFormattedRepresentation(graph, this.includeWeights, this.useShortLabels, this.plotLoops);
		final String fileName = this.outputPathName + this.getOutputFileName(graphFormatter);
		BufferedWriter writer = null;
		try {
			writer = Files.newBufferedWriter(Paths.get(fileName));
			writer.write(specification);
			writer.flush();
		} catch (final IOException e) {
			throw new GraphFormattingException(String.format(WRITE_ERROR_MESSAGE_TEMPLATE, fileName), e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (final IOException e) {
					if (this.logger.isErrorEnabled()) {
						this.logger.error(String.format(WRITE_ERROR_MESSAGE_TEMPLATE, fileName), e);
					}
				}
			}
		}
	}

}
