package kieker.monitoring.queue.behavior;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.writer.AbstractMonitoringWriter;

public class BypassQueueBehavior implements InsertBehavior<IMonitoringRecord> {

	private final AbstractMonitoringWriter writer;
	
	public BypassQueueBehavior(final AbstractMonitoringWriter writer) {
		this.writer = writer;
	}
	
	@Override
	public boolean insert(final IMonitoringRecord element) {
		this.writer.writeMonitoringRecord(element);
		return true;
	}

}
