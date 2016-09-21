package kieker.tools.monitoringServer;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.TeeFilter;
import kieker.analysis.plugin.reader.amqp.AMQPReader;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.logReplayer.filter.MonitoringRecordLoggerFilter;
import kieker.tools.resourceMonitor.ResourceMonitor;

public class MonitoringServer {

	private static final Log LOG = LogFactory.getLog(ResourceMonitor.class);

	public static void main(final String[] args) {
		final IAnalysisController analysisController = new AnalysisController();
		final Configuration amqpReaderConfig = new Configuration();

		amqpReaderConfig.setProperty(AMQPReader.CONFIG_PROPERTY_QUEUENAME, "kieker");
		amqpReaderConfig.setProperty(AMQPReader.CONFIG_PROPERTY_URI, "amqp://guest:guest@192.168.99.100:31111");

		final AMQPReader amqpReader = new AMQPReader(amqpReaderConfig, analysisController);
		final TeeFilter teeFilter = new TeeFilter(new Configuration(), analysisController);

		final MonitoringRecordLoggerFilter mrlf = new MonitoringRecordLoggerFilter(new Configuration(), analysisController);

		try {
			analysisController.connect(amqpReader, AMQPReader.OUTPUT_PORT_NAME_RECORDS, teeFilter, TeeFilter.INPUT_PORT_NAME_EVENTS);
			analysisController.connect(teeFilter, TeeFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, mrlf, MonitoringRecordLoggerFilter.INPUT_PORT_NAME_RECORD);
			analysisController.run();
		} catch (final IllegalStateException e) {
			LOG.warn("An exception occurred", e);
		} catch (final AnalysisConfigurationException e) {
			LOG.warn("An exception occurred", e);
		}
	}
}
