package kieker.writer.queue.amqp;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;
import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.writer.amqp.AmqpWriter;
import kieker.writer.queue.AbstractRabbitMQWriterIT;
import org.junit.Before;
import org.junit.Test;

public class AmqpWriterIT extends AbstractRabbitMQWriterIT {

	AmqpWriter writer;

	Configuration configuration;

	@Before
	public void setUp() {
		super.setUp();
		this.configuration = ConfigurationFactory.createConfigurationFromFile(System.getProperty("user.dir") + "/test-resources/META-INF/kieker.monitoring.integration.amqp.properties");
		try {
			this.writer = new AmqpWriter(this.configuration);
		} catch (KeyManagementException | NoSuchAlgorithmException | IOException | URISyntaxException | TimeoutException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testWrite() {
		writer.writeMonitoringRecord(MOCKED_MONITORING_RECORD);
	}
}
