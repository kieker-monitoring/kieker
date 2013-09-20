package kieker.monitoring.writer;

import com.lmax.disruptor.EventFactory;

import kieker.common.record.IMonitoringRecord;

/**
 * WARNING: This is a mutable object which will be recycled by the RingBuffer.
 * You must take a copy of data it holds
 * before the framework recycles it.
 */
public final class MonitoringRecordDisruptorEvent {
	private IMonitoringRecord value;

	public final IMonitoringRecord getValue() {
		return this.value;
	}

	public void setValue(final IMonitoringRecord value) {
		this.value = value;
	}

	public final static EventFactory<MonitoringRecordDisruptorEvent> EVENT_FACTORY = new EventFactory<MonitoringRecordDisruptorEvent>() {
		public MonitoringRecordDisruptorEvent newInstance() {
			return new MonitoringRecordDisruptorEvent();
		}
	};
}
