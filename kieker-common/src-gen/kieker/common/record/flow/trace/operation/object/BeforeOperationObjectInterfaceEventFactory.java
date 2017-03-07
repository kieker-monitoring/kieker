package kieker.common.record.flow.trace.operation.object;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.util.registry.IRegistry;

/**
 * @author Florian Fittkau
 * 
 * @since 1.10
 */
public final class BeforeOperationObjectInterfaceEventFactory implements IRecordFactory<BeforeOperationObjectInterfaceEvent> {
	
	@Override
	public BeforeOperationObjectInterfaceEvent create(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new BeforeOperationObjectInterfaceEvent(deserializer, buffer, stringRegistry);
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
