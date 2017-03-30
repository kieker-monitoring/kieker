package kieker.common.record.flow.trace.operation.constructor;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Jan Waller
 *
 * @since 1.6
 */
public final class AfterConstructorFailedEventFactory implements IRecordFactory<AfterConstructorFailedEvent> {

	@Override
	public AfterConstructorFailedEvent create(final IValueDeserializer deserializer) {
		return new AfterConstructorFailedEvent(deserializer);
	}

	@Override
	public AfterConstructorFailedEvent create(final Object[] values) {
		return new AfterConstructorFailedEvent(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return AfterConstructorFailedEvent.SIZE;
	}
}
