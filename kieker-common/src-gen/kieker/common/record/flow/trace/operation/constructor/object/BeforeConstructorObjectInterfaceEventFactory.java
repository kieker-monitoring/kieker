package kieker.common.record.flow.trace.operation.constructor.object;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Florian Fittkau
 *
 * @since 1.10
 */
public final class BeforeConstructorObjectInterfaceEventFactory implements IRecordFactory<BeforeConstructorObjectInterfaceEvent> {

	@Override
	public BeforeConstructorObjectInterfaceEvent create(final IValueDeserializer deserializer) {
		return new BeforeConstructorObjectInterfaceEvent(deserializer);
	}

	@Override
	public BeforeConstructorObjectInterfaceEvent create(final Object[] values) {
		return new BeforeConstructorObjectInterfaceEvent(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return BeforeConstructorObjectInterfaceEvent.SIZE;
	}
}
