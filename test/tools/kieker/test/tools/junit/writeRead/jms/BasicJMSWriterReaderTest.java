package kieker.test.tools.junit.writeRead.jms;

import java.util.List;

import org.apache.activemq.broker.BrokerService;
import org.junit.Assert;

import kieker.analysis.AnalysisController;
import kieker.analysis.AnalysisControllerThread;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.jms.JMSReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writer.jms.AsyncJMSWriter;

import kieker.test.tools.junit.writeRead.AbstractWriterReaderTest;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.8
 */
public class BasicJMSWriterReaderTest extends AbstractWriterReaderTest { // NOPMD NOCS (TestClassWithoutTestCases)

	private static final String PROVIDERURL = "tcp://localhost:61616";
	private static final String QUEUE = "queue";
	private static final String FACTORYLOOKUP = "org.apache.activemq.jndi.ActiveMQInitialContextFactory";

	private volatile ListCollectionFilter<IMonitoringRecord> sinkFilter = null; // NOPMD (init for findbugs)

	private void startEmbeddedJMSBroker() throws Exception {
		final BrokerService broker = new BrokerService();

		broker.addConnector(PROVIDERURL);

		broker.start();
	}

	@Override
	protected IMonitoringController createController(final int numRecordsWritten) throws Exception {
		this.startEmbeddedJMSBroker();
		final AnalysisController analysisController = new AnalysisController();

		final Configuration config = ConfigurationFactory.createDefaultConfiguration();
		config.setProperty(ConfigurationFactory.WRITER_CLASSNAME, AsyncJMSWriter.class.getName());
		config.setProperty(AsyncJMSWriter.CONFIG_PROVIDERURL, PROVIDERURL);
		config.setProperty(AsyncJMSWriter.CONFIG_TOPIC, QUEUE);
		config.setProperty(AsyncJMSWriter.CONFIG_CONTEXTFACTORYTYPE, FACTORYLOOKUP);
		final IMonitoringController ctrl = MonitoringController.createInstance(config);
		Thread.sleep(1000);
		final Configuration jmsReaderConfig = new Configuration();
		jmsReaderConfig.setProperty(JMSReader.CONFIG_PROPERTY_NAME_PROVIDERURL, PROVIDERURL);
		jmsReaderConfig.setProperty(JMSReader.CONFIG_PROPERTY_NAME_DESTINATION, QUEUE);
		jmsReaderConfig.setProperty(JMSReader.CONFIG_PROPERTY_NAME_FACTORYLOOKUP, FACTORYLOOKUP);

		final JMSReader jmsReader = new JMSReader(jmsReaderConfig, analysisController);
		this.sinkFilter = new ListCollectionFilter<IMonitoringRecord>(new Configuration(), analysisController);

		analysisController.connect(jmsReader, JMSReader.OUTPUT_PORT_NAME_RECORDS, this.sinkFilter, ListCollectionFilter.INPUT_PORT_NAME);
		final AnalysisControllerThread analysisThread = new AnalysisControllerThread(analysisController);
		analysisThread.start();
		Thread.sleep(1000);
		return ctrl;
	}

	@Override
	protected void checkControllerStateAfterRecordsPassedToController(final IMonitoringController monitoringController) throws Exception {
		Assert.assertTrue(monitoringController.isMonitoringEnabled());
	}

	@Override
	protected void checkControllerStateBeforeRecordsPassedToController(final IMonitoringController monitoringController) throws Exception {
		Assert.assertTrue(monitoringController.isMonitoringEnabled());
	}

	@Override
	protected void inspectRecords(final List<IMonitoringRecord> eventsPassedToController, final List<IMonitoringRecord> eventFromMonitoringLog) throws Exception {
		Assert.assertEquals("Unexpected set of records", eventsPassedToController, eventFromMonitoringLog);
	}

	@Override
	protected boolean terminateBeforeLogInspection() {
		return false;
	}

	@Override
	protected List<IMonitoringRecord> readEvents() throws Exception {
		return this.sinkFilter.getList();
	}

}
