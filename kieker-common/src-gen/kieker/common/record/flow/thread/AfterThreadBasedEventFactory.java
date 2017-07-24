package kieker.common.record.flow.thread;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Christian Wulf
 * 
 * @since 1.13
 */
public final class AfterThreadBasedEventFactory implements IRecordFactory<AfterThreadBasedEvent> {

	@Override
	public AfterThreadBasedEvent create(final IValueDeserializer deserializer) {
		return new AfterThreadBasedEvent(deserializer);
	}

	@Override
	public AfterThreadBasedEvent create(final Object[] values) {
		return new AfterThreadBasedEvent(values);
	}

	public int getRecordSizeInBytes() {
		return AfterThreadBasedEvent.SIZE;
	}
}
