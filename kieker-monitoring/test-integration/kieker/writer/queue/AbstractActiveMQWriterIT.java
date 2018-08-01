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
import kieker.test.common.junit.AbstractKiekerTest;
import org.junit.Before;
import org.junit.ClassRule;
import pl.domzal.junit.docker.rule.DockerRule;
import pl.domzal.junit.docker.rule.WaitFor;

public abstract class AbstractActiveMQWriterIT extends AbstractKiekerTest {

    public static final IMonitoringRecord MOCKED_MONITORING_RECORD = new OperationExecutionRecord(NO_OPERATION_SIGNATURE,
            SESSION_ID, TRACE_ID, TIN, TOUT, HOSTNAME,
            EOI, ESS);

    @ClassRule
    public static DockerRule rule = DockerRule.builder()
            .imageName("webcenter/activemq:5.13.2")
            .expose("61616", "61616")
            .expose("61613", "61613")
            .expose("8161", "8161")
            .env("ACTIVEMQ_SERIALIZABLE_PACKAGES", "'*'")
            .env("ACTIVEMQ_STATIC_QUEUES", "kieker")
            .waitFor(WaitFor.logMessage("INFO success"))
            .build();

    @Before
    public void setUp() {
    }
}
