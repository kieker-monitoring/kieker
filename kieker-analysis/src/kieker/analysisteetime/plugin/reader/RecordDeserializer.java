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

package kieker.analysisteetime.plugin.reader;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.factory.CachedRecordFactoryCatalog;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.BinaryValueDeserializer;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.registry.reader.ReaderRegistry;

/**
 * Represents a deserializer that deserializes records based on their id and a byte buffer.
 *
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class RecordDeserializer {

	private static final Logger LOGGER = LoggerFactory.getLogger(RecordDeserializer.class);

	private final IRecordReceivedListener listener;
	private final ReaderRegistry<String> registry;
	private final CachedRecordFactoryCatalog recordFactories;

	public RecordDeserializer(final IRecordReceivedListener listener, final ReaderRegistry<String> registry) {
		this.listener = listener;
		this.registry = registry;
		this.recordFactories = CachedRecordFactoryCatalog.getInstance();
	}

	public boolean deserializeRecord(final int clazzId, final ByteBuffer buffer) {
		// identify logging timestamp
		if (buffer.remaining() < AbstractMonitoringRecord.TYPE_SIZE_LONG) {
			return false;
		}

		final String recordClassName = this.registry.get(clazzId);
		final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.recordFactories.get(recordClassName);
		if (recordFactory == null) {
			return false;
		}

		// identify record data
		if (buffer.remaining() < recordFactory.getRecordSizeInBytes()) {
			return false;
		}

		final long loggingTimestamp = buffer.getLong();

		// PERFORMANCE ISSUE declare as field instead, as soon as possible
		final IValueDeserializer deserializer = BinaryValueDeserializer.create(buffer, this.registry);
		try {
			final IMonitoringRecord record = recordFactory.create(deserializer);
			record.setLoggingTimestamp(loggingTimestamp);

			this.listener.onRecordReceived(record);
		} catch (final RecordInstantiationException ex) {
			LOGGER.error("Failed to create: {}", recordClassName, ex);
		}

		return true;
	}
}
