package kieker.common.record.flow.trace;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.5
 */
public final class ConstructionEventFactory implements IRecordFactory<ConstructionEvent> {
	
	@Override
	public ConstructionEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new ConstructionEvent(buffer, stringRegistry);
	}
	
	@Override
	public ConstructionEvent create(final Object[] values) {
		return new ConstructionEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return ConstructionEvent.SIZE;
	}
}
