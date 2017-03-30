package kieker.common.record.flow.trace.operation.object;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Jan Waller
 *
 * @since 1.6
 */
public final class AfterOperationObjectEventFactory implements IRecordFactory<AfterOperationObjectEvent> {

	@Override
	public AfterOperationObjectEvent create(final IValueDeserializer deserializer) {
		return new AfterOperationObjectEvent(deserializer);
	}

	@Override
	public AfterOperationObjectEvent create(final Object[] values) {
		return new AfterOperationObjectEvent(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return AfterOperationObjectEvent.SIZE;
	}
}
