package kieker.common.record.flow.trace.operation.constructor.object;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public final class BeforeConstructorObjectEventFactory implements IRecordFactory<BeforeConstructorObjectEvent> {
	
	@Override
	public BeforeConstructorObjectEvent create(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new BeforeConstructorObjectEvent(deserializer, buffer, stringRegistry);
	}
	
	@Override
	public BeforeConstructorObjectEvent create(final Object[] values) {
		return new BeforeConstructorObjectEvent(values);
	}
	
	@Override
	public int getRecordSizeInBytes() {
		return BeforeConstructorObjectEvent.SIZE;
	}
}
