package kieker.writer.queue;

import static kieker.common.record.controlflow.OperationExecutionRecord.EOI;
import static kieker.common.record.controlflow.OperationExecutionRecord.ESS;
import static kieker.common.record.controlflow.OperationExecutionRecord.HOSTNAME;
import static kieker.common.record.controlflow.OperationExecutionRecord.NO_OPERATION_SIGNATURE;
import static kieker.common.record.controlflow.OperationExecutionRecord.SESSION_ID;
import static kieker.common.record.controlflow.OperationExecutionRecord.TIN;
import static kieker.common.record.controlflow.OperationExecutionRecord.TOUT;
import static kieker.common.record.controlflow.OperationExecutionRecord.TRACE_ID;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import org.junit.Before;
import org.junit.ClassRule;
import pl.domzal.junit.docker.rule.DockerRule;

public abstract class AbstractRabbitMQWriterIT {

	private static int port = 66666;

	public static final IMonitoringRecord MOCKED_MONITORING_RECORD = new OperationExecutionRecord(NO_OPERATION_SIGNATURE,
			SESSION_ID, TRACE_ID, TIN, TOUT, HOSTNAME,
			EOI, ESS);

	@ClassRule
	public static DockerRule rule = DockerRule.builder()
			.imageName("rabbitmq:3.6-alpine")
			.expose("15672", "15672")
			.build();

	@Before
	public void setUp() {
	}
}
