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

package kieker.tools.trace.analysis.filter.visualization;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.tools.trace.analysis.filter.visualization.dependencyGraph.ComponentAllocationDependencyGraph;
import kieker.tools.trace.analysis.filter.visualization.dependencyGraph.ComponentAllocationDependencyGraphFormatter;
import kieker.tools.trace.analysis.filter.visualization.dependencyGraph.ComponentAssemblyDependencyGraph;
import kieker.tools.trace.analysis.filter.visualization.dependencyGraph.ComponentAssemblyDependencyGraphFormatter;
import kieker.tools.trace.analysis.filter.visualization.dependencyGraph.ContainerDependencyGraph;
import kieker.tools.trace.analysis.filter.visualization.dependencyGraph.ContainerDependencyGraphFormatter;
import kieker.tools.trace.analysis.filter.visualization.dependencyGraph.OperationAllocationDependencyGraph;
import kieker.tools.trace.analysis.filter.visualization.dependencyGraph.OperationAllocationDependencyGraphFormatter;
import kieker.tools.trace.analysis.filter.visualization.dependencyGraph.OperationAssemblyDependencyGraph;
import kieker.tools.trace.analysis.filter.visualization.dependencyGraph.OperationAssemblyDependencyGraphFormatter;
import kieker.tools.trace.analysis.filter.visualization.exception.GraphFormattingException;
import kieker.tools.trace.analysis.filter.visualization.graph.AbstractGraph;

/**
 * Generic graph writer plugin to generate graph specifications and save them to disk. This plugin uses
 * a formatter registry (see {@link #FORMATTER_REGISTRY}) to determine the appropriate formatter for a
 * given graph.
 *
 * @author Holger Knoche
 *
 * @since 1.6
 */
@Plugin(name = "Graph writer plugin", description = "Generic plugin for writing graphs to files", configuration = {
	@Property(name = GraphWriterPlugin.CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS, defaultValue = "true"),
	@Property(name = GraphWriterPlugin.CONFIG_PROPERTY_NAME_SHORTLABELS, defaultValue = "true"),
	@Property(name = GraphWriterPlugin.CONFIG_PROPERTY_NAME_SELFLOOPS, defaultValue = "false")
})
public class GraphWriterPlugin extends AbstractFilterPlugin {

	/**
	 * Name of the configuration property containing the output file name.
	 */
	public static final String CONFIG_PROPERTY_NAME_OUTPUT_FILE_NAME = "dotOutputFn";
	/**
	 * Name of the configuration property containing the output path name.
	 */
	public static final String CONFIG_PROPERTY_NAME_OUTPUT_PATH_NAME = "outputPath";
	/**
	 * Name of the configuration property indicating that weights should be included.
	 */
	public static final String CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS = "includeWeights";
	/**
	 * Name of the configuration property indicating that short labels should be used.
	 */
	public static final String CONFIG_PROPERTY_NAME_SHORTLABELS = "shortLabels";
	/**
	 * Name of the configuration property indicating that self-loops should be displayed.
	 */
	public static final String CONFIG_PROPERTY_NAME_SELFLOOPS = "selfLoops";
	/**
	 * Name of the plugin's graph input port.
	 */
	public static final String INPUT_PORT_NAME_GRAPHS = "inputGraph";

	private static final String ENCODING = "UTF-8";

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
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public GraphWriterPlugin(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.outputPathName = configuration.getPathProperty(CONFIG_PROPERTY_NAME_OUTPUT_PATH_NAME);
		this.outputFileName = configuration.getPathProperty(CONFIG_PROPERTY_NAME_OUTPUT_FILE_NAME);
		this.includeWeights = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS);
		this.useShortLabels = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_SHORTLABELS);
		this.plotLoops = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_SELFLOOPS);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_OUTPUT_PATH_NAME, this.outputPathName);
		configuration.setProperty(CONFIG_PROPERTY_NAME_OUTPUT_FILE_NAME, this.outputFileName);
		configuration.setProperty(CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS, String.valueOf(this.includeWeights));
		configuration.setProperty(CONFIG_PROPERTY_NAME_SHORTLABELS, String.valueOf(this.useShortLabels));
		configuration.setProperty(CONFIG_PROPERTY_NAME_SELFLOOPS, String.valueOf(this.plotLoops));
		return configuration;
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
		} catch (final SecurityException e) {
			GraphWriterPlugin.handleInstantiationException(graph.getClass(), formatterClass, e);
		} catch (final NoSuchMethodException e) {
			GraphWriterPlugin.handleInstantiationException(graph.getClass(), formatterClass, e);
		} catch (final IllegalArgumentException e) {
			GraphWriterPlugin.handleInstantiationException(graph.getClass(), formatterClass, e);
		} catch (final InstantiationException e) {
			GraphWriterPlugin.handleInstantiationException(graph.getClass(), formatterClass, e);
		} catch (final IllegalAccessException e) {
			GraphWriterPlugin.handleInstantiationException(graph.getClass(), formatterClass, e);
		} catch (final InvocationTargetException e) {
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
	@InputPort(name = INPUT_PORT_NAME_GRAPHS, eventTypes = { AbstractGraph.class })
	public void writeGraph(final AbstractGraph<?, ?, ?> graph) {
		final AbstractGraphFormatter<?> graphFormatter = GraphWriterPlugin.createFormatter(graph);
		final String specification = graphFormatter.createFormattedRepresentation(graph, this.includeWeights, this.useShortLabels, this.plotLoops);
		final String fileName = this.outputPathName + this.getOutputFileName(graphFormatter);
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), ENCODING));
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
