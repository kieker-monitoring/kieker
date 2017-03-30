package kieker.common.record.flow.trace.operation;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Jan Waller
 *
 * @since 1.5
 */
public final class AfterOperationFailedEventFactory implements IRecordFactory<AfterOperationFailedEvent> {

	@Override
	public AfterOperationFailedEvent create(final IValueDeserializer deserializer) {
		return new AfterOperationFailedEvent(deserializer);
	}

	@Override
	public AfterOperationFailedEvent create(final Object[] values) {
		return new AfterOperationFailedEvent(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return AfterOperationFailedEvent.SIZE;
	}
}
