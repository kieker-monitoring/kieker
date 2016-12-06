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

package kieker.monitoring.writernew.explorviz;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.IObjectRecord;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.record.misc.HostApplicationMetaData;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.registry.IRegistryListener;
import kieker.monitoring.registry.IWriterRegistry;
import kieker.monitoring.registry.RegisterAdapter;
import kieker.monitoring.registry.WriterRegistry;
import kieker.monitoring.writer.explorviz.ExplorVizExportWriter;
import kieker.monitoring.writernew.AbstractMonitoringWriter;

/**
 * @author Florian Fittkau, Jan Waller, Christian Wulf
 *
 * @since 1.9
 */
public class ExplorVizTcpWriter extends AbstractMonitoringWriter implements IRegistryListener<String> {

	private static final Log LOG = LogFactory.getLog(ExplorVizTcpWriter.class);

	private static final String PREFIX = ExplorVizExportWriter.class.getName() + ".";
	public static final String CONFIG_HOSTNAME = PREFIX + "hostname"; // NOCS (afterPREFIX)
	public static final String CONFIG_PORT = PREFIX + "port"; // NOCS (afterPREFIX)
	public static final String CONFIG_BUFFERSIZE = PREFIX + "bufferSize"; // NOCS (afterPREFIX)
	public static final String CONFIG_FLUSH = PREFIX + "flush"; // NOCS (afterPREFIX)

	private static final byte HOST_APPLICATION_META_DATA_CLAZZ_ID = 0;
	private static final byte BEFORE_OPERATION_CLAZZ_ID = 1;
	private static final byte AFTER_FAILED_OPERATION_CLAZZ_ID = 2;
	private static final byte AFTER_OPERATION_CLAZZ_ID = 3;
	private static final byte STRING_REGISTRY_CLAZZ_ID = 4;

	private static final String EMPTY_STRING = "";

	private final String hostname;
	private final int port;
	private final int bufferSize;
	private final boolean flush;

	private final WritableByteChannel socketChannel;
	private final ByteBuffer byteBuffer;

	private final IWriterRegistry<String> writerRegistry;
	private final RegisterAdapter<String> registerStringsAdapter;

	public ExplorVizTcpWriter(final Configuration configuration) throws IOException {
		super(configuration);
		this.hostname = configuration.getStringProperty(CONFIG_HOSTNAME);
		this.port = configuration.getIntProperty(CONFIG_PORT);
		// should we check for buffers too small for a single record?
		this.bufferSize = configuration.getIntProperty(CONFIG_BUFFERSIZE);
		this.flush = configuration.getBooleanProperty(CONFIG_FLUSH);

		this.byteBuffer = ByteBuffer.allocateDirect(this.bufferSize);
		this.socketChannel = SocketChannel.open(new InetSocketAddress(this.hostname, this.port));

		this.writerRegistry = new WriterRegistry(this);
		this.writerRegistry.register(EMPTY_STRING);
		this.registerStringsAdapter = new RegisterAdapter<String>(this.writerRegistry);
	}

	@Override
	public void onStarting() {
		final IMonitoringController monitoringController = MonitoringController.getInstance();

		try {
			final HostApplicationMetaData record = new HostApplicationMetaData(
					"Default System",
					InetAddress.getLocalHost().getHostAddress(),
					monitoringController.getHostname(),
					monitoringController.getName());

			this.writeMonitoringRecord(record);
		} catch (final UnknownHostException e) {
			LOG.warn("An exception occurred", e);
		}
	}

	@Override
	public void writeMonitoringRecord(final IMonitoringRecord record) {
		record.registerStrings(this.registerStringsAdapter);

		// sizes from ExplorViz not Kieker!
		int recordSize = 0;
		if (record instanceof BeforeOperationEvent) {
			recordSize = 37;
		} else if (record instanceof AfterOperationFailedEvent) {
			recordSize = 25;
		} else if (record instanceof AfterOperationEvent) {
			recordSize = 21;
		} else if (record instanceof HostApplicationMetaData) {
			recordSize = 17;
		}

		final ByteBuffer buffer = this.byteBuffer;
		if (recordSize > buffer.remaining()) {
			this.send(this.byteBuffer);
		}
		this.convertKiekerToExplorViz(buffer, record);

		if (this.flush) {
			this.send(this.byteBuffer);
		}
	}

