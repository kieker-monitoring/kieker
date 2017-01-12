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

package kieker.monitoring.writer.serializer;

import java.nio.ByteBuffer;
import java.util.Collection;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;

/**
 * @author Holger Knoche
 *
 * @since 1.13
 */
public class StringSerializer implements IMonitoringRecordSerializer {

	public StringSerializer(final Configuration configuration) {
		// Do nothing
	}

	private static byte[] stringToBytes(final String input) {
		return input.getBytes();
	}

	@Override
	public int serializeRecord(final IMonitoringRecord record, final ByteBuffer buffer) {
		final byte[] rawData = this.serializeRecord(record);
		buffer.put(rawData);

		return rawData.length;
	}

	private byte[] serializeRecord(final IMonitoringRecord record) {
		return StringSerializer.stringToBytes(record.toString() + "\n");
	}

	@Override
	public int serializeRecords(final Collection<IMonitoringRecord> records, final ByteBuffer buffer) {
		final byte[] rawData = this.serializeRecords(records);
		buffer.put(rawData);

		return rawData.length;
	}

	private byte[] serializeRecords(final Collection<IMonitoringRecord> records) {
		final StringBuffer stringBuffer = new StringBuffer();

		for (final IMonitoringRecord record : records) {
			stringBuffer.append(record.toString());
			stringBuffer.append('\n');
		}

		return StringSerializer.stringToBytes(stringBuffer.toString());
	}

}
