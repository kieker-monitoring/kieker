package livedemo.managedbeans;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import kieker.analysis.AnalysisController;
import kieker.analysis.AnalysisControllerThread;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.CPUUtilizationDisplayFilter;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.filter.forward.MemSwapUtilizationDisplayFilter;
import kieker.analysis.plugin.filter.forward.MethodAndComponentFlowDisplayFilter;
import kieker.analysis.plugin.filter.select.TypeFilter;
import kieker.analysis.plugin.reader.jmx.JMXReader;
import kieker.analysis.plugin.reader.timer.TimeReader;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.executionRecordTransformation.ExecutionRecordTransformationFilter;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;
import livedemo.entities.Record;
import livedemo.filter.MethodResponsetimeDisplayFilter;
import livedemo.filter.OER2RecordFilter;

/**
 * @author Bjoern Weissenfels
 */
@ManagedBean(name="analysisBean", eager=true)
@ApplicationScoped
public class AnalysisBean {
	
	private final String numberOfCpuAndMemSwapEntries = "20";
	private final String numberOfRecordListEntries = "50";
	private final String numberOfResponsetimeEntries = "20";
	private final String timeReaderUpdateIntervallNS = "2000000000";
	
	private final AnalysisController analysisInstance;
	private final AnalysisControllerThread act;
	private final UpdateThread updateThread;
	
