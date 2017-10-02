/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.examples.livedemo.analysis;

import kieker.analysis.AnalysisController;
import kieker.analysis.AnalysisControllerThread;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.jmx.JmxReader;
import kieker.analysis.plugin.reader.timer.TimeReader;
import kieker.common.configuration.Configuration;
import kieker.examples.livedemo.analysis.filter.OperationExecutionRecordEnrichmentFilter;
import kieker.examples.livedemo.analysis.select.Distributor;
import kieker.examples.livedemo.analysis.sink.AbstractAggregatingDisplayFilter;
import kieker.examples.livedemo.analysis.sink.AbstractNonAggregatingDisplayFilter;
import kieker.examples.livedemo.analysis.sink.CPUUtilizationDisplayFilter;
import kieker.examples.livedemo.analysis.sink.ClassLoadingDisplayFilter;
import kieker.examples.livedemo.analysis.sink.CompilationDisplayFilter;
import kieker.examples.livedemo.analysis.sink.ComponentFlowDisplayFilter;
import kieker.examples.livedemo.analysis.sink.GCCountDisplayFilter;
import kieker.examples.livedemo.analysis.sink.GCTimeDisplayFilter;
import kieker.examples.livedemo.analysis.sink.JVMHeapDisplayFilter;
import kieker.examples.livedemo.analysis.sink.JVMNonHeapDisplayFilter;
import kieker.examples.livedemo.analysis.sink.MemoryDisplayFilter;
import kieker.examples.livedemo.analysis.sink.MethodFlowDisplayFilter;
import kieker.examples.livedemo.analysis.sink.MethodResponsetimeDisplayFilter;
import kieker.examples.livedemo.analysis.sink.SwapDisplayFilter;
import kieker.examples.livedemo.analysis.sink.ThreadsStatusDisplayFilter;
import kieker.examples.livedemo.common.EnrichedOperationExecutionRecord;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.executionRecordTransformation.ExecutionRecordTransformationFilter;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public final class LiveDemoAnalysis {

	private static final LiveDemoAnalysis INSTANCE = new LiveDemoAnalysis();

	private static final String NUMBER_OF_CPU_AND_MEMSWAP_ENTRIES = "20";
	private static final String NUMBER_OF_RECORD_LIST_ENTRIES = "50";
	private static final String NUMBER_OF_RESPONSE_TIME_ENTRIES = "20";
	private static final String TIME_READER_UPDATE_INTERVAL_NS = "2000000000";

	private final IAnalysisController analysisController = new AnalysisController();
	private final AnalysisControllerThread analysisControllerThread = new AnalysisControllerThread(this.analysisController);

	private final SystemModelRepository systemModelRepository;
	private final CPUUtilizationDisplayFilter cpuFilter;
	private final ListCollectionFilter<EnrichedOperationExecutionRecord> recordListFilter;
	private final MethodResponsetimeDisplayFilter responsetimeFilter;
	private final MethodFlowDisplayFilter methodFlowDisplayFilter;
	private final ComponentFlowDisplayFilter componentFlowDisplayFilter;
	private final ClassLoadingDisplayFilter classLoadingDisplayFilter;
	private final ThreadsStatusDisplayFilter threadsStatusDisplayFilter;
	private final CompilationDisplayFilter jitCompilationDisplayFilter;
	private final GCCountDisplayFilter gcCountDisplayFilter;
	private final GCTimeDisplayFilter gcTimeDisplayFilter;
	private final JVMHeapDisplayFilter jvmHeapDisplayFilter;
	private final JVMNonHeapDisplayFilter jvmNonHeapDisplayFilter;
	private final MemoryDisplayFilter memoryDisplayFilter;
	private final SwapDisplayFilter swapDisplayFilter;

	private LiveDemoAnalysis() {
		this.systemModelRepository = new SystemModelRepository(new Configuration(), this.analysisController);

		final Configuration cpuConfiguration = new Configuration();
		cpuConfiguration.setProperty(CPUUtilizationDisplayFilter.CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES, LiveDemoAnalysis.NUMBER_OF_CPU_AND_MEMSWAP_ENTRIES);
		this.cpuFilter = new CPUUtilizationDisplayFilter(cpuConfiguration, this.analysisController);

		final Configuration memSwapConfiguration = new Configuration();
		memSwapConfiguration.setProperty(AbstractNonAggregatingDisplayFilter.CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES,
				LiveDemoAnalysis.NUMBER_OF_CPU_AND_MEMSWAP_ENTRIES);
		this.memoryDisplayFilter = new MemoryDisplayFilter(memSwapConfiguration, this.analysisController);
		this.swapDisplayFilter = new SwapDisplayFilter(memSwapConfiguration, this.analysisController);

		final Configuration recordListConfiguration = new Configuration();
		recordListConfiguration.setProperty(ListCollectionFilter.CONFIG_PROPERTY_NAME_MAX_NUMBER_OF_ENTRIES, LiveDemoAnalysis.NUMBER_OF_RECORD_LIST_ENTRIES);
		recordListConfiguration.setProperty(ListCollectionFilter.CONFIG_PROPERTY_NAME_LIST_FULL_BEHAVIOR, "dropOldest");
		this.recordListFilter = new ListCollectionFilter<EnrichedOperationExecutionRecord>(recordListConfiguration, this.analysisController);

		final Configuration responsetimeConfiguration = new Configuration();
		responsetimeConfiguration.setProperty(AbstractAggregatingDisplayFilter.CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES,
				LiveDemoAnalysis.NUMBER_OF_RESPONSE_TIME_ENTRIES);
		this.responsetimeFilter = new MethodResponsetimeDisplayFilter(responsetimeConfiguration, this.analysisController);

		final Configuration classLoadingConfiguration = new Configuration();
		classLoadingConfiguration.setProperty(AbstractAggregatingDisplayFilter.CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES,
				LiveDemoAnalysis.NUMBER_OF_RESPONSE_TIME_ENTRIES);
		this.classLoadingDisplayFilter = new ClassLoadingDisplayFilter(classLoadingConfiguration, this.analysisController);

		final Configuration threadsStatusConfiguration = new Configuration();
		threadsStatusConfiguration
				.setProperty(AbstractAggregatingDisplayFilter.CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES, LiveDemoAnalysis.NUMBER_OF_RESPONSE_TIME_ENTRIES);
		this.threadsStatusDisplayFilter = new ThreadsStatusDisplayFilter(threadsStatusConfiguration, this.analysisController);

		this.jitCompilationDisplayFilter = new CompilationDisplayFilter(threadsStatusConfiguration, this.analysisController);
		this.gcCountDisplayFilter = new GCCountDisplayFilter(threadsStatusConfiguration, this.analysisController);
		this.gcTimeDisplayFilter = new GCTimeDisplayFilter(threadsStatusConfiguration, this.analysisController);
		this.jvmHeapDisplayFilter = new JVMHeapDisplayFilter(threadsStatusConfiguration, this.analysisController);
		this.jvmNonHeapDisplayFilter = new JVMNonHeapDisplayFilter(threadsStatusConfiguration, this.analysisController);

		this.methodFlowDisplayFilter = new MethodFlowDisplayFilter(new Configuration(), this.analysisController);
		this.componentFlowDisplayFilter = new ComponentFlowDisplayFilter(new Configuration(), this.analysisController);
	}

	public void initializeAnalysis() throws IllegalStateException, AnalysisConfigurationException {
		final Configuration jmxReaderConfig = new Configuration();
		jmxReaderConfig.setProperty(JmxReader.CONFIG_PROPERTY_NAME_SILENT, "true");
		final JmxReader reader = new JmxReader(jmxReaderConfig, this.analysisController);

		final Configuration timeReaderConfig = new Configuration();
		timeReaderConfig.setProperty(TimeReader.CONFIG_PROPERTY_NAME_UPDATE_INTERVAL_NS, LiveDemoAnalysis.TIME_READER_UPDATE_INTERVAL_NS);
		final TimeReader timeReader = new TimeReader(timeReaderConfig, this.analysisController);

		final Distributor distributor = new Distributor(new Configuration(), this.analysisController);

		final OperationExecutionRecordEnrichmentFilter oer2RecordFilter = new OperationExecutionRecordEnrichmentFilter(new Configuration(), this.analysisController);

		final ExecutionRecordTransformationFilter ertf = new ExecutionRecordTransformationFilter(new Configuration(), this.analysisController);

		this.analysisController.connect(reader, JmxReader.OUTPUT_PORT_NAME_RECORDS, distributor, Distributor.INPUT_PORT_NAME_RECORDS);

		this.analysisController.connect(distributor, Distributor.OUTPUT_PORT_NAME_OPERATION_EXECUTION_RECORDS, this.responsetimeFilter,
				AbstractAggregatingDisplayFilter.INPUT_PORT_NAME_RECORDS);
		this.analysisController.connect(timeReader, TimeReader.OUTPUT_PORT_NAME_TIMESTAMPS, this.responsetimeFilter,
				AbstractAggregatingDisplayFilter.INPUT_PORT_NAME_TIMESTAMPS);

		this.analysisController.connect(distributor, Distributor.OUTPUT_PORT_NAME_OPERATION_EXECUTION_RECORDS, this.methodFlowDisplayFilter,
				AbstractNonAggregatingDisplayFilter.INPUT_PORT_NAME_RECORDS);

		this.analysisController.connect(distributor, Distributor.OUTPUT_PORT_NAME_OPERATION_EXECUTION_RECORDS, this.componentFlowDisplayFilter,
				AbstractNonAggregatingDisplayFilter.INPUT_PORT_NAME_RECORDS);

		this.analysisController.connect(distributor, Distributor.OUTPUT_PORT_NAME_OPERATION_EXECUTION_RECORDS,
				oer2RecordFilter, OperationExecutionRecordEnrichmentFilter.INPUT_PORT_NAME_RECORDS);

		this.analysisController.connect(oer2RecordFilter, OperationExecutionRecordEnrichmentFilter.OUTPUT_PORT_NAME_RECORDS, this.recordListFilter,
				ListCollectionFilter.INPUT_PORT_NAME);

		this.analysisController.connect(distributor, Distributor.OUTPUT_PORT_NAME_CPU_UTILIZATION_RECORDS, this.cpuFilter,
				CPUUtilizationDisplayFilter.INPUT_PORT_NAME_RECORDS);

		this.analysisController.connect(distributor, Distributor.OUTPUT_PORT_NAME_MEM_SWAP_USAGE_RECORDS, this.memoryDisplayFilter,
				AbstractNonAggregatingDisplayFilter.INPUT_PORT_NAME_RECORDS);
		this.analysisController.connect(distributor, Distributor.OUTPUT_PORT_NAME_MEM_SWAP_USAGE_RECORDS, this.swapDisplayFilter,
				AbstractNonAggregatingDisplayFilter.INPUT_PORT_NAME_RECORDS);

		this.analysisController.connect(distributor, Distributor.OUTPUT_PORT_NAME_CLASS_LOADING_RECORDS, this.classLoadingDisplayFilter,
				AbstractNonAggregatingDisplayFilter.INPUT_PORT_NAME_RECORDS);

		this.analysisController.connect(distributor, Distributor.OUTPUT_PORT_NAME_THREADS_STATUS_RECORDS, this.threadsStatusDisplayFilter,
				AbstractNonAggregatingDisplayFilter.INPUT_PORT_NAME_RECORDS);

		this.analysisController.connect(distributor, Distributor.OUTPUT_PORT_NAME_COMPILATION_RECORDS, this.jitCompilationDisplayFilter,
				AbstractNonAggregatingDisplayFilter.INPUT_PORT_NAME_RECORDS);

		this.analysisController.connect(distributor, Distributor.OUTPUT_PORT_NAME_GC_RECORDS, this.gcCountDisplayFilter,
				AbstractNonAggregatingDisplayFilter.INPUT_PORT_NAME_RECORDS);

		this.analysisController.connect(distributor, Distributor.OUTPUT_PORT_NAME_GC_RECORDS, this.gcTimeDisplayFilter,
				AbstractNonAggregatingDisplayFilter.INPUT_PORT_NAME_RECORDS);

		this.analysisController.connect(distributor, Distributor.OUTPUT_PORT_NAME_JVM_MEMORY_RECORDS, this.jvmHeapDisplayFilter,
				AbstractNonAggregatingDisplayFilter.INPUT_PORT_NAME_RECORDS);
		this.analysisController.connect(distributor, Distributor.OUTPUT_PORT_NAME_JVM_MEMORY_RECORDS, this.jvmNonHeapDisplayFilter,
				AbstractNonAggregatingDisplayFilter.INPUT_PORT_NAME_RECORDS);

		this.analysisController.connect(reader, JmxReader.OUTPUT_PORT_NAME_RECORDS, ertf, ExecutionRecordTransformationFilter.INPUT_PORT_NAME_RECORDS);

		this.analysisController.connect(ertf, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, this.systemModelRepository);
	}

	public SystemModelRepository getSystemModelRepository() {
		return this.systemModelRepository;
	}

	public CPUUtilizationDisplayFilter getCPUUtilizationDisplayFilter() {
		return this.cpuFilter;
	}

	public MemoryDisplayFilter getMemoryDisplayFilter() {
		return this.memoryDisplayFilter;
	}

	public SwapDisplayFilter getSwapDisplayFilter() {
		return this.swapDisplayFilter;
	}

	public ListCollectionFilter<EnrichedOperationExecutionRecord> getRecordListFilter() {
		return this.recordListFilter;
	}

	public MethodResponsetimeDisplayFilter getMethodResponsetimeDisplayFilter() {
		return this.responsetimeFilter;
	}

	public MethodFlowDisplayFilter getMethodFlowDisplayFilter() {
		return this.methodFlowDisplayFilter;
	}

	public ComponentFlowDisplayFilter getComponentFlowDisplayFilter() {
		return this.componentFlowDisplayFilter;
	}

	public ClassLoadingDisplayFilter getClassLoadingDisplayFilter() {
		return this.classLoadingDisplayFilter;
	}

	public ThreadsStatusDisplayFilter getThreadsStatusDisplayFilter() {
		return this.threadsStatusDisplayFilter;
	}

	public CompilationDisplayFilter getJitCompilationDisplayFilter() {
		return this.jitCompilationDisplayFilter;
	}

	public GCCountDisplayFilter getGcCountDisplayFilter() {
		return this.gcCountDisplayFilter;
	}

	public GCTimeDisplayFilter getGcTimeDisplayFilter() {
		return this.gcTimeDisplayFilter;
	}

	public JVMHeapDisplayFilter getJvmHeapMemoryDisplayFilter() {
		return this.jvmHeapDisplayFilter;
	}

	public JVMNonHeapDisplayFilter getJvmNonHeapMemoryDisplayFilter() {
		return this.jvmNonHeapDisplayFilter;
	}

	public void startAnalysis() {
		this.analysisControllerThread.start();
	}

	public static LiveDemoAnalysis getInstance() {
		return LiveDemoAnalysis.INSTANCE;
	}

}
