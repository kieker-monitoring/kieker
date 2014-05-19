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
package kieker.panalysis.examples.recordReader;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.IMonitoringRecord;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class RecordReaderAnalysisTest {

	@Test
	public void test() {
		final File inputFile = new File("examples/userguide/ch5--trace-monitoring-aspectj/testdata");
		final List<IMonitoringRecord> records = new LinkedList<IMonitoringRecord>();

		final RecordReaderAnalysis recordReaderAnalysis = new RecordReaderAnalysis();
		recordReaderAnalysis.init();
		recordReaderAnalysis.setInputFile(inputFile);
		recordReaderAnalysis.setOutputRecordList(records);
		recordReaderAnalysis.start();

		Assert.assertEquals(100, records.size());

		Assert.assertEquals(2349872345907l, records.get(0).getLoggingTimestamp());

	}
}
