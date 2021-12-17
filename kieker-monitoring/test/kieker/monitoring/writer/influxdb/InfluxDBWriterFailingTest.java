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

package kieker.monitoring.writer.influxdb;

import java.io.IOException;

import org.junit.Assume;
import org.junit.Test;

import kieker.OSUtil;
import kieker.common.configuration.Configuration;

/**
 * @author Teerat Pitakrat
 *
 * @since 1.13
 */
public class InfluxDBWriterFailingTest {

	/**
	 * Constructor.
	 */
	public InfluxDBWriterFailingTest() {
		super();
	}

	/**
	 * Test method for {@link kieker.monitoring.writer.influxdb.InfluxDBWriter#connectToInfluxDB()}.
	 *
	 * @throws IOException
	 *             If connection to InfluxDB fails.
	 */
	@Test(expected = IOException.class)
	public void testConnectToInfluxDB() throws IOException {
		Assume.assumeFalse(OSUtil.isWindows()); 
		final Configuration configuration = new Configuration();
		configuration.setProperty(InfluxDBWriter.CONFIG_PROPERTY_DB_URL, "http://localhost");
		configuration.setProperty(InfluxDBWriter.CONFIG_PROPERTY_DB_PORT, "80");
		configuration.setProperty(InfluxDBWriter.CONFIG_PROPERTY_DB_USERNAME, "root");
		configuration.setProperty(InfluxDBWriter.CONFIG_PROPERTY_DB_PASSWORD, "root");
		configuration.setProperty(InfluxDBWriter.CONFIG_PROPERTY_DB_NAME, "kieker");
		final InfluxDBWriter influxDBWriter = new InfluxDBWriter(configuration);
		influxDBWriter.connectToInfluxDB();
	}

}
