package kieker.common.record.flow.thread;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Christian Wulf
 * 
 * @since 1.13
 */
public final class BeforeThreadBasedEventFactory implements IRecordFactory<BeforeThreadBasedEvent> {

	@Override
	public BeforeThreadBasedEvent create(final IValueDeserializer deserializer) {
		return new BeforeThreadBasedEvent(deserializer);
	}

	@Override
	public BeforeThreadBasedEvent create(final Object[] values) {
		return new BeforeThreadBasedEvent(values);
	}

	public int getRecordSizeInBytes() {
		return BeforeThreadBasedEvent.SIZE;
	}
}
