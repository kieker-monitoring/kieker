package kieker.common.record.flow.trace.operation;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Jan Waller
 *
 * @since 1.6
 */
public final class AfterOperationEventFactory implements IRecordFactory<AfterOperationEvent> {

	@Override
	public AfterOperationEvent create(final IValueDeserializer deserializer) {
		return new AfterOperationEvent(deserializer);
	}

	@Override
	public AfterOperationEvent create(final Object[] values) {
		return new AfterOperationEvent(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return AfterOperationEvent.SIZE;
	}
}
