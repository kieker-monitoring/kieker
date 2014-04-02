/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.traceAnalysis.filter.visualization.util.graphml;

import javax.xml.bind.JAXBException;

import org.graphdrawing.graphml.xmlns.GraphmlType;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.filter.visualization.GraphWriterPlugin;
import kieker.tools.traceAnalysis.filter.visualization.dependencyGraph.OperationAllocationDependencyGraph;
import kieker.tools.traceAnalysis.filter.visualization.exception.GraphFormattingException;
import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractGraph;

/**
 * @author Christian Wulf
 * 
 * @since 1.9
 */
@Plugin(name = "GraphML writer plugin",
		description = "Plugin for writing graphs to files in the GraphML format",
		configuration = {
			@Property(name = GraphWriterPlugin.CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS, defaultValue = "true"),
			@Property(name = GraphWriterPlugin.CONFIG_PROPERTY_NAME_SHORTLABELS, defaultValue = "true"),
			@Property(name = GraphWriterPlugin.CONFIG_PROPERTY_NAME_SELFLOOPS, defaultValue = "false")
		})
public class GraphmlWriterFilter extends AbstractFilterPlugin {
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

	private static final String WRITE_ERROR_MESSAGE_TEMPLATE = "Graph could not be written to file %s.";
	private static final String GRAPHML_EXTENSION = ".graphml";

	private final String outputPathName;
	private final String outputFileName;
	private final boolean includeWeights;
	private final boolean useShortLabels;
	private final boolean plotLoops;

	public GraphmlWriterFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.outputPathName = configuration.getPathProperty(CONFIG_PROPERTY_NAME_OUTPUT_PATH_NAME);
		this.outputFileName = configuration.getPathProperty(CONFIG_PROPERTY_NAME_OUTPUT_FILE_NAME);
		this.includeWeights = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS);
		this.useShortLabels = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_SHORTLABELS);
		this.plotLoops = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_SELFLOOPS);
	}

	@InputPort(name = INPUT_PORT_NAME_GRAPHS, eventTypes = { AbstractGraph.class })
	public void writeGraph(final OperationAllocationDependencyGraph graph) {
		final String filename = this.outputPathName + this.getOutputFilename(graph);

		final GraphmlType graphml = this.transformGraphToGraphml(graph);
		try {
			new GraphmlWriter().write(graphml, filename);
		} catch (final JAXBException e) {
			throw new GraphFormattingException(String.format(WRITE_ERROR_MESSAGE_TEMPLATE, filename), e);
		}
	}

	private String getOutputFilename(final AbstractGraph<?, ?, ?> graph) {
		final String defaultFilename = graph.getClass().getName() + GRAPHML_EXTENSION;
		final String fileName;

		if (!this.outputFileName.isEmpty()) {
			fileName = this.outputFileName;
		} else {
			fileName = defaultFilename;
		}

		return fileName;
	}

	private GraphmlType transformGraphToGraphml(final OperationAllocationDependencyGraph graph) {
		final Graph2GraphmlVisitor graph2graphmlVisitor = new Graph2GraphmlVisitor(this.includeWeights, this.useShortLabels, this.plotLoops);
		graph2graphmlVisitor.buildAssemblyComponentNodes(graph);
		graph.traverseWithVerticesFirst(graph2graphmlVisitor);
		return graph2graphmlVisitor.getGraphml();
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

}
