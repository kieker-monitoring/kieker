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

package kieker.test.analysisteetime.junit.plugin.reader.tcp;

import kieker.analysisteetime.plugin.reader.tcp.dualsocket.DualSocketTcpReaderStage;
import kieker.common.record.IMonitoringRecord;

import teetime.framework.Configuration;
import teetime.framework.Execution;
import teetime.stage.io.Printer;

/**
 * @author Christian Wulf
 *
 * @since 1.14
 */
public class TcpReaderConfig extends Configuration { // NOPMD (class creates configuration)

	public TcpReaderConfig() {
		final DualSocketTcpReaderStage tcpReaderStage = new DualSocketTcpReaderStage();
		final Printer<IMonitoringRecord> printer = new Printer<>();

		this.connectPorts(tcpReaderStage.getOutputPort(), printer.getInputPort());
	}

	public static void main(final String[] args) {
		final TcpReaderConfig tcpReaderConfig = new TcpReaderConfig();
		final Execution<Configuration> execution = new Execution<>(tcpReaderConfig);
		execution.executeBlocking();
	}
}
