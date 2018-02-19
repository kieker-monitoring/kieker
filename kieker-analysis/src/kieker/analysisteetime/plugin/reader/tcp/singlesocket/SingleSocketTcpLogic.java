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

/**
 * @since 1.14
 *
 */
class SingleSocketTcpLogic extends AbstractTcpReader {

	private static final int INT_BYTES = AbstractMonitoringRecord.TYPE_SIZE_INT;
	private static final Charset ENCODING = StandardCharsets.UTF_8;

	private final ReaderRegistry<String> readerRegistry = new ReaderRegistry<>();
	private final RecordDeserializer recordDeserializer;

	public SingleSocketTcpLogic(final int port, final int bufferCapacity, final Logger logger,
			final IRecordReceivedListener listener) {
		super(port, bufferCapacity, logger);

		final GetValueAdapter<String> stringRegistryWrapper = new GetValueAdapter<>(this.readerRegistry);
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
			return this.registerRegistryEntry(buffer);
		} else {
			return this.recordDeserializer.deserializeRecord(clazzId, buffer);
		}
	}

	private boolean registerRegistryEntry(final ByteBuffer buffer) {
		// identify string identifier and string length
		if (buffer.remaining() < (INT_BYTES + INT_BYTES)) {
			return false;
		}

		final int stringLength = buffer.getInt();

		if (buffer.remaining() < stringLength) {
			return false;
		}

		final int id = buffer.getInt();

		final byte[] strBytes = new byte[stringLength];
		buffer.get(strBytes);
		final String string = new String(strBytes, ENCODING);

		this.readerRegistry.register(id, string);
		return true;
	}

}
