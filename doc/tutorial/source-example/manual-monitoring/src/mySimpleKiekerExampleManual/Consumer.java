package mySimpleKiekerExampleManual;

import java.util.ArrayList;
import java.util.Collection;

import kieker.analysis.plugin.IMonitoringRecordConsumerPlugin;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.OperationExecutionRecord;

public class Consumer implements IMonitoringRecordConsumerPlugin {

	private long maxResponseTime;

	public Consumer(long maxResponseTime) {
		this.maxResponseTime = maxResponseTime;
	}

	@Override
	public Collection<Class<? extends IMonitoringRecord>> getRecordTypeSubscriptionList() {
		/* We want only receive records of one type */
		ArrayList<Class<? extends IMonitoringRecord>> result = new ArrayList<Class<? extends IMonitoringRecord>>();
		result.add(OperationExecutionRecord.class);
		return result;
	}

	@Override
	public boolean newMonitoringRecord(IMonitoringRecord arg0) {
		OperationExecutionRecord rec = (OperationExecutionRecord) arg0;
		/* Get the response time from the record. */
		long time = rec.tout - rec.tin;
		/* Now check with the maximal allowed response time. */
		if (time > maxResponseTime) {
			System.err.println("maximal response time exceeded by "
					+ (time - maxResponseTime) + " ms: " + rec.componentName
					+ "." + rec.opname);
		} else {
			System.out.println("response time accepted: " + rec.componentName
					+ "." + rec.opname);
		}
		return true;
	}

	@Override
	public boolean execute() {
		return true;
	}

	@Override
	public void terminate(boolean arg0) {
	}

}
