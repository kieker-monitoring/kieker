package kieker.common.record.flow.trace.operation.object;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Jan Waller
 *
 * @since 1.6
 */
public final class AfterOperationFailedObjectEventFactory implements IRecordFactory<AfterOperationFailedObjectEvent> {

	@Override
	public AfterOperationFailedObjectEvent create(final IValueDeserializer deserializer) {
		return new AfterOperationFailedObjectEvent(deserializer);
	}

	@Override
	public AfterOperationFailedObjectEvent create(final Object[] values) {
		return new AfterOperationFailedObjectEvent(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return AfterOperationFailedObjectEvent.SIZE;
	}
}
