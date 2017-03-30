package kieker.common.record.flow.trace.operation.object;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Jan Waller
 *
 * @since 1.6
 */
public final class CallOperationObjectEventFactory implements IRecordFactory<CallOperationObjectEvent> {

	@Override
	public CallOperationObjectEvent create(final IValueDeserializer deserializer) {
		return new CallOperationObjectEvent(deserializer);
	}

	@Override
	public CallOperationObjectEvent create(final Object[] values) {
		return new CallOperationObjectEvent(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return CallOperationObjectEvent.SIZE;
	}
}
