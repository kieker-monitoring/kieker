package kieker.common.record.flow.trace.operation.constructor.object;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.util.registry.IRegistry;

/**
 * @author Florian Fittkau
 * 
 * @since 1.10
 */
public final class BeforeConstructorObjectInterfaceEventFactory implements IRecordFactory<BeforeConstructorObjectInterfaceEvent> {
	
	@Override
	public BeforeConstructorObjectInterfaceEvent create(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new BeforeConstructorObjectInterfaceEvent(deserializer, buffer, stringRegistry);
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
