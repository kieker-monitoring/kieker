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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.analysis.AnalysisController;
import kieker.analysis.AnalysisControllerThread;
import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.reader.namedRecordPipe.PipeReader;
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
	public void testNamedPipeReaderReceivesFromPipe() {
		final String pipeName = NamedPipeFactory.createPipeName();
		final Configuration configuration = new Configuration(null);
		configuration.setProperty(PipeReader.CONFIG_PIPENAME, pipeName);
		final PipeReader pipeReader = new PipeReader(configuration);

		final List<IMonitoringRecord> receivedRecords = Collections.synchronizedList(new ArrayList<IMonitoringRecord>());

		final IPipeWriter writer = kieker.test.analysis.junit.util.NamedPipeFactory.createAndRegisterNamedPipeRecordWriter(pipeName);

		final MonitoringSinkClass receiver = new MonitoringSinkClass(receivedRecords);

		final AnalysisController analysis = new AnalysisController();
		analysis.registerReader(pipeReader);
		AbstractPlugin.connect(pipeReader, PipeReader.OUTPUT_PORT_NAME, receiver, MonitoringSinkClass.INPUT_PORT_NAME);
		analysis.registerFilter(receiver);
		final AnalysisControllerThread analysisThread = new AnalysisControllerThread(analysis);
		analysisThread.start();

		/*
		 * Send 7 dummy records
		 */
		final int numRecordsToSend = 7;
		for (int i = 0; i < numRecordsToSend; i++) {
			writer.newMonitoringRecord(new EmptyRecord()); // NOPMD (new in loop)
		}

		analysisThread.terminate();

		/*
		 * Make sure that numRecordsToSend where read.
		 */
		Assert.assertEquals("Unexpected number of records received", numRecordsToSend, receivedRecords.size());
	}
}

class MonitoringSinkClass extends AbstractAnalysisPlugin {

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

	@Override
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
