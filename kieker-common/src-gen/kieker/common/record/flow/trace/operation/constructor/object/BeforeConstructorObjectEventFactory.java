package kieker.common.record.flow.trace.operation.constructor.object;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public final class BeforeConstructorObjectEventFactory implements IRecordFactory<BeforeConstructorObjectEvent> {
	
	@Override
	public BeforeConstructorObjectEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new BeforeConstructorObjectEvent(buffer, stringRegistry);
	}
	
	@Override
	public BeforeConstructorObjectEvent create(final Object[] values) {
		return new BeforeConstructorObjectEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return BeforeConstructorObjectEvent.SIZE;
	}
}
