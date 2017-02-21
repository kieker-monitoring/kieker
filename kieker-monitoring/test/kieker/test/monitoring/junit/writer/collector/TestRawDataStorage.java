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

package kieker.test.monitoring.junit.writer.collector;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Raw data storage for test purposes. This storage allows to store raw byte data under arbitrary
 * string IDs, and to retrieve the data. It is purposefully built to be a singleton, so
 * that passing a reference to the writer is unnecessary.
 *
 * @author Holger Knoche
 *
 * @since 1.13
 */
public final class TestRawDataStorage {

	private static final TestRawDataStorage INSTANCE = new TestRawDataStorage();

	private final Map<String, ByteArrayOutputStream> rawData;

	private TestRawDataStorage() {
		this.rawData = new HashMap<>();
	}

	/**
	 * Retrieves the singleton instance.
	 *
	 * @return See above
	 */
	public static TestRawDataStorage getInstance() {
		return INSTANCE;
	}

	/**
	 * Appends the given data to the data already stored under the given ID. If no such data
	 * exists, it is just stored.
	 *
	 * @param id
	 *            The ID to store the data under
	 * @param data
	 *            The data to store
	 */
	public synchronized void appendData(final String id, final byte[] data) throws IOException {
		ByteArrayOutputStream stream = this.rawData.get(id);

		if (stream == null) {
			stream = new ByteArrayOutputStream();
			this.rawData.put(id, stream);
		}

		stream.write(data);
	}

	/**
	 * Retrieves the data currently stored under the given ID.
	 *
	 * @param id
	 *            The ID to retrieve the data for
	 * @return The stored data, or {@code null} if no data exists
	 */
	public synchronized byte[] getData(final String id) {
		final ByteArrayOutputStream stream = this.rawData.get(id);

		if (stream == null) {
			return null;
		}

		return stream.toByteArray();
	}

}
