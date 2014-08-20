/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.writer.explorviz;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.IObjectRecord;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.record.misc.RegistryRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.writer.AbstractAsyncThread;
import kieker.monitoring.writer.AbstractAsyncWriter;

/**
 * 
 * @author Florian Fittkau, Jan Waller
 * 
 * @since 1.9
 */
public class ExplorVizExportWriter extends AbstractAsyncWriter {

	private static final String PREFIX = ExplorVizExportWriter.class.getName() + ".";
	public static final String CONFIG_HOSTNAME = PREFIX + "hostname"; // NOCS (afterPREFIX)
	public static final String CONFIG_PORT = PREFIX + "port"; // NOCS (afterPREFIX)
	public static final String CONFIG_BUFFERSIZE = PREFIX + "bufferSize"; // NOCS (afterPREFIX)
	public static final String CONFIG_FLUSH = PREFIX + "flush"; // NOCS (afterPREFIX)

	private final String hostname;
	private final int port;
	private final int bufferSize;
	private final boolean flush;

	public ExplorVizExportWriter(final Configuration configuration) {
		super(configuration);
		this.hostname = configuration.getStringProperty(CONFIG_HOSTNAME);
		this.port = configuration.getIntProperty(CONFIG_PORT);
		// should be check for buffers too small for a single record?
		this.bufferSize = configuration.getIntProperty(CONFIG_BUFFERSIZE);
		this.flush = configuration.getBooleanProperty(CONFIG_FLUSH);
	}

	@Override
	protected void init() throws Exception {
		this.addWorker(new ExplorVizExportWriterThread(this.monitoringController, this.blockingQueue, this.hostname, this.port, this.bufferSize, this.flush));
		this.addWorker(new ExplorVizExportWriterThread(this.monitoringController, this.prioritizedBlockingQueue, this.hostname, this.port, this.bufferSize,
				this.flush));
	}
}

/**
 * 
 * @author Florian Fittkau, Jan Waller
 * 
 * @since 1.9
 */
final class ExplorVizExportWriterThread extends AbstractAsyncThread {
	private static final Log LOG = LogFactory.getLog(ExplorVizExportWriterThread.class);

	private static final byte HOST_APPLICATION_META_DATA_CLAZZ_ID = 0;
	private static final byte BEFORE_OPERATION_CLAZZ_ID = 1;
	private static final byte AFTER_FAILED_OPERATION_CLAZZ_ID = 2;
	private static final byte AFTER_OPERATION_CLAZZ_ID = 3;
	private static final byte STRING_REGISTRY_CLAZZ_ID = 4;

	private final SocketChannel socketChannel;
	private final ByteBuffer byteBuffer;
	private final boolean flush;

	public ExplorVizExportWriterThread(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> writeQueue, final String hostname,
			final int port, final int bufferSize, final boolean flush) throws IOException {
		super(monitoringController, writeQueue);
		this.byteBuffer = ByteBuffer.allocateDirect(bufferSize);
		this.socketChannel = SocketChannel.open(new InetSocketAddress(hostname, port));
		this.flush = flush;

		this.byteBuffer.put(HOST_APPLICATION_META_DATA_CLAZZ_ID);

		final String systemName = "Default System";
		final int systemId = this.monitoringController.getUniqueIdForString(systemName);
		this.byteBuffer.putInt(systemId);

		final String ip = InetAddress.getLocalHost().getHostAddress();
		final int ipId = this.monitoringController.getUniqueIdForString(ip);
		this.byteBuffer.putInt(ipId);

		final String localHostname = monitoringController.getHostname();
		// is called to early in the initialization, so no record is created
		final int localHostnameId = this.monitoringController.getUniqueIdForString(localHostname);
		this.byteBuffer.putInt(localHostnameId);

		final String applicatioName = monitoringController.getName();
		// is called to early in the initialization, so no record is created
		final int applicationId = this.monitoringController.getUniqueIdForString(applicatioName);
		this.byteBuffer.putInt(applicationId);

		this.putRegistryRecordIntoBuffer(new RegistryRecord(systemId, systemName));
		this.putRegistryRecordIntoBuffer(new RegistryRecord(ipId, ip));
		this.putRegistryRecordIntoBuffer(new RegistryRecord(localHostnameId, localHostname));
		this.putRegistryRecordIntoBuffer(new RegistryRecord(applicationId, applicatioName));

		this.send();
	}

