package kieker.common.record.flow.trace.concurrency;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Jan Waller
 *
 * @since 1.5
 */
public final class SplitEventFactory implements IRecordFactory<SplitEvent> {

	@Override
	public SplitEvent create(final IValueDeserializer deserializer) {
		return new SplitEvent(deserializer);
	}

	@Override
	public SplitEvent create(final Object[] values) {
		return new SplitEvent(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return SplitEvent.SIZE;
	}
}
