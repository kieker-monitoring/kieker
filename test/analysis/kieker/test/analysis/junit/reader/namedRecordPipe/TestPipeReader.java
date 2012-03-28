/***************************************************************************
 * Copyright 2011 by
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

package kieker.test.analysis.junit.reader.namedRecordPipe;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.analysis.AnalysisController;
import kieker.analysis.AnalysisControllerThread;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.analysis.plugin.filter.forward.CountingFilter;
import kieker.analysis.plugin.reader.namedRecordPipe.PipeReader;
import kieker.common.configuration.Configuration;
import kieker.common.namedRecordPipe.IPipeWriter;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.EmptyRecord;
import kieker.test.analysis.junit.util.NamedPipeFactory;

import org.junit.Test;

/**
 * A simple test for the class <code>PipeReader</code>.
 * 
 * @author Andre van Hoorn
 */
public class TestPipeReader extends TestCase { // NOCS (MissingCtorCheck)
	// private static final Log log = LogFactory.getLog(TestPipeReader.class);

	@Test
	public void testNamedPipeReaderReceivesFromPipe() throws IllegalStateException, AnalysisConfigurationException {
		// the pipe
		final String pipeName = NamedPipeFactory.createPipeName();

		// the reader
		final Configuration readerConfiguration = new Configuration();
		readerConfiguration.setProperty(PipeReader.CONFIG_PROPERTY_NAME_PIPENAME, pipeName);
		final PipeReader pipeReader = new PipeReader(readerConfiguration);

		// the consumer
		final Configuration countinConfiguration = new Configuration();
		final CountingFilter countingFilter = new CountingFilter(countinConfiguration);

		// the writer
		final IPipeWriter writer = NamedPipeFactory.createAndRegisterNamedPipeRecordWriter(pipeName);

		// the analysis controller
		final AnalysisController analysis = new AnalysisController(this.getName());
		analysis.registerReader(pipeReader);
		analysis.registerFilter(countingFilter);
		analysis.connect(pipeReader, PipeReader.OUTPUT_PORT_NAME_RECORDS, countingFilter, CountingFilter.INPUT_PORT_NAME_EVENTS);

		final AnalysisControllerThread analysisThread = new AnalysisControllerThread(analysis);
		analysisThread.start(); // start asynchronously
		/*
		 * Send 7 dummy records
		 */
		final int numRecordsToSend = 7;
		for (int i = 0; i < numRecordsToSend; i++) {
			writer.newMonitoringRecord(new EmptyRecord());
		}

		analysisThread.terminate();

		/*
		 * Make sure that numRecordsToSend where read.
		 */
		Assert.assertEquals("Unexpected number of records received", numRecordsToSend, countingFilter.getMessageCount());
	}
}

class MonitoringSinkClass extends AbstractFilterPlugin { // NOPMD (subclass of Test)

	public static final String INPUT_PORT_NAME = "doJob";
	private final List<IMonitoringRecord> receivedRecords;

	public MonitoringSinkClass(final List<IMonitoringRecord> receivedRecords) {
		super(new Configuration());
		this.receivedRecords = receivedRecords;
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	@InputPort(
			name = MonitoringSinkClass.INPUT_PORT_NAME,
			eventTypes = { IMonitoringRecord.class })
	public void doJob(final Object data) {
		this.receivedRecords.add((IMonitoringRecord) data);
	}
}
