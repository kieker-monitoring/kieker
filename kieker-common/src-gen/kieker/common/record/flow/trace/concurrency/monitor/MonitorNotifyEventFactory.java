package kieker.common.record.flow.trace.concurrency.monitor;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Jan Waller
 *
 * @since 1.8
 */
public final class MonitorNotifyEventFactory implements IRecordFactory<MonitorNotifyEvent> {

	@Override
	public MonitorNotifyEvent create(final IValueDeserializer deserializer) {
		return new MonitorNotifyEvent(deserializer);
	}

	@Override
	public MonitorNotifyEvent create(final Object[] values) {
		return new MonitorNotifyEvent(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return MonitorNotifyEvent.SIZE;
	}
}
