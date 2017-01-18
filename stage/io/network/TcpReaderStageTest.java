package teetime.stage.io.network;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import kieker.common.record.IMonitoringRecord;

import teetime.framework.test.StageTester;

public class TcpReaderStageTest {

	@Test
	@Ignore
	public void testNonStringRecordTransmission() throws Exception {
		List<IMonitoringRecord> receivedRecords = new ArrayList<IMonitoringRecord>();

		TcpReaderStage stage = new TcpReaderStage();
		StageTester.test(stage).and()
				.receive(receivedRecords).from(stage.getOutputPort())
				.start();

		List<IMonitoringRecord> sentRecords = new ArrayList<IMonitoringRecord>();
		// TODO

		assertThat(receivedRecords, is(equalTo(sentRecords)));
	}
}
