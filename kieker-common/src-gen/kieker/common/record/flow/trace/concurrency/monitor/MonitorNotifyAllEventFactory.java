package kieker.common.record.flow.trace.concurrency.monitor;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Jan Waller
 *
 * @since 1.8
 */
public final class MonitorNotifyAllEventFactory implements IRecordFactory<MonitorNotifyAllEvent> {

	@Override
	public MonitorNotifyAllEvent create(final IValueDeserializer deserializer) {
		return new MonitorNotifyAllEvent(deserializer);
	}

	@Override
	public MonitorNotifyAllEvent create(final Object[] values) {
		return new MonitorNotifyAllEvent(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return MonitorNotifyAllEvent.SIZE;
	}
}
