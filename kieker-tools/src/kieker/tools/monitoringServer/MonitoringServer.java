package kieker.tools.monitoringServer;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.TeeFilter;
import kieker.analysis.plugin.reader.jms.JMSReader;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.monitoring.writer.AbstractAsyncWriter;
import kieker.monitoring.writer.filesystem.AsyncFsWriter;
import kieker.tools.resourceMonitor.ResourceMonitor;

public class MonitoringServer {

	private static final Log LOG = LogFactory.getLog(ResourceMonitor.class);

	public static void main(final String[] args) {
		final IAnalysisController analysisController = new AnalysisController();
		final Configuration jmsReaderConfig = new Configuration();
		jmsReaderConfig.setProperty(JMSReader.CONFIG_PROPERTY_NAME_DESTINATION, "kiekerLogQueue1");
		jmsReaderConfig.setProperty(JMSReader.CONFIG_PROPERTY_NAME_PROVIDERURL, "jmsServer");
		jmsReaderConfig.setProperty(JMSReader.OUTPUT_PORT_NAME_RECORDS, "61616");
		final JMSReader jmsReader = new JMSReader(jmsReaderConfig, analysisController);

		final TeeFilter teeFilter = new TeeFilter(new Configuration(), analysisController);

		final Configuration fsWriterConfig = new Configuration();
		fsWriterConfig.setProperty(AbstractAsyncWriter.CONFIG_SHUTDOWNDELAY, "1000");
		final AsyncFsWriter fsWriter = new AsyncFsWriter(fsWriterConfig);


		try {
			analysisController.connect(jmsReader, JMSReader.OUTPUT_PORT_NAME_RECORDS, teeFilter, TeeFilter.INPUT_PORT_NAME_EVENTS);
			//analysisController.connect(teeFilter, TeeFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, fsWriter, AsyncFsWriter.);
		} catch (final IllegalStateException e) {
			LOG.warn("An exception occurred", e);
		} catch (final AnalysisConfigurationException e) {
			LOG.warn("An exception occurred", e);
		}

	}

}
