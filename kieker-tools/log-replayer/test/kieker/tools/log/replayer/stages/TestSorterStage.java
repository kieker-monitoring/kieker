package kieker.tools.log.replayer.stages;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.analysis.generic.sink.DataSink;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import teetime.framework.AbstractConsumerStage;
import teetime.framework.pipe.BoundedSynchedPipe;
import teetime.framework.pipe.IPipe;

public class TestSorterStage {

	@Test
	public void testStage() throws Exception {
		SorterStage stage = addData();
		
		List<IMonitoringRecord> records = readRecords(stage);
		
		OperationExecutionRecord third = (OperationExecutionRecord) records.get(2);
		Assert.assertEquals("ClazzA.myTest3", third.getOperationSignature());
	}

	private List<IMonitoringRecord> readRecords(SorterStage stage) {
		List<IMonitoringRecord> records = new LinkedList<>();
		IPipe<IMonitoringRecord> pipe = (IPipe<IMonitoringRecord>) stage.getOutputPort().getPipe();
		System.out.println("Pipe has more: " + pipe.hasMore());
		while (pipe.hasMore()) {
			IMonitoringRecord record = (IMonitoringRecord) pipe.removeLast();
			records.add(record);
		}
		return records;
	}

	private SorterStage addData() throws Exception {
		SorterStage stage = new SorterStage(3);
		
		final AbstractConsumerStage<IMonitoringRecord> consumer = new DataSink(ConfigurationFactory.createDefaultConfiguration());
		new BoundedSynchedPipe(stage.getOutputPort(), consumer.getInputPort(), 100);
		
		long startTime = System.nanoTime();
		stage.execute(new OperationExecutionRecord("ClazzA.myTest1", "1", 1, startTime, startTime, null, 0, 0));
		stage.execute(new OperationExecutionRecord("ClazzA.myTest2", "1", 1, startTime + 1, startTime + 1, null, 0, 0));
		stage.execute(
				new OperationExecutionRecord("ClazzA.myTest5", "1", 1, startTime + 1000, startTime + 1000, null, 0, 0));
		stage.execute(new OperationExecutionRecord("ClazzA.myTest3", "1", 1, startTime + 3, startTime + 3, null, 0, 0));
		stage.execute(new OperationExecutionRecord("ClazzA.myTest4", "1", 1, startTime + 4, startTime + 4, null, 0, 0));

		stage.onTerminating();
		return stage;
	}
}
