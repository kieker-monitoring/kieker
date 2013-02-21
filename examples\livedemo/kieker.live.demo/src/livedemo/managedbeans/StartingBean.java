package livedemo.managedbeans;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import kieker.analysis.AnalysisController;
import kieker.analysis.AnalysisControllerThread;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.CountingThroughputFilter;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.jmx.JMXReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.tools.traceAnalysis.filter.AbstractMessageTraceProcessingFilter;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.IGraphOutputtingFilter;
import kieker.tools.traceAnalysis.filter.executionRecordTransformation.ExecutionRecordTransformationFilter;
import kieker.tools.traceAnalysis.filter.traceReconstruction.TraceReconstructionFilter;
import kieker.tools.traceAnalysis.filter.visualization.GraphWriterConfiguration;
import kieker.tools.traceAnalysis.filter.visualization.GraphWriterPlugin;
import kieker.tools.traceAnalysis.filter.visualization.dependencyGraph.OperationDependencyGraphAllocationFilter;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

@ManagedBean(name="startingBean", eager=true)
@ApplicationScoped
public class StartingBean {
	
	final AnalysisController analysisInstance;
	AnalysisControllerThread act;
	final ListCollectionFilter<OperationExecutionRecord> listCollectionFilter;
	final CountingThroughputFilter ctFilter;
	SystemModelRepository systemModelRepository;
	
	public StartingBean(){
		this.analysisInstance = new AnalysisController();
		this.listCollectionFilter = new ListCollectionFilter<OperationExecutionRecord>(new Configuration());
		this.ctFilter = new CountingThroughputFilter(new Configuration());
		this.systemModelRepository = new SystemModelRepository(new Configuration());
		try {
			init();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (AnalysisConfigurationException e) {
			e.printStackTrace();
		}
		act = new AnalysisControllerThread(analysisInstance);
		act.start();
	}
	
	public SystemModelRepository getSystemModelRepository(){
		return this.systemModelRepository;
	}
	
	public CountingThroughputFilter getCountingFilter(){
		return this.ctFilter;
	}
	
	public ListCollectionFilter<OperationExecutionRecord> getCollectionFilter(){
		return this.listCollectionFilter;
	}
	
	private void init() throws IllegalStateException, AnalysisConfigurationException{
	
		final Configuration jmxReaderConfig = new Configuration();
		jmxReaderConfig.setProperty(JMXReader.CONFIG_PROPERTY_NAME_SILENT, "true");
		final JMXReader reader = new JMXReader(jmxReaderConfig);
		analysisInstance.registerReader(reader);

		
//		final Configuration teeFilterConfig = new Configuration();
//		teeFilterConfig.setProperty(TeeFilter.CONFIG_PROPERTY_NAME_STREAM,
//		TeeFilter.CONFIG_PROPERTY_VALUE_STREAM_STDOUT);
//		final TeeFilter teeFilter = new TeeFilter(teeFilterConfig);
//
//		analysisInstance.registerFilter(teeFilter);
		analysisInstance.registerFilter(listCollectionFilter);
		analysisInstance.registerFilter(ctFilter);

		analysisInstance.connect(reader, JMXReader.OUTPUT_PORT_NAME_RECORDS,
				listCollectionFilter, ListCollectionFilter.INPUT_PORT_NAME);
//		analysisInstance.connect(listCollectionFilter, ListCollectionFilter.OUTPUT_PORT_NAME,
//				teeFilter, TeeFilter.INPUT_PORT_NAME_EVENTS);
		analysisInstance.connect(listCollectionFilter, ListCollectionFilter.OUTPUT_PORT_NAME, 
				ctFilter, CountingThroughputFilter.INPUT_PORT_NAME_RECORDS);
		
	
		analysisInstance.registerRepository(systemModelRepository);
		
		ExecutionRecordTransformationFilter ertf = new ExecutionRecordTransformationFilter(new Configuration());
		analysisInstance.registerFilter(ertf);
		analysisInstance.connect(ertf, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemModelRepository);
		analysisInstance.connect(reader, JMXReader.OUTPUT_PORT_NAME_RECORDS, ertf, ExecutionRecordTransformationFilter.INPUT_PORT_NAME_RECORDS);
		
		Configuration traceConfig = new Configuration();
		traceConfig.setProperty(TraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION_MILLIS, "1000");
		TraceReconstructionFilter trf = new TraceReconstructionFilter(traceConfig);
		analysisInstance.registerFilter(trf);
		analysisInstance.connect(trf, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemModelRepository);
		analysisInstance.connect(ertf, ExecutionRecordTransformationFilter.OUTPUT_PORT_NAME_EXECUTIONS, trf, TraceReconstructionFilter.INPUT_PORT_NAME_EXECUTIONS);
		
		OperationDependencyGraphAllocationFilter dependencyGraphFilter = 
				new OperationDependencyGraphAllocationFilter(new Configuration());
		analysisInstance.registerFilter(dependencyGraphFilter);
		analysisInstance.connect(trf, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE, dependencyGraphFilter, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
		analysisInstance.connect(dependencyGraphFilter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemModelRepository);

		Configuration writerConfig = new Configuration();
		writerConfig.setProperty(GraphWriterConfiguration.CONFIG_PROPERTY_NAME_OUTPUT_PATH_NAME, "C:\\IDE\\Eclipse Indigo EE\\workspace\\kieker.examples\\kieker.live.demo\\webapps\\kiekerLiveDemo\\resources\\");
		writerConfig.setProperty(GraphWriterConfiguration.CONFIG_PROPERTY_NAME_OUTPUT_FILE_NAME, "graphzahl");
		GraphWriterPlugin graphWriter = new GraphWriterPlugin(writerConfig);
		analysisInstance.registerFilter(graphWriter);
		analysisInstance.connect(dependencyGraphFilter, IGraphOutputtingFilter.OUTPUT_PORT_NAME_GRAPH, 
				graphWriter, GraphWriterPlugin.INPUT_PORT_NAME_GRAPHS);
	}

}
