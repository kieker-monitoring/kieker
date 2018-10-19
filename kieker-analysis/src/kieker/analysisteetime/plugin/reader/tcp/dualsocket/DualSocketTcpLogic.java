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

package kieker.analysisteetime.plugin.reader.tcp.dualsocket;

import java.nio.ByteBuffer;

import org.slf4j.Logger;

import kieker.analysisteetime.plugin.reader.IRecordReceivedListener;
import kieker.analysisteetime.plugin.reader.RecordDeserializer;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.registry.reader.ReaderRegistry;

import teetime.framework.OutputPort;
import teetime.util.stage.io.network.AbstractTcpReader;

/**
 * @author Christian Wulf
 *
 * @since 1.14
 */
class DualSocketTcpLogic extends AbstractTcpReader implements IRecordReceivedListener {

	private final OutputPort<IMonitoringRecord> outputPort;
	private final RecordDeserializer recordDeserializer;

	public DualSocketTcpLogic(final int port, final int bufferCapacity, final Logger logger, final ReaderRegistry<String> stringRegistry,
			final OutputPort<IMonitoringRecord> outputPort) {
		super(port, bufferCapacity, logger);

		this.outputPort = outputPort;
		this.recordDeserializer = new RecordDeserializer(this, stringRegistry);
	}

	@Override
	protected boolean onBufferReceived(final ByteBuffer buffer) {
		// identify record class
		if (buffer.remaining() < AbstractMonitoringRecord.TYPE_SIZE_INT) {
			return false;
		}
		final int clazzId = buffer.getInt();

		return this.recordDeserializer.deserializeRecord(clazzId, buffer);
	}

	@Override
	public void onRecordReceived(final IMonitoringRecord record) {
		this.outputPort.send(record);
	}
}
