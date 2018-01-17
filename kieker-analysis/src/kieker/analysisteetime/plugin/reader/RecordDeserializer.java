package kieker.analysisteetime.plugin.reader;

import java.nio.ByteBuffer;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.factory.CachedRecordFactoryCatalog;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.DefaultValueDeserializer;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.util.registry.IRegistry;

/**
 * Represents a deserializer that deserializes records based on their id and a byte buffer.
 * 
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class RecordDeserializer {

	private static final Log LOGGER = LogFactory.getLog(RecordDeserializer.class);

	private final IRecordReceivedListener listener;
	private final IRegistry<String> registry;
	private final CachedRecordFactoryCatalog recordFactories;

	public RecordDeserializer(final IRecordReceivedListener listener, IRegistry<String> registry) {
		this.listener = listener;
		this.registry = registry;
		this.recordFactories = CachedRecordFactoryCatalog.getInstance();
	}

	public boolean deserializeRecord(final int clazzId, final ByteBuffer buffer) {
		// identify logging timestamp
		if (buffer.remaining() < AbstractMonitoringRecord.TYPE_SIZE_LONG) {
			return false;
		}
		final long loggingTimestamp = buffer.getLong();

		final String recordClassName = this.registry.get(clazzId);
		final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.recordFactories.get(recordClassName);
		if (recordFactory == null) {
			return false;
		}

		// identify record data
		if (buffer.remaining() < recordFactory.getRecordSizeInBytes()) {
			return false;
		}

		// PERFORMANCE ISSUE declare as field instead, as soon as possible
		final IValueDeserializer deserializer = DefaultValueDeserializer.create(buffer, this.registry);
		try {
			final IMonitoringRecord record = recordFactory.create(deserializer);
			record.setLoggingTimestamp(loggingTimestamp);

			this.listener.onRecordReceived(record);
		} catch (final RecordInstantiationException ex) {
			LOGGER.error("Failed to create: " + recordClassName, ex);
		}

		return true;
	}
}
