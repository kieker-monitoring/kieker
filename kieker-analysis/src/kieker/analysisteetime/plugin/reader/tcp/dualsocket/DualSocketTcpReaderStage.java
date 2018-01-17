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
package kieker.analysisteetime.plugin.reader.tcp.dualsocket;

import java.nio.ByteBuffer;

import kieker.analysisteetime.plugin.reader.tcp.singlesocket.SingleSocketTcpReaderStage;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.RegistryRecord;
import kieker.common.util.registry.ILookup;
import kieker.common.util.registry.Lookup;

import teetime.framework.AbstractProducerStage;
import teetime.util.stage.io.network.AbstractTcpReader;

/**
 * This is a reader which reads the records from two TCP ports.
 *
 * @author Jan Waller, Nils Christian Ehmke, Christian Wulf
 *
 * @deprecated Since 1.13. Use {@link SingleSocketTcpReaderStage} instead. This {@link DualSocketTcpReaderStage} will be
 *             kept for research purposes.
 */
@Deprecated
public final class DualSocketTcpReaderStage extends AbstractProducerStage<IMonitoringRecord> {

	private final ILookup<String> stringRegistry = new Lookup<String>();
	private final AbstractTcpReader tcpMonitoringRecordReader;
	private final AbstractTcpReader tcpStringRecordReader;

	private Thread tcpStringRecordReaderThread;

	/**
	 * Default constructor with <code>port1=10133</code>, <code>bufferCapacity=65535</code>, and
	 * <code>port2=10134</code>
	 */
	public DualSocketTcpReaderStage() {
		this(10133, 65535, 10134);
	}

	/**
	 *
	 * @param port1
	 *            used to accept <code>IMonitoringRecord</code>s
	 * @param port2
	 *            used to accept <code>StringRecord</code>s
	 * @param bufferCapacity
	 *            capacity of the receiving buffer
	 */
	public DualSocketTcpReaderStage(final int port1, final int bufferCapacity, final int port2) {
		super();

		this.tcpMonitoringRecordReader = new DualSocketTcpLogic(port1, bufferCapacity, this.logger, stringRegistry,
				this.outputPort);

		this.tcpStringRecordReader = new AbstractTcpReader(port2, bufferCapacity, this.logger) {
			@Override
			protected boolean onBufferReceived(final ByteBuffer buffer) {
				RegistryRecord.registerRecordInRegistry(buffer, stringRegistry);
				return true;
			}
		};
	}

	@Override
	public void onStarting() throws Exception {
		super.onStarting();
		this.tcpStringRecordReaderThread = new Thread(tcpStringRecordReader);
		this.tcpStringRecordReaderThread.start();
	}

	@Override
	protected void execute() {
		tcpMonitoringRecordReader.run();
		terminateStage();
	}

	@Override
	public void onTerminating() throws Exception {
		this.tcpStringRecordReader.terminate();
		this.tcpStringRecordReaderThread.interrupt();
		super.onTerminating();
	}

	public int getPort1() {
		return tcpMonitoringRecordReader.getPort();
	}

	public int getPort2() {
		return tcpStringRecordReader.getPort();
	}

}
