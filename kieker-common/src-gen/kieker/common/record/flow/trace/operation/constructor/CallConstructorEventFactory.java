package kieker.common.record.flow.trace.operation.constructor;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Jan Waller
 *
 * @since 1.6
 */
public final class CallConstructorEventFactory implements IRecordFactory<CallConstructorEvent> {

	@Override
	public CallConstructorEvent create(final IValueDeserializer deserializer) {
		return new CallConstructorEvent(deserializer);
	}

	@Override
	public CallConstructorEvent create(final Object[] values) {
		return new CallConstructorEvent(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return CallConstructorEvent.SIZE;
	}
}
