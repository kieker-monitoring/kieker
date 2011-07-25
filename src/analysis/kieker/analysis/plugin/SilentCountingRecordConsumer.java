package kieker.analysis.plugin;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

import kieker.common.record.IMonitoringRecord;

/**
 * 
 * @author Jan Waller
 */
public final class SilentCountingRecordConsumer implements IMonitoringRecordConsumerPlugin {

	private AtomicLong counter = new AtomicLong();

	public final Collection<Class<? extends IMonitoringRecord>> getRecordTypeSubscriptionList() {
		return null;
	}

	public final boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		counter.incrementAndGet();
		return true;
	}

	public final boolean execute() {
		return true;
	}

	public final void terminate(final boolean error) {
	}

	public final long getMessageCount() {
		return counter.get();
	}
}
