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
import kieker.tools.traceAnalysis.filter.visualization.exception.GraphFormattingException;
import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractGraph;

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
	public void writeGraph(final AbstractGraph<?, ?, ?> graph) {
		final String filename = this.outputPathName + this.getOutputFilename(graph);

		final GraphmlType graphml = this.transformGraphToGraphml(graph);
		try {
			new GraphmlWriter().write(graphml, filename);
		} catch (final JAXBException e) {
			throw new GraphFormattingException(String.format(WRITE_ERROR_MESSAGE_TEMPLATE, filename), e);
		}
	}

	private String getOutputFilename(final AbstractGraph<?, ?, ?> graph) {
		final String DEFAULT_FILENAME = graph.getClass().getName() + GRAPHML_EXTENSION;
		final String outputFilename = (this.outputFileName.length() > 0 ? this.outputFileName : DEFAULT_FILENAME);
		return outputFilename;
	}

	private GraphmlType transformGraphToGraphml(final AbstractGraph<?, ?, ?> graph) {
		// TODO Auto-generated method stub
		return null;
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
