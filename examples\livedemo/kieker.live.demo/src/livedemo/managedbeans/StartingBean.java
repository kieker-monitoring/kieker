package livedemo.managedbeans;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import kieker.analysis.AnalysisController;
import kieker.analysis.AnalysisControllerThread;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.filter.select.TypeFilter;
import kieker.analysis.plugin.reader.jmx.JMXReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.system.CPUUtilizationRecord;
import kieker.common.record.system.MemSwapUsageRecord;
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
	final ListCollectionFilter<OperationExecutionRecord> oerCollectionFilter;
	final ListCollectionFilter<CPUUtilizationRecord> cpuCollectionFilter;
	final ListCollectionFilter<MemSwapUsageRecord> memSwapCollectionFilter;
	SystemModelRepository systemModelRepository;
	
	public StartingBean(){
		this.analysisInstance = new AnalysisController();
		this.oerCollectionFilter = new ListCollectionFilter<OperationExecutionRecord>(new Configuration());
		this.cpuCollectionFilter = new ListCollectionFilter<CPUUtilizationRecord>(new Configuration());
		this.memSwapCollectionFilter = new ListCollectionFilter<MemSwapUsageRecord>(new Configuration());
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
	
	public ListCollectionFilter<OperationExecutionRecord> getOERCollectionFilter(){
		return this.oerCollectionFilter;
	}
	
	public ListCollectionFilter<CPUUtilizationRecord> getCPUCollectionFilter(){
		return this.cpuCollectionFilter;
	}
	
	public ListCollectionFilter<MemSwapUsageRecord> getMemSwapCollectionFilter(){
		return this.memSwapCollectionFilter;
	}
	
	private void init() throws IllegalStateException, AnalysisConfigurationException{
	
		final Configuration jmxReaderConfig = new Configuration();
		jmxReaderConfig.setProperty(JMXReader.CONFIG_PROPERTY_NAME_SILENT, "true");
		final JMXReader reader = new JMXReader(jmxReaderConfig);
		analysisInstance.registerReader(reader);

		final Configuration typeFilter1Config = new Configuration();
		typeFilter1Config.setProperty(TypeFilter.CONFIG_PROPERTY_NAME_TYPES, "kieker.common.record.controlflow.OperationExecutionRecord");
		final TypeFilter typeFilter1 = new TypeFilter(typeFilter1Config);
		
		final Configuration typeFilter2Config = new Configuration();
		typeFilter2Config.setProperty(TypeFilter.CONFIG_PROPERTY_NAME_TYPES, "kieker.common.record.system.CPUUtilizationRecord");
		final TypeFilter typeFilter2 = new TypeFilter(typeFilter2Config);
		
		final Configuration typeFilter3Config = new Configuration();
		typeFilter3Config.setProperty(TypeFilter.CONFIG_PROPERTY_NAME_TYPES, "kieker.common.record.system.MemSwapUsageRecord");
		final TypeFilter typeFilter3 = new TypeFilter(typeFilter3Config);
		
//		final Configuration teeFilterConfig = new Configuration();
//		teeFilterConfig.setProperty(TeeFilter.CONFIG_PROPERTY_NAME_STREAM,
//		TeeFilter.CONFIG_PROPERTY_VALUE_STREAM_STDOUT);
//		final TeeFilter teeFilter = new TeeFilter(teeFilterConfig);

//		analysisInstance.registerFilter(teeFilter);
		analysisInstance.registerFilter(oerCollectionFilter);
		analysisInstance.registerFilter(cpuCollectionFilter);
		analysisInstance.registerFilter(memSwapCollectionFilter);
		analysisInstance.registerFilter(typeFilter1);
		analysisInstance.registerFilter(typeFilter2);
		analysisInstance.registerFilter(typeFilter3);
		
//		analysisInstance.connect(reader, JMXReader.OUTPUT_PORT_NAME_RECORDS,
//				teeFilter, TeeFilter.INPUT_PORT_NAME_EVENTS);
//		analysisInstance.connect(teeFilter, TeeFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS,
//				typeFilter1, TypeFilter.INPUT_PORT_NAME_EVENTS);
		
		analysisInstance.connect(reader, JMXReader.OUTPUT_PORT_NAME_RECORDS,
				typeFilter1, TypeFilter.INPUT_PORT_NAME_EVENTS);

		analysisInstance.connect(typeFilter1, TypeFilter.OUTPUT_PORT_NAME_TYPE_MATCH,
				oerCollectionFilter, ListCollectionFilter.INPUT_PORT_NAME);
		analysisInstance.connect(typeFilter1, TypeFilter.OUTPUT_PORT_NAME_TYPE_MISMATCH,
				typeFilter2, TypeFilter.INPUT_PORT_NAME_EVENTS);
		analysisInstance.connect(typeFilter2, TypeFilter.OUTPUT_PORT_NAME_TYPE_MATCH,
				cpuCollectionFilter, ListCollectionFilter.INPUT_PORT_NAME);
		analysisInstance.connect(typeFilter2, TypeFilter.OUTPUT_PORT_NAME_TYPE_MISMATCH,
				typeFilter3, TypeFilter.INPUT_PORT_NAME_EVENTS);
		analysisInstance.connect(typeFilter3, TypeFilter.OUTPUT_PORT_NAME_TYPE_MATCH,
				memSwapCollectionFilter, ListCollectionFilter.INPUT_PORT_NAME);
		
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
