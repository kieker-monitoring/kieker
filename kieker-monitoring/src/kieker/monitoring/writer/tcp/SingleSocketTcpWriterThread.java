package kieker.monitoring.writer.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.IRegistry;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.writer.AbstractAsyncThread;
import kieker.monitoring.writer.RecordSerializer;

/**
 * @author Christian Wulf
 *
 * @since 1.12
 */
class SingleSocketTcpWriterThread extends AbstractAsyncThread {
	private static final Log LOG = LogFactory.getLog(SingleSocketTcpWriterThread.class);

	private final SocketChannel socketChannel;
	private final ByteBuffer byteBuffer;
	private final IRegistry<String> stringRegistry;
	private final boolean flush;
	private final RecordSerializer recordSerializer;

	public SingleSocketTcpWriterThread(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> writeQueue, final String hostname,
			final int port, final int bufferSize, final boolean flush) throws IOException {
		super(monitoringController, writeQueue);
		this.byteBuffer = ByteBuffer.allocateDirect(bufferSize);
		this.socketChannel = SocketChannel.open(new InetSocketAddress(hostname, port));
		this.stringRegistry = this.monitoringController.getStringRegistry();
		this.flush = flush;
		this.recordSerializer = new RecordSerializer(this.stringRegistry);
	}

	@Override
	protected void consume(final IMonitoringRecord monitoringRecord) throws Exception {
		final ByteBuffer buffer = this.byteBuffer;
		final int requiredBufferSize = monitoringRecord.getSize() + AbstractMonitoringRecord.TYPE_SIZE_INT + AbstractMonitoringRecord.TYPE_SIZE_LONG;

		if (requiredBufferSize > buffer.remaining()) {
			buffer.flip();
			while (buffer.hasRemaining()) {
				this.socketChannel.write(buffer);
			}
			buffer.clear();
		}

		this.recordSerializer.serialize(monitoringRecord, buffer);

		if (this.flush) {
			buffer.flip();
			while (buffer.hasRemaining()) {
				this.socketChannel.write(buffer);
			}
			buffer.clear();
		}
	}

	@Override
	protected void cleanup() {
		try {
			final ByteBuffer buffer = this.byteBuffer;
			buffer.flip();
			while (buffer.hasRemaining()) {
				this.socketChannel.write(buffer);
			}
			this.socketChannel.close();
		} catch (final IOException ex) {
			LOG.error("Error closing connection", ex);
		}
	}
}
