package kieker.analysisteetime.plugin.reader.tcp;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Ignore;
import org.junit.Test;

import kieker.analysisteetime.plugin.reader.tcp.TcpReaderStage;
import kieker.common.record.IMonitoringRecord;

import teetime.framework.test.StageTester;

public class TcpReaderStageTest {

	@Test
	@Ignore
	public void testNonStringRecordTransmission() throws Exception {
		final List<IMonitoringRecord> receivedRecords = new ArrayList<IMonitoringRecord>();

		final TcpReaderStage stage = new TcpReaderStage();
		StageTester.test(stage).and()
				.receive(receivedRecords).from(stage.getOutputPort())
				.start();

		final List<IMonitoringRecord> sentRecords = new ArrayList<IMonitoringRecord>();
		// TODO

		MatcherAssert.assertThat(receivedRecords, Is.is(IsEqual.equalTo(sentRecords)));
	}
}
