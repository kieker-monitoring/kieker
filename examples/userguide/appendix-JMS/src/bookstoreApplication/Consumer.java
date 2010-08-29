package bookstoreApplication;

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
		/* Derive response time from the record. */
		long responseTime = rec.tout - rec.tin;
		/* Now compare with the response responseTime threshold: */
		if (responseTime > maxResponseTime) {
			System.err.println("maximum response time exceeded by "
					+ (responseTime - maxResponseTime) + " ns: " + rec.className
					+ "." + rec.operationName);
		} else {
			System.out.println("response time accepted: " + rec.className
					+ "." + rec.operationName);
		}
		return true;
	}

	@Override
	public boolean execute() { return true;	}

	@Override
	public void terminate(boolean error) {	}

}
