package kieker.common.record.flow.trace;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.5
 */
public final class ConstructionEventFactory implements IRecordFactory<ConstructionEvent> {
	
	@Override
	public ConstructionEvent create(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new ConstructionEvent(deserializer, buffer, stringRegistry);
	}
	
	@Override
	public ConstructionEvent create(final Object[] values) {
		return new ConstructionEvent(values);
	}
	
	@Override
	public int getRecordSizeInBytes() {
		return ConstructionEvent.SIZE;
	}
}
