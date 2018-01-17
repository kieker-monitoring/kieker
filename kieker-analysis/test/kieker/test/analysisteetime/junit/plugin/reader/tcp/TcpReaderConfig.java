package kieker.test.analysisteetime.junit.plugin.reader.tcp;

import kieker.analysisteetime.plugin.reader.tcp.dualsocket.DualSocketTcpReaderStage;
import kieker.common.record.IMonitoringRecord;

import teetime.framework.Configuration;
import teetime.framework.Execution;
import teetime.stage.io.Printer;

public class TcpReaderConfig extends Configuration {

	public TcpReaderConfig() {
		final DualSocketTcpReaderStage tcpReaderStage = new DualSocketTcpReaderStage();
		final Printer<IMonitoringRecord> printer = new Printer<IMonitoringRecord>();

		this.connectPorts(tcpReaderStage.getOutputPort(), printer.getInputPort());
	}

	public static void main(final String[] args) {
		final TcpReaderConfig tcpReaderConfig = new TcpReaderConfig();
		final Execution<Configuration> execution = new Execution<Configuration>(tcpReaderConfig);
		execution.executeBlocking();
	}
}
