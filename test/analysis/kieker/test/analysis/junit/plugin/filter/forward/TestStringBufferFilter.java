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
import kieker.common.record.misc.EmptyRecord;

/**
 * Andre van Hoorn
 */
public class TestStringBufferFilter {
	@Test
	public void testLoggingTimestampRemains() throws IllegalStateException, AnalysisConfigurationException {
		final AnalysisController analysisController = new AnalysisController();

		final long timestamp = 3268936l;
		final IMonitoringRecord recordIn = new EmptyRecord();
		recordIn.setLoggingTimestamp(timestamp);

		final ListReader<IMonitoringRecord> reader = new ListReader<IMonitoringRecord>(new Configuration());
		reader.addObject(recordIn);
		analysisController.registerReader(reader);

		final StringBufferFilter stringBufferFilter = new StringBufferFilter(new Configuration());
		analysisController.registerFilter(stringBufferFilter);
		analysisController.connect(reader, ListReader.OUTPUT_PORT_NAME, stringBufferFilter, StringBufferFilter.INPUT_PORT_NAME_EVENTS);

		final ListCollectionFilter<IMonitoringRecord> collectionFilter = new ListCollectionFilter<IMonitoringRecord>(new Configuration());
		analysisController.registerFilter(collectionFilter);
		analysisController.connect(stringBufferFilter, StringBufferFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, collectionFilter, ListCollectionFilter.INPUT_PORT_NAME);

		analysisController.run();

		final List<IMonitoringRecord> records = collectionFilter.getList();
		Assert.assertEquals("Unexpected number of records", 1, records.size());
		final IMonitoringRecord recordOut = records.get(0);
		Assert.assertEquals("Unexpected logging timestamp", timestamp, recordOut.getLoggingTimestamp());
	}
}
