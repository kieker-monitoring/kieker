package kieker.common.record.flow.trace.operation.constructor.object;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Jan Waller
 *
 * @since 1.6
 */
public final class AfterConstructorObjectEventFactory implements IRecordFactory<AfterConstructorObjectEvent> {

	@Override
	public AfterConstructorObjectEvent create(final IValueDeserializer deserializer) {
		return new AfterConstructorObjectEvent(deserializer);
	}

	@Override
	public AfterConstructorObjectEvent create(final Object[] values) {
		return new AfterConstructorObjectEvent(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return AfterConstructorObjectEvent.SIZE;
	}
}
