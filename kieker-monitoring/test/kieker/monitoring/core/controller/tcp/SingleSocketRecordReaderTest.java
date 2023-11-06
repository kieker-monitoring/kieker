/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.monitoring.core.controller.tcp;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IRecordReceivedListener;
import kieker.common.record.io.BinaryValueSerializer;
import kieker.common.record.io.IValueSerializer;
import kieker.common.record.misc.RegistryRecord;
import kieker.common.record.remotecontrol.ActivationParameterEvent;
import kieker.common.registry.IRegistryListener;
import kieker.common.registry.writer.IWriterRegistry;
import kieker.common.registry.writer.WriterRegistry;

/**
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ SingleSocketRecordReader.class, AbstractTcpReader.class, SocketChannel.class })
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "javax.management.*"})
public class SingleSocketRecordReaderTest {

	private static final int RECEIVING_PORT = 1234;
	private static final int BUFFER_CAPACITY = 6553500;
	private static final Logger LOGGER = LoggerFactory.getLogger(SingleSocketRecordReaderTest.class);
	private static final Integer CHUNK_SIZE = 1024;
	private static final String DUMMY_IP_ADDRESS = "192.168.48.12";

	/** test constructor. */
	public SingleSocketRecordReaderTest() {
		// nothing to do here
	}

	/** Test whether the buffer is processed corretly. */
	@Test
	public void testOnBufferReceived() {
		final ByteBuffer buffer = ByteBuffer.allocate(SingleSocketRecordReaderTest.BUFFER_CAPACITY);

		final ServerSocketChannel mockServerSocket = Mockito.mock(ServerSocketChannel.class);
		// Set it first
		final SocketChannel mockSocketChannel = Mockito.mock(SocketChannel.class);

		try {
			// Then mock it
			Mockito.when(mockServerSocket.accept()).thenReturn(mockSocketChannel);
			Mockito.when(mockSocketChannel.read(buffer)).thenReturn(SingleSocketRecordReaderTest.CHUNK_SIZE);
		} catch (final IOException e) {
			Assert.fail(e.getMessage());
		}

		// create record
		final ActivationParameterEvent event = new ActivationParameterEvent("classname", "whitelist",
				this.createValues(100000));

		final IRecordReceivedListener receiver = new IRecordReceivedListener() {

			@Override
			public void onRecordReceived(final IMonitoringRecord record) {
				Assert.assertTrue("Records must match", record.equals(event));
			}
		};
		final SingleSocketRecordReader reader = new SingleSocketRecordReader(
				SingleSocketRecordReaderTest.RECEIVING_PORT, SingleSocketRecordReaderTest.BUFFER_CAPACITY,
				SingleSocketRecordReaderTest.LOGGER, receiver);

		this.createData(buffer, event);

		final ByteBuffer writeBuffer = ByteBuffer.allocate(65535);

		for (final byte value : buffer.array()) {
			writeBuffer.mark();
			writeBuffer.put(value);
			writeBuffer.reset();
			reader.onBufferReceived(writeBuffer);
		}
	}

	private void createData(final ByteBuffer buffer, final IMonitoringRecord event) {
		// create serialization
		final IRegistryListener<String> registryListener = new IRegistryListener<String>() {

			@Override
			public void onNewRegistryEntry(final String value, final int id) {
				final byte[] bytes = value.getBytes(StandardCharsets.UTF_8);

				buffer.putInt(RegistryRecord.CLASS_ID);
				buffer.putInt(id);
				buffer.putInt(value.length());
				buffer.put(bytes);
			}
		};
		final IWriterRegistry<String> registry = new WriterRegistry(registryListener);
		final IValueSerializer serializer = BinaryValueSerializer.create(buffer, registry);
		event.serialize(serializer);
	}

	private String[] createValues(final int amount) {
		final String[] values = new String[amount];
		for (int i = 0; i < amount; i++) {
			values[i] = SingleSocketRecordReaderTest.DUMMY_IP_ADDRESS;
		}
		return values;
	}

}
