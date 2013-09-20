/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.writer.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.RegistryRecord;
import kieker.common.util.registry.IRegistry;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.writer.AbstractMonitoringWriter;

/**
 * 
 * @author Florian Fittkau, Jan Waller
 * 
 * @since 1.8
 */
public final class TCPDisruptorWriter extends AbstractMonitoringWriter {
	private static final String PREFIX = TCPDisruptorWriter.class.getName() + ".";
	public static final String CONFIG_HOSTNAME = PREFIX + "hostname"; // NOCS (afterPREFIX)
	public static final String CONFIG_PORT1 = PREFIX + "port1"; // NOCS (afterPREFIX)
	public static final String CONFIG_PORT2 = PREFIX + "port2"; // NOCS (afterPREFIX)
	public static final String CONFIG_BUFFERSIZE = PREFIX + "bufferSize"; // NOCS (afterPREFIX)
	public static final String CONFIG_FLUSH = PREFIX + "flush"; // NOCS (afterPREFIX)

	private RingBuffer<MonitoringRecordDisruptorEvent> ringBuffer;
	EventHandler<MonitoringRecordDisruptorEvent>[] eventHandlers;
	private final String hostname;
	private final int port1;
	private final int port2;
	private final int bufferSize;

	@SuppressWarnings("unchecked")
	public TCPDisruptorWriter(final Configuration configuration) throws IOException {
		super(configuration);
		this.hostname = configuration.getStringProperty(CONFIG_HOSTNAME);
		this.port1 = configuration.getIntProperty(CONFIG_PORT1);
		this.port2 = configuration.getIntProperty(CONFIG_PORT2);
		this.bufferSize = configuration.getIntProperty(CONFIG_BUFFERSIZE);

	}

	public boolean newMonitoringRecord(final IMonitoringRecord record) {
		final long hiseq = this.ringBuffer.next();
		final MonitoringRecordDisruptorEvent valueEvent = this.ringBuffer.get(hiseq);
		valueEvent.setValue(record);
		this.ringBuffer.publish(hiseq);
		return true;
	}

	public void terminate() {
		for (final EventHandler<MonitoringRecordDisruptorEvent> eventHandler : this.eventHandlers) {
			if (eventHandler instanceof TCPWriterEventHandler) {
				final TCPWriterEventHandler tcpWriterEventHandler = (TCPWriterEventHandler) eventHandler;
				tcpWriterEventHandler.cleanup();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void init() throws Exception {
		final ExecutorService exec = Executors.newCachedThreadPool();
		final Disruptor<MonitoringRecordDisruptorEvent> disruptor = new Disruptor<MonitoringRecordDisruptorEvent>(
				MonitoringRecordDisruptorEvent.EVENT_FACTORY, 32768, exec);

		this.eventHandlers = new EventHandler[1];
		this.eventHandlers[0] = new TCPWriterEventHandler(this.monitoringController, this.hostname, this.port1, this.port2, this.bufferSize);
		disruptor.handleEventsWith(this.eventHandlers);

		this.ringBuffer = disruptor.start();
	}

}

/**
 * 
 * @author Florian Fittkau, Jan Waller
 * 
 * @since 1.8
 */
final class TCPWriterEventHandler implements EventHandler<MonitoringRecordDisruptorEvent> {
	private static final Log LOG = LogFactory.getLog(TCPWriterEventHandler.class);

	private final SocketChannel socketChannelRecords;
	private final SocketChannel socketChannelStrings;
	private final ByteBuffer byteBuffer;
	private final IMonitoringController monitoringController;
	private final IRegistry<String> stringRegistry;

	public TCPWriterEventHandler(final IMonitoringController monitoringController, final String hostname,
			final int port1, final int port2, final int bufferSize) throws IOException {
		this.monitoringController = monitoringController;
		this.byteBuffer = ByteBuffer.allocateDirect(bufferSize);
		this.socketChannelRecords = SocketChannel.open(new InetSocketAddress(hostname, port1));
		this.socketChannelStrings = SocketChannel.open(new InetSocketAddress(hostname, port2));
		this.stringRegistry = this.monitoringController.getStringRegistry();
	}

	protected void cleanup() {
		try {
			this.byteBuffer.flip();
			this.socketChannelRecords.write(this.byteBuffer);
			this.socketChannelRecords.close();
		} catch (final IOException ex) {
			LOG.error("Error closing connection", ex);
		}
		try {
			this.socketChannelStrings.close();
		} catch (final IOException ex) {
			LOG.error("Error closing connection", ex);
		}
	}

	public void onEvent(final MonitoringRecordDisruptorEvent event, final long sequence, final boolean endOfBatch) throws Exception {
		final IMonitoringRecord monitoringRecord = event.getValue();

		if (monitoringRecord instanceof RegistryRecord) {
			final int size = monitoringRecord.getSize();
			final ByteBuffer buffer = ByteBuffer.allocateDirect(size);
			monitoringRecord.writeBytes(buffer, this.stringRegistry);
			buffer.flip();
			this.socketChannelStrings.write(buffer);
		} else {
			final ByteBuffer buffer = this.byteBuffer;
			if ((monitoringRecord.getSize() + 4 + 8) > buffer.remaining()) {
				buffer.flip();
				this.socketChannelRecords.write(buffer);
				buffer.clear();
			}
			buffer.putInt(this.monitoringController.getUniqueIdForString(monitoringRecord.getClass().getName()));
			buffer.putLong(monitoringRecord.getLoggingTimestamp());
			monitoringRecord.writeBytes(buffer, this.stringRegistry);
			if (endOfBatch) {
				buffer.flip();
				this.socketChannelRecords.write(buffer);
				buffer.clear();
			}
		}
	}
}

/**
 * WARNING: This is a mutable object which will be recycled by the RingBuffer.
 * You must take a copy of data it holds before the framework recycles it.
 * 
 * @author Florian Fittkau, Jan Waller
 * 
 * @since 1.8
 */
final class MonitoringRecordDisruptorEvent {
	private IMonitoringRecord value;

	public final IMonitoringRecord getValue() {
		return this.value;
	}

	public void setValue(final IMonitoringRecord value) {
		this.value = value;
	}

	public final static EventFactory<MonitoringRecordDisruptorEvent> EVENT_FACTORY = new EventFactory<MonitoringRecordDisruptorEvent>() {
		public MonitoringRecordDisruptorEvent newInstance() {
			return new MonitoringRecordDisruptorEvent();
		}
	};
}
