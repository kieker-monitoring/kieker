package kieker.common.record.flow.trace.operation;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.5
 */
public final class BeforeOperationEventFactory implements IRecordFactory<BeforeOperationEvent> {
	
	@Override
	public BeforeOperationEvent create(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new BeforeOperationEvent(deserializer, buffer, stringRegistry);
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
