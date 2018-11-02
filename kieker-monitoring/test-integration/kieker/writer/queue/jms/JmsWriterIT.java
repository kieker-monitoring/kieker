package kieker.writer.queue.jms;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.writer.jms.JmsWriter;
import kieker.writer.queue.AbstractActiveMQWriterIT;
import org.junit.Before;
import org.junit.Test;

public class JmsWriterIT extends AbstractActiveMQWriterIT {

    JmsWriter writer;

    Configuration configuration;

    @Before
    public void setUp() throws InterruptedException {
        super.setUp();
        this.configuration = ConfigurationFactory.createDefaultConfiguration();
        this.configuration.setProperty(JmsWriter.CONFIG_PROVIDERURL, "tcp://" + System.getenv("HOST_IP") + ":61616");
        this.configuration.setProperty(JmsWriter.CONFIG_TOPIC, "kieker");

        this.writer = new JmsWriter(this.configuration);
    }

    @Test
    public void testWrite() {
        writer.writeMonitoringRecord(MOCKED_MONITORING_RECORD);
    }
}
