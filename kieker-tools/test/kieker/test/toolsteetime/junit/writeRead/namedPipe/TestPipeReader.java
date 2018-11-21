/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.toolsteetime.junit.writeRead.namedPipe;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysisteetime.plugin.reader.namedRecordPipe.PipeReader;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.EmptyRecord;
import kieker.monitoring.writer.namedRecordPipe.PipeWriter;

import kieker.test.common.junit.AbstractKiekerTest;

import teetime.framework.test.StageTester;

/**
 * A simple test for the class <code>PipeReader</code>.
 *
 * @author Andre van Hoorn
 *
 * @since 1.4
 */
public class TestPipeReader extends AbstractKiekerTest {

	public TestPipeReader() {
		super();
	}

	@Test
	public void testNamedPipeReaderReceivesFromPipe() throws InterruptedException {
		// the pipe
		final String pipeName = NamedPipeWriterFactory.createPipeName();

		// the reader
		final PipeReaderThread pipeReaderThread = new PipeReaderThread(pipeName);

		// the writer
		final PipeWriter writer = NamedPipeWriterFactory.createAndRegisterNamedPipeRecordWriter(pipeName);

		pipeReaderThread.start();

		// wait a second to make sure the reader is ready
		Thread.sleep(1000);

		// Send 7 dummy records
		final int numRecordsToSend = 7;
		for (int i = 0; i < numRecordsToSend; i++) {
			writer.writeMonitoringRecord(new EmptyRecord());
		}

		// Make sure that numRecordsToSend where read.
		Assert.assertEquals("Unexpected number of records received", numRecordsToSend, pipeReaderThread.getOutputList().size());

	}

	/**
	 * Extra thread for PipeReader for testing.
	 *
	 * @author Lars Bluemke
	 */
	private static class PipeReaderThread extends Thread {

		private final List<IMonitoringRecord> outputList;
		private final PipeReader pipeReader;

		public PipeReaderThread(final String pipeName) {
			this.outputList = new LinkedList<>();
			this.pipeReader = new PipeReader(pipeName);
		}

		@Override
		public void run() {
			StageTester.test(this.pipeReader).and().receive(this.outputList).from(this.pipeReader.getOutputPort()).start();
		}

		public List<IMonitoringRecord> getOutputList() {
			return this.outputList;
		}
	}
}
