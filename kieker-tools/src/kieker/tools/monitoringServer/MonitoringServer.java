package kieker.tools.monitoringServer;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.TeeFilter;
import kieker.analysis.plugin.reader.jms.JMSReader;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.logReplayer.filter.MonitoringRecordLoggerFilter;
import kieker.tools.resourceMonitor.ResourceMonitor;

public class MonitoringServer {

	private static final Log LOG = LogFactory.getLog(ResourceMonitor.class);

	public static void main(final String[] args) {
		final IAnalysisController analysisController = new AnalysisController();
		final Configuration jmsReaderConfig = new Configuration();

		jmsReaderConfig.setProperty(JMSReader.CONFIG_PROPERTY_NAME_DESTINATION, "kieker");
		jmsReaderConfig.setProperty(JMSReader.CONFIG_PROPERTY_NAME_PROVIDERURL, "tcp://jmsserver:61616");
		jmsReaderConfig.setProperty(JMSReader.CONFIG_PROPERTY_NAME_FACTORYLOOKUP, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");

		final JMSReader jmsReader = new JMSReader(jmsReaderConfig, analysisController);
		final TeeFilter teeFilter = new TeeFilter(new Configuration(), analysisController);

		final MonitoringRecordLoggerFilter mrlf = new MonitoringRecordLoggerFilter(new Configuration(), analysisController);

		try {
			analysisController.connect(jmsReader, JMSReader.OUTPUT_PORT_NAME_RECORDS, teeFilter, TeeFilter.INPUT_PORT_NAME_EVENTS);
			analysisController.connect(teeFilter, TeeFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, mrlf, MonitoringRecordLoggerFilter.INPUT_PORT_NAME_RECORD);
			analysisController.run();
		} catch (final IllegalStateException e) {
			LOG.warn("An exception occurred", e);
		} catch (final AnalysisConfigurationException e) {
			LOG.warn("An exception occurred", e);
		}
	}
}
