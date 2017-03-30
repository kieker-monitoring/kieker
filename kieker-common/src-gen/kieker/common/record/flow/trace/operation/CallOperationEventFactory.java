package kieker.common.record.flow.trace.operation;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Andre van Hoorn, Holger Knoche, Jan Waller
 *
 * @since 1.5
 */
public final class CallOperationEventFactory implements IRecordFactory<CallOperationEvent> {

	@Override
	public CallOperationEvent create(final IValueDeserializer deserializer) {
		return new CallOperationEvent(deserializer);
	}

	@Override
	public CallOperationEvent create(final Object[] values) {
		return new CallOperationEvent(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return CallOperationEvent.SIZE;
	}
}
