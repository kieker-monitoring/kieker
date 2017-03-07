package kieker.common.record.flow.trace.concurrency;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.5
 */
public final class SplitEventFactory implements IRecordFactory<SplitEvent> {
	
	@Override
	public SplitEvent create(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new SplitEvent(deserializer, buffer, stringRegistry);
	}
	
	@Override
	public SplitEvent create(final Object[] values) {
		return new SplitEvent(values);
	}
	
	@Override
	public int getRecordSizeInBytes() {
		return SplitEvent.SIZE;
	}
}
