package teetime.stage.io.network;

import kieker.common.record.IMonitoringRecord;

import teetime.framework.Configuration;
import teetime.framework.Execution;
import teetime.stage.io.Printer;

public class TcpReaderConfig extends Configuration {

	public TcpReaderConfig() {
		TcpReaderStage tcpReaderStage = new TcpReaderStage();
		Printer<IMonitoringRecord> printer = new Printer<IMonitoringRecord>();

		connectPorts(tcpReaderStage.getOutputPort(), printer.getInputPort());
	}

	public static void main(final String[] args) {
		TcpReaderConfig tcpReaderConfig = new TcpReaderConfig();
		Execution<Configuration> execution = new Execution<Configuration>(tcpReaderConfig);
		execution.executeBlocking();
	}
}
