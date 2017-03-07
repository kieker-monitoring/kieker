package kieker.common.record.flow.trace.operation.constructor;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public final class BeforeConstructorEventFactory implements IRecordFactory<BeforeConstructorEvent> {
	
	@Override
	public BeforeConstructorEvent create(final IValueDeserializer deserializer,  final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new BeforeConstructorEvent(deserializer, buffer, stringRegistry);
	}
	
	@Override
	public BeforeConstructorEvent create(final Object[] values) {
		return new BeforeConstructorEvent(values);
	}
	
	@Override
	public int getRecordSizeInBytes() {
		return BeforeConstructorEvent.SIZE;
	}
}
