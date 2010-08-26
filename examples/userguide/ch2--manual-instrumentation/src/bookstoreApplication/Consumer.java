package bookstoreApplication;

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
		return null;
	}

	@Override
	public boolean newMonitoringRecord(IMonitoringRecord record) {
		if (!(record instanceof OperationExecutionRecord)) {
			return true;
		}
		OperationExecutionRecord rec = (OperationExecutionRecord) record;
		/* Get the response responseTime from the record. */
		long responseTime = rec.tout - rec.tin;
		/* Now check compare with the response responseTime threshold: */
		if (responseTime > maxResponseTime) {
			System.err.println("maximum response time exceeded by "
					+ (responseTime - maxResponseTime) + " ns: " + rec.componentName
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
