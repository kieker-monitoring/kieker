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

package kieker.test.tools.junit.writeRead.database;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.reader.database.DbReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.controller.IMonitoringController;

import kieker.test.analysis.util.plugin.filter.SimpleSinkFilter;
import kieker.test.tools.junit.writeRead.AbstractWriterReaderTest;

/**
 * @author Jan Waller
 */
public abstract class AbstractTestDbWriterReader extends AbstractWriterReaderTest {

	public static final String DRIVERCLASSNAME = "org.apache.derby.jdbc.EmbeddedDriver";
	public static final String TABLEPREFIX = "kieker";

	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder(); // NOCS (@Rule must be public)

	public String getConnectionString() throws IOException {
		return "jdbc:derby:" + this.tmpFolder.getRoot().getCanonicalPath() + "/KIEKER;user=DBUSER;password=DBPASS";
	}

	@Override
	protected List<IMonitoringRecord> readEvents() throws Exception {
		final Configuration dbReaderConfig = new Configuration();
		dbReaderConfig.setProperty(DbReader.CONFIG_PROPERTY_NAME_DRIVERCLASSNAME, DRIVERCLASSNAME);
		dbReaderConfig.setProperty(DbReader.CONFIG_PROPERTY_NAME_CONNECTIONSTRING, this.getConnectionString());
		dbReaderConfig.setProperty(DbReader.CONFIG_PROPERTY_NAME_TABLEPREFIX, TABLEPREFIX);
		final DbReader dbReader = new DbReader(dbReaderConfig);
		final SimpleSinkFilter<IMonitoringRecord> sinkFilter = new SimpleSinkFilter<IMonitoringRecord>(new Configuration());
		final AnalysisController analysisController = new AnalysisController();
		analysisController.registerReader(dbReader);
		analysisController.registerFilter(sinkFilter);
		analysisController.connect(dbReader, DbReader.OUTPUT_PORT_NAME_RECORDS, sinkFilter, SimpleSinkFilter.INPUT_PORT_NAME);
		analysisController.run();
		return sinkFilter.getList();
	}

	@Override
	protected void inspectRecords(final List<IMonitoringRecord> eventsPassedToController, final List<IMonitoringRecord> eventsFromMonitoringLog) throws Exception {
		// TODO currently the reader screws the order completely
		// Collections.sort(eventsPassedToController);
		final IMonitoringRecord[] eventsPassed = eventsPassedToController.toArray(new IMonitoringRecord[eventsPassedToController.size()]);
		Arrays.sort(eventsPassed);
		// Collections.sort(eventsFromMonitoringLog);
		final IMonitoringRecord[] eventsFrom = eventsFromMonitoringLog.toArray(new IMonitoringRecord[eventsFromMonitoringLog.size()]);
		Arrays.sort(eventsFrom);
		// Assert.assertEquals("Unexpected set of records", eventsPassedToController, eventsFromMonitoringLog);
		Assert.assertTrue("Unexpected set of records", Arrays.equals(eventsPassed, eventsFrom));
	}

	@Override
	protected void checkControllerStateAfterRecordsPassedToController(final IMonitoringController monitoringController) throws Exception {
		Assert.assertTrue(monitoringController.isMonitoringEnabled());
	}

	@Override
	protected void checkControllerStateBeforeRecordsPassedToController(final IMonitoringController monitoringController) throws Exception {
		Assert.assertTrue(monitoringController.isMonitoringEnabled());
	}

	@Override
	protected boolean terminateBeforeLogInspection() { // NOPMD (empty method)
		return false;
	}
}
