package kieker.common.record.flow.trace.operation.object;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Jan Waller
 *
 * @since 1.6
 */
public final class BeforeOperationObjectEventFactory implements IRecordFactory<BeforeOperationObjectEvent> {

	@Override
	public BeforeOperationObjectEvent create(final IValueDeserializer deserializer) {
		return new BeforeOperationObjectEvent(deserializer);
	}

	@Override
	public BeforeOperationObjectEvent create(final Object[] values) {
		return new BeforeOperationObjectEvent(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return BeforeOperationObjectEvent.SIZE;
	}
}
