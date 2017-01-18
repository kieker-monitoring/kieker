/**
 * Copyright (C) 2015 Christian Wulf, Nelson Tavares de Sousa (http://teetime-framework.github.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package teetime.stage.io.network;

import kieker.common.record.IMonitoringRecord;

import teetime.framework.AbstractProducerStage;
import teetime.stage.io.network.util.IRecordReceivedListener;
import teetime.stage.io.network.util.SingleSocketRecordReader;
import teetime.util.io.network.AbstractTcpReader;

/**
 * This is a reader which reads the records from a TCP port.
 *
 * @author Christian Wulf
 *
 */
public final class SingleSocketTcpReaderStage extends AbstractProducerStage<IMonitoringRecord>implements IRecordReceivedListener {

	private final AbstractTcpReader tcpStreamReader;

	/**
	 * Default constructor with <code>port1=10133</code> and <code>bufferCapacity=65535</code></code>
	 */
	public SingleSocketTcpReaderStage() {
		this(10133, 65535);
	}

	/**
	 *
	 * @param port
	 *            used to accept <code>IMonitoringRecord</code>s and string registry entries.
	 * @param bufferCapacity
	 *            capacity of the receiving buffer
	 */
	public SingleSocketTcpReaderStage(final int port, final int bufferCapacity) {
		this.tcpStreamReader = new SingleSocketRecordReader(port, bufferCapacity, logger, this);
	}

	@Override
	protected void execute() {
		tcpStreamReader.run();
		terminateStage();
	}

	@Override
	public void onRecordReceived(final IMonitoringRecord record) {
		outputPort.send(record);
	}

	public int getPort() {
		return tcpStreamReader.getPort();
	}

}
