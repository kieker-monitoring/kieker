package kieker.writer.queue;

import static kieker.common.record.controlflow.OperationExecutionRecord.EOI;
import static kieker.common.record.controlflow.OperationExecutionRecord.ESS;
import static kieker.common.record.controlflow.OperationExecutionRecord.HOSTNAME;
import static kieker.common.record.controlflow.OperationExecutionRecord.OPERATION_SIGNATURE;
import static kieker.common.record.controlflow.OperationExecutionRecord.SESSION_ID;
import static kieker.common.record.controlflow.OperationExecutionRecord.TIN;
import static kieker.common.record.controlflow.OperationExecutionRecord.TOUT;
import static kieker.common.record.controlflow.OperationExecutionRecord.TRACE_ID;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import org.junit.ClassRule;
import pl.domzal.junit.docker.rule.DockerRule;
import pl.domzal.junit.docker.rule.WaitFor;

public abstract class AbstractRabbitMQ {

    public static final IMonitoringRecord MOCKED_MONITORING_RECORD = new OperationExecutionRecord(OPERATION_SIGNATURE,
            SESSION_ID, TRACE_ID, TIN, TOUT, HOSTNAME,
            EOI, ESS);

    @ClassRule
    public static DockerRule rule = DockerRule.builder()
            .imageName("rabbitmq:3.6-alpine")
            .name("rabbitmq")
            .expose("5672", "5672")
            .waitFor(WaitFor.logMessage("Server startup complete"))
            .build();
}
