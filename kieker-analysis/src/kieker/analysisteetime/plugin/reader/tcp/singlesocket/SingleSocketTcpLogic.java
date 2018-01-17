package kieker.analysisteetime.plugin.reader.tcp.singlesocket;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;

import kieker.analysisteetime.plugin.reader.IRecordReceivedListener;
import kieker.analysisteetime.plugin.reader.RecordDeserializer;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.util.registry.reader.GetValueAdapter;
import kieker.common.util.registry.reader.ReaderRegistry;

import teetime.util.io.network.AbstractTcpReader;

class SingleSocketTcpLogic extends AbstractTcpReader {

	private static final int INT_BYTES = AbstractMonitoringRecord.TYPE_SIZE_INT;
	private static final Charset ENCODING = StandardCharsets.UTF_8;

	private final ReaderRegistry<String> readerRegistry = new ReaderRegistry<String>();
	private RecordDeserializer recordDeserializer;

	public SingleSocketTcpLogic(final int port, final int bufferCapacity, final Logger logger,
			final IRecordReceivedListener listener) {
		super(port, bufferCapacity, logger);

		final GetValueAdapter<String> stringRegistryWrapper = new GetValueAdapter<String>(this.readerRegistry);
		this.recordDeserializer = new RecordDeserializer(listener, stringRegistryWrapper);
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
			return this.recordDeserializer.deserializeRecord(clazzId, buffer);
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

}
