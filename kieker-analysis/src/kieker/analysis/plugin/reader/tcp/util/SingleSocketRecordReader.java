package kieker.analysis.plugin.reader.tcp.util;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.logging.Log;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.factory.CachedRecordFactoryCatalog;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.reader.GetValueAdapter;
import kieker.common.util.registry.reader.ReaderRegistry;

public class SingleSocketRecordReader extends AbstractTcpReader {

	private static final int INT_BYTES = AbstractMonitoringRecord.TYPE_SIZE_INT;
	private static final int LONG_BYTES = AbstractMonitoringRecord.TYPE_SIZE_LONG;
	private static final Charset ENCODING = StandardCharsets.UTF_8;

	private final ReaderRegistry<String> readerRegistry = new ReaderRegistry<String>();
	private final IRegistry<String> stringRegistryWrapper;
	private final IRecordReceivedListener listener;
	private final CachedRecordFactoryCatalog recordFactories = new CachedRecordFactoryCatalog();

	public SingleSocketRecordReader(final int port, final int bufferCapacity, final Log logger, final IRecordReceivedListener listener) {
		super(port, bufferCapacity, logger);
		this.listener = listener;
		this.stringRegistryWrapper = new GetValueAdapter<String>(this.readerRegistry);
	}

	@Override
	protected boolean onBufferReceived(final ByteBuffer buffer) {
		// identify record class
		if (buffer.remaining() < INT_BYTES) {
			return false;
		}
		final int clazzId = buffer.getInt();

		if (clazzId == -1) {
			return this.registerRegistryEntry(clazzId, buffer);
		} else {
			return this.deserializeRecord(clazzId, buffer);
		}
	}

	private boolean registerRegistryEntry(final int clazzId, final ByteBuffer buffer) {
		// identify string identifier and string length
		if (buffer.remaining() < (INT_BYTES + INT_BYTES)) {
			return false;
		}

		final int id = buffer.getInt();
		final int stringLength = buffer.getInt();

		if (buffer.remaining() < stringLength) {
			return false;
		}

		final byte[] strBytes = new byte[stringLength];
		buffer.get(strBytes);
		final String string = new String(strBytes, ENCODING);

		this.readerRegistry.register(id, string);
		return true;
	}

	private boolean deserializeRecord(final int clazzId, final ByteBuffer buffer) {
		final String recordClassName = this.readerRegistry.get(clazzId);

		// identify logging timestamp
		if (buffer.remaining() < LONG_BYTES) {
			return false;
		}
		final long loggingTimestamp = buffer.getLong();

		// identify record data
		final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.recordFactories.get(recordClassName);
		if (buffer.remaining() < recordFactory.getRecordSizeInBytes()) {
			return false;
		}

		try {
			final IMonitoringRecord record = recordFactory.create(buffer, this.stringRegistryWrapper);
			record.setLoggingTimestamp(loggingTimestamp);

			this.listener.onRecordReceived(record);
		} catch (final RecordInstantiationException ex) {
			super.logger.error("Failed to create: " + recordClassName, ex);
		}

		return true;
	}

}
