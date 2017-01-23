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

package kieker.test.toolsteetime.junit.logReplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.EmptyRecord;
import kieker.common.record.system.MemSwapUsageRecord;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * Tests the {@link AbstractLogReplayer}.
 *
 * @author Andre van Hoorn, Lars Bluemke
 *
 * @since 1.6
 */
public class TestLogReplayer extends AbstractKiekerTest {

	/** A rule making sure that a temporary folder exists for every test method (which is removed after the test). */
	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder(); // NOCS (@Rule must be public)

	private volatile File monitoringConfigurationFile;
	private volatile List<IMonitoringRecord> recordListFilledByListWriter;
	private final List<IMonitoringRecord> replayList = new ArrayList<IMonitoringRecord>();

	/**
	 * Creates a new instance of this class.
	 */
	public TestLogReplayer() {
		// Adding arbitrary records
		this.replayList.add(new EmptyRecord());
		this.replayList.add(
				new MemSwapUsageRecord(1, "myHost",
						17, // memTotal
						3, // memUsed
						14, // memFree
						100, // swapTotal
						0, // swapUsed
						100 // swapFree
				));
		this.replayList.add(new EmptyRecord());
	}
}

// Blocked: ListReader must be migrated first!
/// **
// * @author Andre van Hoorn
// *
// * @since 1.6
// */
// class ListReplayer extends kieker.toolsteetime.logReplayer.AbstractLogReplayer { // NOPMD
// private final List<IMonitoringRecord> replayList = new ArrayList<IMonitoringRecord>();
//
// public ListReplayer(final IMonitoringController monitoringController, final String monitoringConfigurationFile, final boolean realtimeMode,
// final TimeUnit realtimeTimeunit, final double realtimeAccelerationFactor, final boolean keepOriginalLoggingTimestamps,
// final int numRealtimeWorkerThreads, final long ignoreRecordsBeforeTimestamp, final long ignoreRecordsAfterTimestamp,
// final List<IMonitoringRecord> replayList) {
//
// super(monitoringController, monitoringConfigurationFile, realtimeMode, realtimeTimeunit, realtimeAccelerationFactor,
// ignoreRecordsBeforeTimestamp, ignoreRecordsAfterTimestamp);
// this.replayList.addAll(replayList);
// }
//
// @Override
// protected AbstractReaderPlugin createReader(final IAnalysisController analysisController) {
// final Configuration listReaderConfig = new Configuration();
// listReaderConfig.setProperty(ListReader.CONFIG_PROPERTY_NAME_AWAIT_TERMINATION, Boolean.toString(Boolean.FALSE));
// final ListReader<IMonitoringRecord> listReader = new ListReader<IMonitoringRecord>(listReaderConfig, analysisController);
// listReader.addAllObjects(this.replayList);
// return listReader;
// }
//
// @Override
// protected AbstractReader createReader() {
// return null;
// }
// }
