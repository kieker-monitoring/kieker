package kieker.common.record.flow.trace.operation.constructor.object;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Florian Fittkau
 * 
 * @since 1.10
 */
public final class BeforeConstructorObjectInterfaceEventFactory implements IRecordFactory<BeforeConstructorObjectInterfaceEvent> {
	
	@Override
	public BeforeConstructorObjectInterfaceEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new BeforeConstructorObjectInterfaceEvent(buffer, stringRegistry);
	}
	
	@Override
	public BeforeConstructorObjectInterfaceEvent create(final Object[] values) {
		return new BeforeConstructorObjectInterfaceEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return BeforeConstructorObjectInterfaceEvent.SIZE;
	}
}
