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

package kieker.test.tools.junit.writeRead.database;

import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.reader.database.DBReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.test.analysis.junit.plugin.SimpleSinkPlugin;

import org.junit.Test;

/**
 * @author Jan Waller
 */
public class BasicDBWriterReaderTest {
	public static final String DRIVERCLASSNAME = "org.apache.derby.jdbc.EmbeddedDriver";
	public static final String CONNECTIONSTRING = "jdbc:derby:tmp/KIEKER;user=DBUSER;password=DBPASS";
	public static final String TABLEPREFIX = "kieker";

	@Test
	public void testDBReader() throws Exception {
		final Configuration dbReaderConfig = new Configuration();
		dbReaderConfig.setProperty(DBReader.CONFIG_PROPERTY_NAME_DRIVERCLASSNAME, DRIVERCLASSNAME);
		dbReaderConfig.setProperty(DBReader.CONFIG_PROPERTY_NAME_CONNECTIONSTRING, CONNECTIONSTRING);
		dbReaderConfig.setProperty(DBReader.CONFIG_PROPERTY_NAME_TABLEPREFIX, TABLEPREFIX);
		final DBReader dbReader = new DBReader(dbReaderConfig);
		final SimpleSinkPlugin<IMonitoringRecord> sinkFilter = new SimpleSinkPlugin<IMonitoringRecord>(new Configuration());
		final AnalysisController analysisController = new AnalysisController();
		analysisController.registerReader(dbReader);
		analysisController.registerFilter(sinkFilter);
		analysisController.connect(dbReader, DBReader.OUTPUT_PORT_NAME_RECORDS, sinkFilter, SimpleSinkPlugin.INPUT_PORT_NAME);
		// analysisController.run();
	}
}
