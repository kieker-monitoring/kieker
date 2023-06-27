package kieker.tools.log.replayer.stages;

import java.util.PriorityQueue;

import kieker.common.record.IMonitoringRecord;
import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

public class SorterStage extends AbstractConsumerStage<IMonitoringRecord> {

	private final OutputPort<IMonitoringRecord> outputPort = this.createOutputPort();

	private final PriorityQueue<IMonitoringRecord> sortedRecords = new PriorityQueue<>();
	private final int size;

	public SorterStage(int size) {
		this.size = size;
	}

	@Override
	protected void execute(final IMonitoringRecord element) throws Exception {
		synchronized (sortedRecords) {
			sortedRecords.add(element);

			if (sortedRecords.size() > size) {
				IMonitoringRecord firstRecord = sortedRecords.poll();
				outputPort.send(firstRecord);
			}
		}

	}

	@Override
	protected void onTerminating() {
		synchronized (sortedRecords) {
			while (!sortedRecords.isEmpty()) {
				IMonitoringRecord firstRecord = sortedRecords.poll();
				outputPort.send(firstRecord);
			}
		}
		super.onTerminating();
	}

	public OutputPort<IMonitoringRecord> getOutputPort() {
		return this.outputPort;
	}
}
