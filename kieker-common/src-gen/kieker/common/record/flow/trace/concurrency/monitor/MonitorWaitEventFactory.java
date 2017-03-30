package kieker.common.record.flow.trace.concurrency.monitor;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Jan Waller
 *
 * @since 1.8
 */
public final class MonitorWaitEventFactory implements IRecordFactory<MonitorWaitEvent> {

	@Override
	public MonitorWaitEvent create(final IValueDeserializer deserializer) {
		return new MonitorWaitEvent(deserializer);
	}

	@Override
	public MonitorWaitEvent create(final Object[] values) {
		return new MonitorWaitEvent(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return MonitorWaitEvent.SIZE;
	}
}
