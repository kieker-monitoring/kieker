/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.test.tools.junit.writeRead.filesystem.simple;

import java.util.List;

import junit.framework.Assert;
import kieker.analysis.AnalysisController;
import kieker.analysis.AnalysisControllerThread;
import kieker.analysis.plugin.reader.namedRecordPipe.PipeReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writer.namedRecordPipe.PipeWriter;
import kieker.test.analysis.junit.plugin.SimpleSinkPlugin;
import kieker.test.tools.junit.writeRead.filesystem.AbstractWriterReaderTest;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public class NamedRecordPipeWriterReaderTest extends AbstractWriterReaderTest {
	private static final String PIPE_NAME = "pipe-IVvirGREEf";

	private volatile SimpleSinkPlugin<IMonitoringRecord> sinkFilter;
	private volatile AnalysisControllerThread analysisThread;

	@Override
	protected void setUp() throws Exception {
		final Configuration pipeReaderConfig = new Configuration();
		pipeReaderConfig.setProperty(PipeReader.CONFIG_PROPERTY_NAME_PIPENAME, NamedRecordPipeWriterReaderTest.PIPE_NAME);
		final PipeReader pipeReader = new PipeReader(pipeReaderConfig);
		this.sinkFilter = new SimpleSinkPlugin<IMonitoringRecord>(new Configuration());
		final AnalysisController analysisController = new AnalysisController();
		analysisController.registerReader(pipeReader);
		analysisController.registerFilter(this.sinkFilter);
		analysisController.connect(pipeReader, PipeReader.OUTPUT_PORT_NAME_RECORDS, this.sinkFilter, SimpleSinkPlugin.INPUT_PORT_NAME);
		this.analysisThread = new AnalysisControllerThread(analysisController);
		this.analysisThread.start();
	}

	@Override
	protected IMonitoringController createController(final int numRecordsWritten) {
		final Configuration config = ConfigurationFactory.createDefaultConfiguration();
		config.setProperty(ConfigurationFactory.WRITER_CLASSNAME, PipeWriter.class.getName());
		config.setProperty(PipeWriter.CONFIG_PIPENAME, NamedRecordPipeWriterReaderTest.PIPE_NAME);
		return MonitoringController.createInstance(config);
	}

	@Override
	protected void checkControllerStateAfterRecordsPassedToController(final IMonitoringController monitoringController) {
		Assert.assertTrue(monitoringController.isMonitoringEnabled());
	}

	@Override
	protected void inspectRecords(final List<IMonitoringRecord> eventsPassedToController, final List<IMonitoringRecord> eventFromMonitoringLog) {
		Assert.assertEquals("Unexpected set of records", eventsPassedToController, eventFromMonitoringLog);
	}

	@Override
	protected boolean terminateBeforeLogInspection() {
		return false;
	}

	@Override
	protected List<IMonitoringRecord> readEvents() {
		return this.sinkFilter.getList();
	}
}
