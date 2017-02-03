package kieker.common.record.flow.trace.operation.constructor;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public final class BeforeConstructorEventFactory implements IRecordFactory<BeforeConstructorEvent> {
	
	@Override
	public BeforeConstructorEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new BeforeConstructorEvent(buffer, stringRegistry);
	}
	
	@Override
	public BeforeConstructorEvent create(final Object[] values) {
		return new BeforeConstructorEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return BeforeConstructorEvent.SIZE;
	}
}
