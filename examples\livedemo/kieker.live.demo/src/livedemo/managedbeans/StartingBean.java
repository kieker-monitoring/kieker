package livedemo.managedbeans;

//import java.util.concurrent.TimeUnit;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import kieker.analysis.AnalysisController;
import kieker.analysis.AnalysisControllerThread;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.select.TypeFilter;
import kieker.analysis.plugin.reader.jmx.JMXReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.system.CPUUtilizationRecord;
import kieker.common.record.system.MemSwapUsageRecord;
//import kieker.tools.traceAnalysis.filter.AbstractMessageTraceProcessingFilter;
//import kieker.tools.traceAnalysis.filter.IGraphOutputtingFilter;
//import kieker.tools.traceAnalysis.filter.traceReconstruction.TraceReconstructionFilter;
//import kieker.tools.traceAnalysis.filter.visualization.GraphWriterPlugin;
//import kieker.tools.traceAnalysis.filter.visualization.dependencyGraph.OperationDependencyGraphAllocationFilter;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.executionRecordTransformation.ExecutionRecordTransformationFilter;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;
import livedemo.filter.ListFilter;

/**
 * @author Bjoern Weissenfels
 */
@ManagedBean(name="startingBean", eager=true)
@ApplicationScoped
public class StartingBean {
	
	final AnalysisController analysisInstance;
	AnalysisControllerThread act;
	final ListFilter<OperationExecutionRecord> oerCollectionFilter;
	final ListFilter<CPUUtilizationRecord> cpuCollectionFilter;
	final ListFilter<MemSwapUsageRecord> memSwapCollectionFilter;
	SystemModelRepository systemModelRepository;
	
	public StartingBean(){
		this.analysisInstance = new AnalysisController();
		this.oerCollectionFilter = new ListFilter<OperationExecutionRecord>(new Configuration(), analysisInstance);
		this.cpuCollectionFilter = new ListFilter<CPUUtilizationRecord>(new Configuration(), analysisInstance);
		this.memSwapCollectionFilter = new ListFilter<MemSwapUsageRecord>(new Configuration(), analysisInstance);
		this.systemModelRepository = new SystemModelRepository(new Configuration(), analysisInstance);
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
	
	public ListFilter<OperationExecutionRecord> getOERCollectionFilter(){
		return this.oerCollectionFilter;
	}
	
	public ListFilter<CPUUtilizationRecord> getCPUCollectionFilter(){
		return this.cpuCollectionFilter;
	}
	
	public ListFilter<MemSwapUsageRecord> getMemSwapCollectionFilter(){
		return this.memSwapCollectionFilter;
	}
	
	private void init() throws IllegalStateException, AnalysisConfigurationException{
	
		final Configuration jmxReaderConfig = new Configuration();
		jmxReaderConfig.setProperty(JMXReader.CONFIG_PROPERTY_NAME_SILENT, "true");
		final JMXReader reader = new JMXReader(jmxReaderConfig, analysisInstance);

		final Configuration typeFilter1Config = new Configuration();
		typeFilter1Config.setProperty(TypeFilter.CONFIG_PROPERTY_NAME_TYPES, "kieker.common.record.controlflow.OperationExecutionRecord");
		final TypeFilter typeFilter1 = new TypeFilter(typeFilter1Config, analysisInstance);
		
		final Configuration typeFilter2Config = new Configuration();
		typeFilter2Config.setProperty(TypeFilter.CONFIG_PROPERTY_NAME_TYPES, "kieker.common.record.system.CPUUtilizationRecord");
		final TypeFilter typeFilter2 = new TypeFilter(typeFilter2Config, analysisInstance);
		
		final Configuration typeFilter3Config = new Configuration();
		typeFilter3Config.setProperty(TypeFilter.CONFIG_PROPERTY_NAME_TYPES, "kieker.common.record.system.MemSwapUsageRecord");
		final TypeFilter typeFilter3 = new TypeFilter(typeFilter3Config, analysisInstance);
	
		analysisInstance.connect(reader, JMXReader.OUTPUT_PORT_NAME_RECORDS,
				typeFilter1, TypeFilter.INPUT_PORT_NAME_EVENTS);

		analysisInstance.connect(typeFilter1, TypeFilter.OUTPUT_PORT_NAME_TYPE_MATCH,
				oerCollectionFilter, ListFilter.INPUT_PORT_NAME);
		analysisInstance.connect(typeFilter1, TypeFilter.OUTPUT_PORT_NAME_TYPE_MISMATCH,
				typeFilter2, TypeFilter.INPUT_PORT_NAME_EVENTS);
		analysisInstance.connect(typeFilter2, TypeFilter.OUTPUT_PORT_NAME_TYPE_MATCH,
				cpuCollectionFilter, ListFilter.INPUT_PORT_NAME);
		analysisInstance.connect(typeFilter2, TypeFilter.OUTPUT_PORT_NAME_TYPE_MISMATCH,
				typeFilter3, TypeFilter.INPUT_PORT_NAME_EVENTS);
		analysisInstance.connect(typeFilter3, TypeFilter.OUTPUT_PORT_NAME_TYPE_MATCH,
				memSwapCollectionFilter, ListFilter.INPUT_PORT_NAME);
		
		
		ExecutionRecordTransformationFilter ertf = new ExecutionRecordTransformationFilter(new Configuration(), analysisInstance);
		analysisInstance.connect(ertf, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemModelRepository);
		analysisInstance.connect(reader, JMXReader.OUTPUT_PORT_NAME_RECORDS, ertf, ExecutionRecordTransformationFilter.INPUT_PORT_NAME_RECORDS);
		
//		Configuration traceConfig = new Configuration();
//		traceConfig.setProperty(TraceReconstructionFilter.CONFIG_PROPERTY_VALUE_TIMEUNIT, TimeUnit.MILLISECONDS.name());
//		traceConfig.setProperty(TraceReconstructionFilter.CONFIG_PROPERTY_VALUE_MAX_TRACE_DURATION, "1000");
//		TraceReconstructionFilter trf = new TraceReconstructionFilter(traceConfig, analysisInstance);
//		analysisInstance.connect(trf, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemModelRepository);
//		analysisInstance.connect(ertf, ExecutionRecordTransformationFilter.OUTPUT_PORT_NAME_EXECUTIONS, trf, TraceReconstructionFilter.INPUT_PORT_NAME_EXECUTIONS);
//		
//		OperationDependencyGraphAllocationFilter dependencyGraphFilter = 
//				new OperationDependencyGraphAllocationFilter(new Configuration(), analysisInstance);
//		analysisInstance.connect(trf, TraceReconstructionFilter.OUTPUT_PORT_NAME_MESSAGE_TRACE, dependencyGraphFilter, AbstractMessageTraceProcessingFilter.INPUT_PORT_NAME_MESSAGE_TRACES);
//		analysisInstance.connect(dependencyGraphFilter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemModelRepository);
//
//		Configuration writerConfig = new Configuration();
//		GraphWriterPlugin graphWriter = new GraphWriterPlugin(writerConfig, analysisInstance);
//		analysisInstance.connect(dependencyGraphFilter, IGraphOutputtingFilter.OUTPUT_PORT_NAME_GRAPH, 
//				graphWriter, GraphWriterPlugin.INPUT_PORT_NAME_GRAPHS);
	}

}
