package kieker.common.record.flow.trace.concurrency.monitor;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Jan Waller
 *
 * @since 1.8
 */
public final class MonitorExitEventFactory implements IRecordFactory<MonitorExitEvent> {

	@Override
	public MonitorExitEvent create(final IValueDeserializer deserializer) {
		return new MonitorExitEvent(deserializer);
	}

	@Override
	public MonitorExitEvent create(final Object[] values) {
		return new MonitorExitEvent(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return MonitorExitEvent.SIZE;
	}
}
