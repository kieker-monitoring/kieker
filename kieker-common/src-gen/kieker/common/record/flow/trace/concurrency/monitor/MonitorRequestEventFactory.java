package kieker.common.record.flow.trace.concurrency.monitor;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Jan Waller
 *
 * @since 1.8
 */
public final class MonitorRequestEventFactory implements IRecordFactory<MonitorRequestEvent> {

	@Override
	public MonitorRequestEvent create(final IValueDeserializer deserializer) {
		return new MonitorRequestEvent(deserializer);
	}

	@Override
	public MonitorRequestEvent create(final Object[] values) {
		return new MonitorRequestEvent(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return MonitorRequestEvent.SIZE;
	}
}