	private final SystemModelRepository systemModelRepository;
	private final CPUUtilizationDisplayFilter cpuFilter;
	private final MemSwapUtilizationDisplayFilter memSwapFilter;
	private final ListCollectionFilter<Record> recordListFilter;
	private final MethodResponsetimeDisplayFilter responsetimeFilter;
	private final MethodAndComponentFlowDisplayFilter tagCloudFilter;
	
	
	public AnalysisBean(){
		this.updateThread = new UpdateThread(1000); // will notify its observers every second
		this.updateThread.start();
		
		this.analysisInstance = new AnalysisController();
		
		this.systemModelRepository = new SystemModelRepository(new Configuration(), analysisInstance);
		
		Configuration cpuConfiguration = new Configuration();
		cpuConfiguration.setProperty(CPUUtilizationDisplayFilter.CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES, this.numberOfCpuAndMemSwapEntries);
		this.cpuFilter = new CPUUtilizationDisplayFilter(cpuConfiguration, analysisInstance);
		
		Configuration memSwapConfiguration = new Configuration();
		memSwapConfiguration.setProperty(MemSwapUtilizationDisplayFilter.CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES, this.numberOfCpuAndMemSwapEntries);
		this.memSwapFilter = new MemSwapUtilizationDisplayFilter(memSwapConfiguration, analysisInstance);
		
		Configuration recordListConfiguration = new Configuration();
		recordListConfiguration.setProperty(ListCollectionFilter.CONFIG_PROPERTY_NAME_MAX_NUMBER_OF_ENTRIES, this.numberOfRecordListEntries);
		recordListConfiguration.setProperty(ListCollectionFilter.CONFIG_PROPERTY_NAME_LIST_FULL_BEHAVIOR, "dropOldest");
		this.recordListFilter = new ListCollectionFilter<Record>(recordListConfiguration, analysisInstance);
		
		Configuration responsetimeConfiguration = new Configuration();
		responsetimeConfiguration.setProperty(MethodResponsetimeDisplayFilter.CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES, this.numberOfResponsetimeEntries);
		responsetimeConfiguration.setProperty(MethodResponsetimeDisplayFilter.CONFIG_PROPERTY_NAME_RESPONSETIME_TIMEUNIT, "MILLISECONDS");
		this.responsetimeFilter = new MethodResponsetimeDisplayFilter(responsetimeConfiguration, analysisInstance);
		
		this.tagCloudFilter = new MethodAndComponentFlowDisplayFilter(new Configuration(), analysisInstance);
		
		try {
			init();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (AnalysisConfigurationException e) {
			e.printStackTrace();
		}
		this.act = new AnalysisControllerThread(analysisInstance);
		this.act.start();
	}
	
	private void init() throws IllegalStateException, AnalysisConfigurationException{
		final Configuration jmxReaderConfig = new Configuration();
		jmxReaderConfig.setProperty(JMXReader.CONFIG_PROPERTY_NAME_SILENT, "true");
		final JMXReader reader = new JMXReader(jmxReaderConfig, analysisInstance);
		
		Configuration timeReaderConfig = new Configuration();
		timeReaderConfig.setProperty(TimeReader.CONFIG_PROPERTY_NAME_UPDATE_INTERVAL_NS, this.timeReaderUpdateIntervallNS);
		final TimeReader timeReader = new TimeReader(timeReaderConfig, analysisInstance);

		final Configuration typeFilter1Config = new Configuration();
		typeFilter1Config.setProperty(TypeFilter.CONFIG_PROPERTY_NAME_TYPES, "kieker.common.record.controlflow.OperationExecutionRecord");
		final TypeFilter typeFilter1 = new TypeFilter(typeFilter1Config, analysisInstance);
		
		final Configuration typeFilter2Config = new Configuration();
		typeFilter2Config.setProperty(TypeFilter.CONFIG_PROPERTY_NAME_TYPES, "kieker.common.record.system.CPUUtilizationRecord");
		final TypeFilter typeFilter2 = new TypeFilter(typeFilter2Config, analysisInstance);
		
		final Configuration typeFilter3Config = new Configuration();
		typeFilter3Config.setProperty(TypeFilter.CONFIG_PROPERTY_NAME_TYPES, "kieker.common.record.system.MemSwapUsageRecord");
		final TypeFilter typeFilter3 = new TypeFilter(typeFilter3Config, analysisInstance);
		
		final OER2RecordFilter oer2RecordFilter = new OER2RecordFilter(new Configuration(), analysisInstance);
		
		final ExecutionRecordTransformationFilter ertf = new ExecutionRecordTransformationFilter(new Configuration(), analysisInstance);
	
		analysisInstance.connect(reader, JMXReader.OUTPUT_PORT_NAME_RECORDS,
				typeFilter1, TypeFilter.INPUT_PORT_NAME_EVENTS);
		
		analysisInstance.connect(typeFilter1, TypeFilter.OUTPUT_PORT_NAME_TYPE_MATCH,
				responsetimeFilter, MethodResponsetimeDisplayFilter.INPUT_PORT_NAME_RECORDS);
		
		analysisInstance.connect(timeReader, TimeReader.OUTPUT_PORT_NAME_TIMESTAMPS,
				responsetimeFilter, MethodResponsetimeDisplayFilter.INPUT_PORT_NAME_TIMESTAMPS);
		
		analysisInstance.connect(typeFilter1, TypeFilter.OUTPUT_PORT_NAME_TYPE_MATCH,
				tagCloudFilter, MethodAndComponentFlowDisplayFilter.INPUT_PORT_NAME_EVENTS);
		
		analysisInstance.connect(typeFilter1, TypeFilter.OUTPUT_PORT_NAME_TYPE_MATCH,
				oer2RecordFilter, OER2RecordFilter.INPUT_PORT_NAME);
		
		analysisInstance.connect(oer2RecordFilter, OER2RecordFilter.OUTPUT_PORT_NAME,
				recordListFilter, ListCollectionFilter.INPUT_PORT_NAME);
		
		analysisInstance.connect(typeFilter1, TypeFilter.OUTPUT_PORT_NAME_TYPE_MISMATCH,
				typeFilter2, TypeFilter.INPUT_PORT_NAME_EVENTS);
		
		analysisInstance.connect(typeFilter2, TypeFilter.OUTPUT_PORT_NAME_TYPE_MATCH, 
				cpuFilter, CPUUtilizationDisplayFilter.INPUT_PORT_NAME_EVENTS);
		
		analysisInstance.connect(typeFilter2, TypeFilter.OUTPUT_PORT_NAME_TYPE_MISMATCH,
				typeFilter3, TypeFilter.INPUT_PORT_NAME_EVENTS);
		
		analysisInstance.connect(typeFilter3, TypeFilter.OUTPUT_PORT_NAME_TYPE_MATCH,
				memSwapFilter, MemSwapUtilizationDisplayFilter.INPUT_PORT_NAME_EVENTS);
				
		analysisInstance.connect(reader, JMXReader.OUTPUT_PORT_NAME_RECORDS, ertf, 
				ExecutionRecordTransformationFilter.INPUT_PORT_NAME_RECORDS);
		
		analysisInstance.connect(ertf, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, 
				systemModelRepository);
	}
	
	public UpdateThread getUpdateThread(){
		return this.updateThread;
	}
	
	public SystemModelRepository getSystemModelRepository(){
		return this.systemModelRepository;
	}
	
	public CPUUtilizationDisplayFilter getCPUUtilizationDisplayFilter(){
		return this.cpuFilter;
	}
	
	public MemSwapUtilizationDisplayFilter getMemSwapUtilizationDisplayFilter(){
		return this.memSwapFilter;
	}
	
	public ListCollectionFilter<Record> getRecordListFilter(){
		return this.recordListFilter;
	}
	
	public MethodResponsetimeDisplayFilter getMethodResponsetimeDisplayFilter(){
		return this.responsetimeFilter;
	}
	
	public MethodAndComponentFlowDisplayFilter getTagCloudFilter(){
		return this.tagCloudFilter;
	}

}
