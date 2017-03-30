package kieker.common.record.flow.trace.operation.constructor.object;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Jan Waller
 *
 * @since 1.6
 */
public final class CallConstructorObjectEventFactory implements IRecordFactory<CallConstructorObjectEvent> {

	@Override
	public CallConstructorObjectEvent create(final IValueDeserializer deserializer) {
		return new CallConstructorObjectEvent(deserializer);
	}

	@Override
	public CallConstructorObjectEvent create(final Object[] values) {
		return new CallConstructorObjectEvent(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return CallConstructorObjectEvent.SIZE;
	}
}
