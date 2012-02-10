package kieker.test.tools.manual;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.TeeFilter;
import kieker.analysis.reader.filesystem.FSReader;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.traceAnalysis.plugins.AbstractMessageTraceProcessingPlugin;
import kieker.tools.traceAnalysis.plugins.AbstractTraceAnalysisPlugin;
import kieker.tools.traceAnalysis.plugins.flow.EventRecordTraceGenerationFilter;
import kieker.tools.traceAnalysis.plugins.flow.EventTrace2ExecutionTraceFilter;
import kieker.tools.traceAnalysis.plugins.flow.TimestampFilter;
import kieker.tools.traceAnalysis.plugins.flow.TraceIdFilter;
import kieker.tools.traceAnalysis.plugins.visualization.dependencyGraph.ComponentDependencyGraphPluginAllocation;
import kieker.tools.traceAnalysis.plugins.visualization.dependencyGraph.OperationDependencyGraphPluginAllocation;
import kieker.tools.traceAnalysis.plugins.visualization.sequenceDiagram.SequenceDiagramPlugin;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * 
 * @author Jan Waller
 * 
 */
public class TestAnalysis {
	private static final Log LOG = LogFactory.getLog(TestAnalysis.class);
	private static final boolean LOADCONFIG = false;

	public static void main(final String[] args) {
		try {
			final AnalysisController analysisController;
			final SystemModelRepository traceRepo = new SystemModelRepository(new Configuration());
			if (TestAnalysis.LOADCONFIG) {
				analysisController = new AnalysisController(new File("analysisproject/flowproject.xml"));
			} else {
				analysisController = new AnalysisController();

				final Map<String, AbstractRepository> repoMap = new HashMap<String, AbstractRepository>();
				repoMap.put(AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME, traceRepo);

				final Configuration configurationReader = new Configuration();
				configurationReader.setProperty(FSReader.CONFIG_INPUTDIRS, "analysisproject/testdata-ascii/");
				final FSReader reader = new FSReader(configurationReader, null);

				final Configuration configurationTeeFilter = new Configuration();
				configurationTeeFilter.setProperty(TeeFilter.CONFIG_STREAM, TeeFilter.CONFIG_STREAM_STDOUT);
				final TeeFilter teeFilter = new TeeFilter(configurationTeeFilter, null);

				final Configuration configurationTimestampFilter = new Configuration();
				final TimestampFilter timestampFilter = new TimestampFilter(configurationTimestampFilter, null);

				final Configuration configurationTraceIdFilter = new Configuration();
				final TraceIdFilter traceIdFilter = new TraceIdFilter(configurationTraceIdFilter, null);

				final Configuration configurationEventRecordTraceGenerationFilter = new Configuration();
				final EventRecordTraceGenerationFilter eventRecordTraceGenerationFilter = new EventRecordTraceGenerationFilter(
						configurationEventRecordTraceGenerationFilter, repoMap);

				final Configuration configurationEventTrace2ExecutionTraceFilter = new Configuration();
				final EventTrace2ExecutionTraceFilter eventTrace2ExecutionTraceFilter = new EventTrace2ExecutionTraceFilter(
						configurationEventTrace2ExecutionTraceFilter, repoMap);

				final Configuration configurationSequenceDiagramPlugin = new Configuration();
				final SequenceDiagramPlugin sequenceDiagramPlugin = new SequenceDiagramPlugin(configurationSequenceDiagramPlugin, repoMap,
						SequenceDiagramPlugin.SDModes.ASSEMBLY, "analysisproject/a", true);

				final Configuration configurationComponentDependencyGraphPluginAllocation = new Configuration();
				final ComponentDependencyGraphPluginAllocation componentDependencyGraphPluginAllocation = new ComponentDependencyGraphPluginAllocation(
						configurationComponentDependencyGraphPluginAllocation, repoMap, new File("analysisproject/dep"), true, true, false);

				final Configuration configurationOperationDependencyGraphPluginAllocation = new Configuration();
				configurationOperationDependencyGraphPluginAllocation.setProperty(OperationDependencyGraphPluginAllocation.CONFIG_DOT_OUTPUT_FILE,
						"analysisproject/dep-op");
				final OperationDependencyGraphPluginAllocation operationDependencyGraphPluginAllocation = new OperationDependencyGraphPluginAllocation(
						configurationOperationDependencyGraphPluginAllocation, repoMap);

				analysisController.registerRepository(traceRepo);

				analysisController.setReader(reader);

				analysisController.registerPlugin(timestampFilter);
				AbstractPlugin.connect(reader, FSReader.OUTPUT_PORT_NAME, timestampFilter, TimestampFilter.INPUT_PORT_NAME);

				analysisController.registerPlugin(traceIdFilter);
				AbstractPlugin.connect(timestampFilter, TimestampFilter.OUTPUT_PORT_NAME, traceIdFilter, TraceIdFilter.INPUT_PORT_NAME);

				analysisController.registerPlugin(eventRecordTraceGenerationFilter);
				AbstractPlugin.connect(traceIdFilter, TraceIdFilter.OUTPUT_PORT_NAME, eventRecordTraceGenerationFilter,
						EventRecordTraceGenerationFilter.INPUT_PORT_NAME);

				analysisController.registerPlugin(eventTrace2ExecutionTraceFilter);
				AbstractPlugin.connect(eventRecordTraceGenerationFilter, EventRecordTraceGenerationFilter.OUTPUT_PORT_NAME, eventTrace2ExecutionTraceFilter,
						EventTrace2ExecutionTraceFilter.INPUT_PORT_NAME);

				analysisController.registerPlugin(teeFilter);
				AbstractPlugin.connect(eventTrace2ExecutionTraceFilter, EventTrace2ExecutionTraceFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE, teeFilter,
						TeeFilter.INPUT_PORT_NAME);

				analysisController.registerPlugin(sequenceDiagramPlugin);
				AbstractPlugin.connect(teeFilter, TeeFilter.OUTPUT_PORT_NAME, sequenceDiagramPlugin, AbstractMessageTraceProcessingPlugin.INPUT_PORT_NAME);

				analysisController.registerPlugin(componentDependencyGraphPluginAllocation);
				AbstractPlugin.connect(teeFilter, TeeFilter.OUTPUT_PORT_NAME, componentDependencyGraphPluginAllocation,
						AbstractMessageTraceProcessingPlugin.INPUT_PORT_NAME);

				analysisController.registerPlugin(operationDependencyGraphPluginAllocation);
				AbstractPlugin.connect(teeFilter, TeeFilter.OUTPUT_PORT_NAME, operationDependencyGraphPluginAllocation,
						AbstractMessageTraceProcessingPlugin.INPUT_PORT_NAME);

				analysisController.saveToFile(new File("analysisproject/flowproject.xml"), "FlowProject");
			}
			analysisController.run();
			traceRepo.saveSystemToHTMLFile("analysisproject/system.html");
		} catch (final Exception ex) {
			TestAnalysis.LOG.error("Error initializing AnalysisController", ex);
		}
	}
}
