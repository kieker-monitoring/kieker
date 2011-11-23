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
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.analysis.AnalysisController;
import kieker.analysis.AnalysisControllerThread;
import kieker.analysis.configuration.Configuration;
import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.reader.namedRecordPipe.PipeReader;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IMonitoringRecordReceiver;
import kieker.test.analysis.junit.util.DummyRecord;
import kieker.test.analysis.junit.util.NamedPipeFactory;

import org.junit.Test;

/**
 * 
 * @author Andre van Hoorn
 * 
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

		final IMonitoringRecordReceiver writer = kieker.test.analysis.junit.util.NamedPipeFactory.createAndRegisterNamedPipeRecordWriter(pipeName);

		final Receiver receiver = new Receiver(receivedRecords);

		final AnalysisController analysis = new AnalysisController();
		analysis.setReader(pipeReader);
		pipeReader.getAllOutputPorts()[0].subscribe(receiver.getInputPort());
		analysis.registerPlugin(receiver);
		final AnalysisControllerThread analysisThread = new AnalysisControllerThread(analysis);
		analysisThread.start();

		/*
		 * Send 7 dummy records
		 */
		final int numRecordsToSend = 7;
		for (int i = 0; i < numRecordsToSend; i++) {
			writer.newMonitoringRecord(new DummyRecord()); // NOPMD (new in loop)
		}

		analysisThread.terminate();

		/*
		 * Make sure that numRecordsToSend where read.
		 */
		Assert.assertEquals("Unexpected number of records received", numRecordsToSend, receivedRecords.size());
	}

	class Receiver extends AbstractAnalysisPlugin {

		private final List<IMonitoringRecord> receivedRecords;

		public Receiver(final List<IMonitoringRecord> receivedRecords) {
			super(new Configuration(null));
			this.receivedRecords = receivedRecords;
		}

		private final AbstractInputPort input = new AbstractInputPort("in", Collections.unmodifiableCollection(new CopyOnWriteArrayList<Class<?>>(
				new Class<?>[] { IMonitoringRecord.class }))) {

			@Override
			public void newEvent(final Object event) {
				Receiver.this.receivedRecords.add((IMonitoringRecord) event);
			}
		};

		@Override
		public boolean execute() {
			/* no need to do anything */
			return true;
		}

		@Override
		public void terminate(final boolean error) {
			/* do nothing */
		}

		@Override
		protected Properties getDefaultProperties() {
			return new Properties();
		}

		public AbstractInputPort getInputPort() {
			return this.input;
		}

		@Override
		public Configuration getCurrentConfiguration() {
			final Configuration configuration = new Configuration(null);

			// TODO: Save the current configuration

			return configuration;
		}
	}
}
