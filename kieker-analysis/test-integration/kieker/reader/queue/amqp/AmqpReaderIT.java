package kieker.reader.queue.amqp;

import static org.assertj.core.api.Assertions.assertThat;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.CountingFilter;
import kieker.analysis.plugin.reader.amqp.AmqpReader;
import kieker.common.configuration.Configuration;
import kieker.reader.queue.AbstractRabbitMQReaderIT;
import org.junit.Before;
import org.junit.Test;

public class AmqpReaderIT extends AbstractRabbitMQReaderIT {

	AmqpReader reader;

	CountingFilter sink;

	@Before
	public void setUp() throws AnalysisConfigurationException {
		super.setUp();

		final IAnalysisController analysisController = new AnalysisController();

		reader = new AmqpReader(this.configuration, analysisController);

		sink = new CountingFilter(new Configuration(), analysisController);

		analysisController.connect(reader, AmqpReader.OUTPUT_PORT_NAME_RECORDS, sink, CountingFilter.INPUT_PORT_NAME_EVENTS);
		analysisController.run();
	}

	@Test
	public void testRead() {
		writer.writeMonitoringRecord(MOCKED_MONITORING_RECORD);

		assertThat(reader.read()).isEqualTo(true);
		assertThat(sink.getMessageCount()).isEqualTo(1L);
	}
}