	@Override
	protected void consume(final IMonitoringRecord monitoringRecord) throws Exception {
		// sizes from ExplorViz not Kieker!
		int recordSize = 0;
		if (monitoringRecord instanceof BeforeOperationEvent) {
			recordSize = 37;
		} else if (monitoringRecord instanceof AfterOperationFailedEvent) {
			recordSize = 25;
		} else if (monitoringRecord instanceof AfterOperationEvent) {
			recordSize = 21;
		} else if (monitoringRecord instanceof RegistryRecord) {
			final RegistryRecord registryRecord = (RegistryRecord) monitoringRecord;
			recordSize = 9 + registryRecord.getStrBytes().length;
		}

		final ByteBuffer buffer = this.byteBuffer;
		if (recordSize > buffer.remaining()) {
			this.send();
		}
		this.convertKiekerToExplorViz(buffer, monitoringRecord);

		if (this.flush) {
			this.send();
		}
	}

	private void convertKiekerToExplorViz(final ByteBuffer buffer, final IMonitoringRecord kiekerRecord) {
		if (kiekerRecord instanceof BeforeOperationEvent) {
			final BeforeOperationEvent kiekerBefore = (BeforeOperationEvent) kiekerRecord;
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
			buffer.putInt(this.monitoringController.getUniqueIdForString(kiekerBefore.getOperationSignature()));
			buffer.putInt(this.monitoringController.getUniqueIdForString(kiekerBefore.getClassSignature()));
			// if (kiekerRecord instanceof IInterfaceRecord) {
			// final IInterfaceRecord iInterfaceRecord = (IInterfaceRecord) kiekerRecord;
			// buffer.putInt(this.monitoringController.getUniqueIdForString(iInterfaceRecord.getInterface()));
			// } else {
			buffer.putInt(this.monitoringController.getUniqueIdForString(""));
			// }
		} else if (kiekerRecord instanceof AfterOperationFailedEvent) {
			final AfterOperationFailedEvent kiekerAfterFailed = (AfterOperationFailedEvent) kiekerRecord;
			buffer.put(AFTER_FAILED_OPERATION_CLAZZ_ID);
			buffer.putLong(kiekerAfterFailed.getTimestamp());
			buffer.putLong(kiekerAfterFailed.getTraceId());
			buffer.putInt(kiekerAfterFailed.getOrderIndex());
			buffer.putInt(this.monitoringController.getUniqueIdForString(kiekerAfterFailed.getCause()));
		} else if (kiekerRecord instanceof AfterOperationEvent) {
			final AfterOperationEvent kiekerAfter = (AfterOperationEvent) kiekerRecord;
			buffer.put(AFTER_OPERATION_CLAZZ_ID);
			buffer.putLong(kiekerAfter.getTimestamp());
			buffer.putLong(kiekerAfter.getTraceId());
			buffer.putInt(kiekerAfter.getOrderIndex());
		} else if (kiekerRecord instanceof RegistryRecord) {
			final RegistryRecord registryRecord = (RegistryRecord) kiekerRecord;
			this.putRegistryRecordIntoBuffer(registryRecord);
		}
	}

	private void putRegistryRecordIntoBuffer(final RegistryRecord registryRecord) {
		final byte[] valueAsBytes = registryRecord.getStrBytes();
		this.byteBuffer.put(STRING_REGISTRY_CLAZZ_ID);
		this.byteBuffer.putInt(registryRecord.getId());
		this.byteBuffer.putInt(valueAsBytes.length);
		this.byteBuffer.put(valueAsBytes);
	}

	private void send() throws IOException {
		this.byteBuffer.flip();
		while (this.byteBuffer.hasRemaining()) {
			this.socketChannel.write(this.byteBuffer);
		}
		this.byteBuffer.clear();
	}

	@Override
	protected void cleanup() {
		try {
			this.send();
			this.socketChannel.close();
		} catch (final IOException ex) {
			LOG.error("Error closing connection", ex);
		}
	}
}
