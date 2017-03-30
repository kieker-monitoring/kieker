package kieker.common.record.flow.trace.operation.constructor.object;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Jan Waller
 *
 * @since 1.6
 */
public final class AfterConstructorFailedObjectEventFactory implements IRecordFactory<AfterConstructorFailedObjectEvent> {

	@Override
	public AfterConstructorFailedObjectEvent create(final IValueDeserializer deserializer) {
		return new AfterConstructorFailedObjectEvent(deserializer);
	}

	@Override
	public AfterConstructorFailedObjectEvent create(final Object[] values) {
		return new AfterConstructorFailedObjectEvent(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return AfterConstructorFailedObjectEvent.SIZE;
	}
}
