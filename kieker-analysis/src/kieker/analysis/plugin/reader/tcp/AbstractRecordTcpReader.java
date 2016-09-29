package kieker.analysis.plugin.reader.tcp;

import java.nio.ByteBuffer;

import kieker.analysis.plugin.reader.tcp.util.AbstractTcpReader;
import kieker.common.exception.RecordInstantiationException;
import kieker.common.logging.Log;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.factory.CachedRecordFactoryCatalog;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.ILookup;

public abstract class AbstractRecordTcpReader extends AbstractTcpReader {

	private static final int INT_BYTES = AbstractMonitoringRecord.TYPE_SIZE_INT;
	private static final int LONG_BYTES = AbstractMonitoringRecord.TYPE_SIZE_LONG;

	private final CachedRecordFactoryCatalog recordFactories;
	// BETTER use a non thread-safe implementation to increase performance. A thread-safe version is not necessary.
	private final ILookup<String> stringRegistry;

	/**
	 * Default constructor with <code>port=10133</code> and <code>bufferCapacity=65535</code> and <code>new CachedRecordFactoryCatalog()</code>
	 */
	public AbstractRecordTcpReader(final Log logger, final ILookup<String> stringRegistry) {
		this(10133, 65535, logger, stringRegistry, new CachedRecordFactoryCatalog());
	}

	/**
	 *
	 * @param port
	 *            accept connections on this port
	 * @param bufferCapacity
	 *            capacity of the receiving buffer
	 */
	public AbstractRecordTcpReader(final int port, final int bufferCapacity, final Log logger, final ILookup<String> stringRegistry,
			final CachedRecordFactoryCatalog recordFactories) {
		super(port, bufferCapacity, logger);
		this.stringRegistry = stringRegistry;
		this.recordFactories = recordFactories;
	}

	@Override
	protected final boolean onBufferReceived(final ByteBuffer buffer) {
		// identify record class
		if (buffer.remaining() < INT_BYTES) {
			return false;
		}
		final int clazzId = buffer.getInt();
		final String recordClassName = this.stringRegistry.get(clazzId);

		// identify logging timestamp
		if (buffer.remaining() < LONG_BYTES) {
			return false;
		}
		final long loggingTimestamp = buffer.getLong();

		// identify record data
		final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.recordFactories.get(recordClassName);
		if (buffer.remaining() < recordFactory.getRecordSizeInBytes()) { // includes the case where size is -1
			return false;
		}

		try {
			final IMonitoringRecord record = recordFactory.create(buffer, this.stringRegistry);
			record.setLoggingTimestamp(loggingTimestamp);

			this.onRecordReceived(record);
		} catch (final RecordInstantiationException ex) {
			super.logger.error("Failed to create: " + recordClassName, ex);
		}

		return true;
	}

	protected abstract void onRecordReceived(IMonitoringRecord record);

}
