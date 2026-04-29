package kieker.tools.oteltransformer.receiver;

import kieker.analysis.generic.sink.DataSink;

import teetime.framework.Configuration;

public class OtlpReceiverStageConfiguration extends Configuration {
	public OtlpReceiverStageConfiguration(int port, kieker.common.configuration.Configuration configuration) {
		OtlpGrpcReceiverStage source = new OtlpGrpcReceiverStage(port);
		DataSink consumer = new DataSink(configuration);
		
		this.connectPorts(source.getOutputPort(), consumer.getInputPort());
	}
}
