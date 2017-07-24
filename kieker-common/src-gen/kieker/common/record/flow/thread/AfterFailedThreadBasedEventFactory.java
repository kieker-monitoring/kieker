package kieker.common.record.flow.thread;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Christian Wulf
 * 
 * @since 1.13
 */
public final class AfterFailedThreadBasedEventFactory implements IRecordFactory<AfterFailedThreadBasedEvent> {
	
	@Override
	public AfterFailedThreadBasedEvent create(final IValueDeserializer deserializer) {
		return new AfterFailedThreadBasedEvent(deserializer);
	}
	
	@Override
	public AfterFailedThreadBasedEvent create(final Object[] values) {
		return new AfterFailedThreadBasedEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return AfterFailedThreadBasedEvent.SIZE;
	}
}
