/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.analysis.junit.plugin.filter.forward;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.filter.forward.StringBufferFilter;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;

/**
 * Andre van Hoorn
 */
public class TestStringBufferFilter {
	@Test
	public void testLoggingTimestampRemains() throws IllegalStateException, AnalysisConfigurationException {
		final AnalysisController analysisController = new AnalysisController();

		long timestamp = 3268936l;
		final IMonitoringRecord recordIn1 = TestStringBufferFilter.createOperationExecutionRecord(timestamp++);
		final IMonitoringRecord recordIn2 = TestStringBufferFilter.createOperationExecutionRecord(timestamp++);

		final ListReader<IMonitoringRecord> reader = new ListReader<IMonitoringRecord>(new Configuration());
		reader.addObject(recordIn1);
		reader.addObject(recordIn2);
		analysisController.registerReader(reader);

		final StringBufferFilter stringBufferFilter = new StringBufferFilter(new Configuration());
		analysisController.registerFilter(stringBufferFilter);
		analysisController.connect(reader, ListReader.OUTPUT_PORT_NAME, stringBufferFilter, StringBufferFilter.INPUT_PORT_NAME_EVENTS);

		final ListCollectionFilter<IMonitoringRecord> collectionFilter = new ListCollectionFilter<IMonitoringRecord>(new Configuration());
		analysisController.registerFilter(collectionFilter);
		analysisController.connect(stringBufferFilter, StringBufferFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, collectionFilter, ListCollectionFilter.INPUT_PORT_NAME);

		analysisController.run();

		final List<IMonitoringRecord> records = collectionFilter.getList();
		Assert.assertEquals("Unexpected number of records", 2, records.size());
		final IMonitoringRecord recordOut1 = records.get(0);
		final IMonitoringRecord recordOut2 = records.get(0);
		Assert.assertNotSame("First output record same as first input record", recordIn2, recordOut2); // includes String, hence NOT "as-is"
		Assert.assertEquals("First output record doesn't equal first input record", recordIn1, recordOut1); // ... but must be equal
		Assert.assertNotSame("Second output record same as second input record", recordIn2, recordOut2); // includes String, hence NOT "as-is"
		Assert.assertEquals("Second output record doesn't equal second input record", recordIn2, recordOut2); // ... but must be equal
	}

	private static OperationExecutionRecord createOperationExecutionRecord(final long loggingTimestamp) {
		final OperationExecutionRecord opRec = new OperationExecutionRecord("mySignatureStringWhichShouldBeCached", OperationExecutionRecord.NO_SESSION_ID,
				OperationExecutionRecord.NO_TRACEID, OperationExecutionRecord.NO_TIMESTAMP, OperationExecutionRecord.NO_TIMESTAMP,
				OperationExecutionRecord.NO_HOSTNAME, OperationExecutionRecord.NO_EOI_ESS, OperationExecutionRecord.NO_EOI_ESS);
		opRec.setLoggingTimestamp(loggingTimestamp);
		return opRec;
	}
}