	private void convertKiekerToExplorViz(final ByteBuffer buffer, final IMonitoringRecord kiekerRecord) {
		if (kiekerRecord instanceof BeforeOperationEvent) {
			final BeforeOperationEvent kiekerBefore = (BeforeOperationEvent) kiekerRecord;

			final int opSigId = this.writerRegistry.getId(kiekerBefore.getOperationSignature());
			final int classSigId = this.writerRegistry.getId(kiekerBefore.getClassSignature());
			final int interfaceId = this.writerRegistry.getId(EMPTY_STRING);

			buffer.put(BEFORE_OPERATION_CLAZZ_ID);
			buffer.putLong(kiekerBefore.getTimestamp());
			buffer.putLong(kiekerBefore.getTraceId());
			buffer.putInt(kiekerBefore.getOrderIndex());
			if (kiekerRecord instanceof IObjectRecord) {
				final IObjectRecord iObjectRecord = (IObjectRecord) kiekerRecord;
				buffer.putInt(iObjectRecord.getObjectId());
			} else {
				buffer.putInt(0);
			}
			buffer.putInt(opSigId);
			buffer.putInt(classSigId);
			// if (kiekerRecord instanceof IInterfaceRecord) {
			// final IInterfaceRecord iInterfaceRecord = (IInterfaceRecord) kiekerRecord;
			// buffer.putInt(this.monitoringController.getUniqueIdForString(iInterfaceRecord.getInterface()));
			// } else {
			buffer.putInt(interfaceId);
			// }
		} else if (kiekerRecord instanceof AfterOperationFailedEvent) {
			final AfterOperationFailedEvent kiekerAfterFailed = (AfterOperationFailedEvent) kiekerRecord;
			buffer.put(AFTER_FAILED_OPERATION_CLAZZ_ID);
			buffer.putLong(kiekerAfterFailed.getTimestamp());
			buffer.putLong(kiekerAfterFailed.getTraceId());
			buffer.putInt(kiekerAfterFailed.getOrderIndex());
			buffer.putInt(this.writerRegistry.getId(kiekerAfterFailed.getCause()));
		} else if (kiekerRecord instanceof AfterOperationEvent) {
			final AfterOperationEvent kiekerAfter = (AfterOperationEvent) kiekerRecord;
			buffer.put(AFTER_OPERATION_CLAZZ_ID);
			buffer.putLong(kiekerAfter.getTimestamp());
			buffer.putLong(kiekerAfter.getTraceId());
			buffer.putInt(kiekerAfter.getOrderIndex());
		} else if (kiekerRecord instanceof HostApplicationMetaData) {
			final HostApplicationMetaData record = (HostApplicationMetaData) kiekerRecord;
			buffer.put(HOST_APPLICATION_META_DATA_CLAZZ_ID);
			buffer.putInt(this.writerRegistry.getId(record.getSystemName()));
			buffer.putInt(this.writerRegistry.getId(record.getIpAddress()));
			buffer.putInt(this.writerRegistry.getId(record.getHostName()));
			buffer.putInt(this.writerRegistry.getId(record.getApplicationName()));
		}
	}

	@Override
	public void onNewRegistryEntry(final String value, final int id) {
		final byte[] valueAsBytes = value.getBytes(StandardCharsets.UTF_8);

		this.byteBuffer.put(STRING_REGISTRY_CLAZZ_ID);
		this.byteBuffer.putInt(id);
		this.byteBuffer.putInt(valueAsBytes.length);
		this.byteBuffer.put(valueAsBytes);

		this.send(this.byteBuffer);
	}

	@Override
	public void onTerminating() {
		try {
			this.send(this.byteBuffer);
			this.socketChannel.close();
		} catch (final IOException ex) {
			LOG.error("Error on closing connection.", ex);
		}
	}

	private void send(final ByteBuffer buffer) {
		buffer.flip();

		try {
			while (buffer.hasRemaining()) {
				this.socketChannel.write(buffer);
			}
		} catch (final IOException e) {
			LOG.error("Error on sending registry entry.", e);
		}

		buffer.clear();
	}

}
