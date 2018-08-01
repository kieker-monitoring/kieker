/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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
import java.nio.charset.Charset;
import java.util.Collection;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;

/**
 * String serializer for monitoring records based on the record's toString method.
 *
 * @author Holger Knoche
 *
 * @since 1.13
 */
public class StringSerializer extends AbstractMonitoringRecordSerializer {

	private static final Charset CHARSET = Charset.forName("UTF-8");
	
	/**
	 * Creates a new serializer using the given configuration.
	 *
	 * @param configuration
	 *            The configuration to use
	 */
	public StringSerializer(final Configuration configuration) {
		super(configuration);
	}

	private static byte[] stringBuilderToBytes(final StringBuilder builder) {
		return builder.toString().getBytes(CHARSET);
	}

	@Override
	public int serializeRecord(final IMonitoringRecord record, final ByteBuffer buffer) {
		final byte[] rawData = this.serializeRecord(record);
		buffer.put(rawData);

		return rawData.length;
	}

	private byte[] serializeRecord(final IMonitoringRecord record) {
		return StringSerializer.stringBuilderToBytes(this.appendSingleRecord(record, new StringBuilder()));
	}

	private StringBuilder appendSingleRecord(final IMonitoringRecord record, final StringBuilder builder) {
		builder.append(record.getClass().getName());
		builder.append(';');
		builder.append(record.getLoggingTimestamp());
		builder.append(';');
		builder.append(record.toString());
		builder.append('\n');

		return builder;
	}

	@Override
	public int serializeRecords(final Collection<IMonitoringRecord> records, final ByteBuffer buffer) {
		final byte[] rawData = this.serializeRecords(records);
		buffer.put(rawData);

		return rawData.length;
	}

	private byte[] serializeRecords(final Collection<IMonitoringRecord> records) {
		final StringBuilder builder = new StringBuilder();

		for (final IMonitoringRecord record : records) {
			this.appendSingleRecord(record, builder);
		}

		return StringSerializer.stringBuilderToBytes(builder);
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
