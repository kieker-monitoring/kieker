/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.monitoring.junit.writer.collector;

import java.io.IOException;
import java.nio.ByteBuffer;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.monitoring.writer.raw.IRawDataWriter;

/**
 * Raw data writer for test purposes, which writes to the test data storage.
 *
 * @author Holger Knoche
 *
 * @since 1.13
 */
public class TestRawDataWriter implements IRawDataWriter {

	private static final Log LOG = LogFactory.getLog(TestRawDataWriter.class);
	private static final String PREFIX = TestRawDataWriter.class.getName() + ".";

	/** Configuration property for the test ID */
	public static final String CONFIG_TEST_ID = PREFIX + "testId"; // NOCS (afterPREFIX)

	private final String testId;
	private final TestRawDataStorage dataStorage;

	public TestRawDataWriter(final Configuration configuration) {
		this.testId = configuration.getStringProperty(CONFIG_TEST_ID);
		this.dataStorage = TestRawDataStorage.getInstance();
	}

	@Override
	public void writeData(final ByteBuffer data) {
		final byte[] dataArray = new byte[data.limit()];
		data.get(dataArray);

		try {
			this.dataStorage.appendData(this.testId, dataArray);
		} catch (final IOException e) {
			// Should not happen
            LOG.info(e.getMessage());
		}
	}

	@Override
	public boolean requiresWrappedData() {
		// TODO Actually, this writer requires wrapped data
		return false;
	}
	
	@Override
	public void onInitialization() {
		// Nothing to do
	}

	@Override
	public void onTermination() {
		// Nothing to do
	}

}
