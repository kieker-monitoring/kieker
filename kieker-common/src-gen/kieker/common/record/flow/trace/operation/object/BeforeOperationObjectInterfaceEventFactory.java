package kieker.common.record.flow.trace.operation.object;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Florian Fittkau
 *
 * @since 1.10
 */
public final class BeforeOperationObjectInterfaceEventFactory implements IRecordFactory<BeforeOperationObjectInterfaceEvent> {

	@Override
	public BeforeOperationObjectInterfaceEvent create(final IValueDeserializer deserializer) {
		return new BeforeOperationObjectInterfaceEvent(deserializer);
	}

	@Override
	public BeforeOperationObjectInterfaceEvent create(final Object[] values) {
		return new BeforeOperationObjectInterfaceEvent(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return BeforeOperationObjectInterfaceEvent.SIZE;
	}
}
