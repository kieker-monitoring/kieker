package kieker.common.record.flow.trace.concurrency.monitor;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Jan Waller
 *
 * @since 1.8
 */
public final class MonitorEntryEventFactory implements IRecordFactory<MonitorEntryEvent> {

	@Override
	public MonitorEntryEvent create(final IValueDeserializer deserializer) {
		return new MonitorEntryEvent(deserializer);
	}

	@Override
	public MonitorEntryEvent create(final Object[] values) {
		return new MonitorEntryEvent(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return MonitorEntryEvent.SIZE;
	}
}
