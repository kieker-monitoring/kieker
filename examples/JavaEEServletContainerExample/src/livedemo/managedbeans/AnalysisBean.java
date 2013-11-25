/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package livedemo.managedbeans;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import kieker.analysis.AnalysisController;
import kieker.analysis.AnalysisControllerThread;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.filter.select.TypeFilter;
import kieker.analysis.plugin.filter.sink.CPUUtilizationDisplayFilter;
import kieker.analysis.plugin.filter.sink.MemSwapUtilizationDisplayFilter;
import kieker.analysis.plugin.filter.sink.MethodAndComponentFlowDisplayFilter;
import kieker.analysis.plugin.reader.jmx.JMXReader;
import kieker.analysis.plugin.reader.timer.TimeReader;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.executionRecordTransformation.ExecutionRecordTransformationFilter;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

import livedemo.entities.EnrichedOERecord;
import livedemo.filter.MethodResponsetimeDisplayFilter;
import livedemo.filter.OER2EnrichedOERFilter;

/**
 * @author Bjoern Weissenfels
 * 
 * @since 1.9
 */
@ManagedBean(name = "analysisBean", eager = true)
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
	private final ListCollectionFilter<EnrichedOERecord> recordListFilter;
	private final MethodResponsetimeDisplayFilter responsetimeFilter;
	private final MethodAndComponentFlowDisplayFilter tagCloudFilter;

	public AnalysisBean() {
		this.updateThread = new UpdateThread(1000); // will notify its observers every second
		this.updateThread.start();

		this.analysisInstance = new AnalysisController();

		this.systemModelRepository = new SystemModelRepository(new Configuration(), this.analysisInstance);

		final Configuration cpuConfiguration = new Configuration();
		cpuConfiguration.setProperty(CPUUtilizationDisplayFilter.CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES, this.numberOfCpuAndMemSwapEntries);
		this.cpuFilter = new CPUUtilizationDisplayFilter(cpuConfiguration, this.analysisInstance);

		final Configuration memSwapConfiguration = new Configuration();
		memSwapConfiguration.setProperty(MemSwapUtilizationDisplayFilter.CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES, this.numberOfCpuAndMemSwapEntries);
		this.memSwapFilter = new MemSwapUtilizationDisplayFilter(memSwapConfiguration, this.analysisInstance);

		final Configuration recordListConfiguration = new Configuration();
		recordListConfiguration.setProperty(ListCollectionFilter.CONFIG_PROPERTY_NAME_MAX_NUMBER_OF_ENTRIES, this.numberOfRecordListEntries);
		recordListConfiguration.setProperty(ListCollectionFilter.CONFIG_PROPERTY_NAME_LIST_FULL_BEHAVIOR, "dropOldest");
		this.recordListFilter = new ListCollectionFilter<EnrichedOERecord>(recordListConfiguration, this.analysisInstance);

		final Configuration responsetimeConfiguration = new Configuration();
		responsetimeConfiguration.setProperty(MethodResponsetimeDisplayFilter.CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES, this.numberOfResponsetimeEntries);
		responsetimeConfiguration.setProperty(MethodResponsetimeDisplayFilter.CONFIG_PROPERTY_NAME_RESPONSETIME_TIMEUNIT, "MILLISECONDS");
		this.responsetimeFilter = new MethodResponsetimeDisplayFilter(responsetimeConfiguration, this.analysisInstance);

		this.tagCloudFilter = new MethodAndComponentFlowDisplayFilter(new Configuration(), this.analysisInstance);

		try {
			this.init();
		} catch (final IllegalStateException e) {
			e.printStackTrace();
		} catch (final AnalysisConfigurationException e) {
			e.printStackTrace();
		}
		this.act = new AnalysisControllerThread(this.analysisInstance);
		this.act.start();
	}

	private void init() throws IllegalStateException, AnalysisConfigurationException {
		final Configuration jmxReaderConfig = new Configuration();
		jmxReaderConfig.setProperty(JMXReader.CONFIG_PROPERTY_NAME_SILENT, "true");
		final JMXReader reader = new JMXReader(jmxReaderConfig, this.analysisInstance);

		final Configuration timeReaderConfig = new Configuration();
		timeReaderConfig.setProperty(TimeReader.CONFIG_PROPERTY_NAME_UPDATE_INTERVAL_NS, this.timeReaderUpdateIntervallNS);
		final TimeReader timeReader = new TimeReader(timeReaderConfig, this.analysisInstance);

		final Configuration typeFilter1Config = new Configuration();
		typeFilter1Config.setProperty(TypeFilter.CONFIG_PROPERTY_NAME_TYPES, "kieker.common.record.controlflow.OperationExecutionRecord");
		final TypeFilter typeFilter1 = new TypeFilter(typeFilter1Config, this.analysisInstance);

		final Configuration typeFilter2Config = new Configuration();
		typeFilter2Config.setProperty(TypeFilter.CONFIG_PROPERTY_NAME_TYPES, "kieker.common.record.system.CPUUtilizationRecord");
		final TypeFilter typeFilter2 = new TypeFilter(typeFilter2Config, this.analysisInstance);

		final Configuration typeFilter3Config = new Configuration();
		typeFilter3Config.setProperty(TypeFilter.CONFIG_PROPERTY_NAME_TYPES, "kieker.common.record.system.MemSwapUsageRecord");
		final TypeFilter typeFilter3 = new TypeFilter(typeFilter3Config, this.analysisInstance);

		final OER2EnrichedOERFilter oer2RecordFilter = new OER2EnrichedOERFilter(new Configuration(), this.analysisInstance);

		final ExecutionRecordTransformationFilter ertf = new ExecutionRecordTransformationFilter(new Configuration(), this.analysisInstance);

		this.analysisInstance.connect(reader, JMXReader.OUTPUT_PORT_NAME_RECORDS,
				typeFilter1, TypeFilter.INPUT_PORT_NAME_EVENTS);

		this.analysisInstance.connect(typeFilter1, TypeFilter.OUTPUT_PORT_NAME_TYPE_MATCH,
				this.responsetimeFilter, MethodResponsetimeDisplayFilter.INPUT_PORT_NAME_RECORDS);

		this.analysisInstance.connect(timeReader, TimeReader.OUTPUT_PORT_NAME_TIMESTAMPS,
				this.responsetimeFilter, MethodResponsetimeDisplayFilter.INPUT_PORT_NAME_TIMESTAMPS);

		this.analysisInstance.connect(typeFilter1, TypeFilter.OUTPUT_PORT_NAME_TYPE_MATCH,
				this.tagCloudFilter, MethodAndComponentFlowDisplayFilter.INPUT_PORT_NAME_EVENTS);

		this.analysisInstance.connect(typeFilter1, TypeFilter.OUTPUT_PORT_NAME_TYPE_MATCH,
				oer2RecordFilter, OER2EnrichedOERFilter.INPUT_PORT_NAME);

		this.analysisInstance.connect(oer2RecordFilter, OER2EnrichedOERFilter.OUTPUT_PORT_NAME,
				this.recordListFilter, ListCollectionFilter.INPUT_PORT_NAME);

		this.analysisInstance.connect(typeFilter1, TypeFilter.OUTPUT_PORT_NAME_TYPE_MISMATCH,
				typeFilter2, TypeFilter.INPUT_PORT_NAME_EVENTS);

		this.analysisInstance.connect(typeFilter2, TypeFilter.OUTPUT_PORT_NAME_TYPE_MATCH,
				this.cpuFilter, CPUUtilizationDisplayFilter.INPUT_PORT_NAME_EVENTS);

		this.analysisInstance.connect(typeFilter2, TypeFilter.OUTPUT_PORT_NAME_TYPE_MISMATCH,
				typeFilter3, TypeFilter.INPUT_PORT_NAME_EVENTS);

		this.analysisInstance.connect(typeFilter3, TypeFilter.OUTPUT_PORT_NAME_TYPE_MATCH,
				this.memSwapFilter, MemSwapUtilizationDisplayFilter.INPUT_PORT_NAME_EVENTS);

		this.analysisInstance.connect(reader, JMXReader.OUTPUT_PORT_NAME_RECORDS, ertf,
				ExecutionRecordTransformationFilter.INPUT_PORT_NAME_RECORDS);

		this.analysisInstance.connect(ertf, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL,
				this.systemModelRepository);
	}

	public UpdateThread getUpdateThread() {
		return this.updateThread;
	}

	public SystemModelRepository getSystemModelRepository() {
		return this.systemModelRepository;
	}

	public CPUUtilizationDisplayFilter getCPUUtilizationDisplayFilter() {
		return this.cpuFilter;
	}

	public MemSwapUtilizationDisplayFilter getMemSwapUtilizationDisplayFilter() {
		return this.memSwapFilter;
	}

	public ListCollectionFilter<EnrichedOERecord> getRecordListFilter() {
		return this.recordListFilter;
	}

	public MethodResponsetimeDisplayFilter getMethodResponsetimeDisplayFilter() {
		return this.responsetimeFilter;
	}

	public MethodAndComponentFlowDisplayFilter getTagCloudFilter() {
		return this.tagCloudFilter;
	}

}
