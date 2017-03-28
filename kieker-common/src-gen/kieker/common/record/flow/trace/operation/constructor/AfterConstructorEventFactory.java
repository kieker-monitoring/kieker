package kieker.common.record.flow.trace.operation.constructor;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Jan Waller
 *
 * @since 1.6
 */
public final class AfterConstructorEventFactory implements IRecordFactory<AfterConstructorEvent> {

	@Override
	public AfterConstructorEvent create(final IValueDeserializer deserializer) {
		return new AfterConstructorEvent(deserializer);
	}

	@Override
	public AfterConstructorEvent create(final Object[] values) {
		return new AfterConstructorEvent(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return AfterConstructorEvent.SIZE;
	}
}
