package kieker.common.record.flow.trace.operation;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Jan Waller
 *
 * @since 1.5
 */
public final class BeforeOperationEventFactory implements IRecordFactory<BeforeOperationEvent> {

	@Override
	public BeforeOperationEvent create(final IValueDeserializer deserializer) {
		return new BeforeOperationEvent(deserializer);
	}

	@Override
	public BeforeOperationEvent create(final Object[] values) {
		return new BeforeOperationEvent(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return BeforeOperationEvent.SIZE;
	}
}
