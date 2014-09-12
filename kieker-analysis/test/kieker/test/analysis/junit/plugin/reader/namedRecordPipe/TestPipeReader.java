/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.analysis.junit.plugin.reader.namedRecordPipe;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.AnalysisControllerThread;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.CountingFilter;
import kieker.analysis.plugin.reader.namedRecordPipe.PipeReader;
import kieker.common.configuration.Configuration;
import kieker.common.namedRecordPipe.IPipeWriter;
import kieker.common.record.misc.EmptyRecord;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.common.util.namedRecordPipe.NamedPipeWriterFactory;

/**
 * A simple test for the class <code>PipeReader</code>.
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.4
 */
public class TestPipeReader extends AbstractKiekerTest { // NOCS (MissingCtorCheck)
	// private static final Log log = LogFactory.getLog(TestPipeReader.class);

	@Test
	public void testNamedPipeReaderReceivesFromPipe() throws IllegalStateException, AnalysisConfigurationException {
		// the analysis controller
		final AnalysisController analysis = new AnalysisController();

		// the pipe
		final String pipeName = NamedPipeWriterFactory.createPipeName();

		// the reader
		final Configuration readerConfiguration = new Configuration();
		readerConfiguration.setProperty(PipeReader.CONFIG_PROPERTY_NAME_PIPENAME, pipeName);
		final PipeReader pipeReader = new PipeReader(readerConfiguration, analysis);

		// the consumer
		final Configuration countinConfiguration = new Configuration();
		final CountingFilter countingFilter = new CountingFilter(countinConfiguration, analysis);

		// the writer
		final IPipeWriter writer = NamedPipeWriterFactory.createAndRegisterNamedPipeRecordWriter(pipeName);

		analysis.connect(pipeReader, PipeReader.OUTPUT_PORT_NAME_RECORDS, countingFilter, CountingFilter.INPUT_PORT_NAME_EVENTS);

		final AnalysisControllerThread analysisThread = new AnalysisControllerThread(analysis);
		analysisThread.start(); // start asynchronously
		// Send 7 dummy records
		final int numRecordsToSend = 7;
		for (int i = 0; i < numRecordsToSend; i++) {
			writer.newMonitoringRecord(new EmptyRecord());
		}

		analysisThread.terminate();
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, analysis.getState());

		// Make sure that numRecordsToSend where read.
		Assert.assertEquals("Unexpected number of records received", numRecordsToSend, countingFilter.getMessageCount());
	}
}
