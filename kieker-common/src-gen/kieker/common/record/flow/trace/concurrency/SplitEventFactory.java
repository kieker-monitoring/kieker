package kieker.common.record.flow.trace.concurrency;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.5
 */
public final class SplitEventFactory implements IRecordFactory<SplitEvent> {
	
	@Override
	public SplitEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new SplitEvent(buffer, stringRegistry);
	}
	
	@Override
	public SplitEvent create(final Object[] values) {
		return new SplitEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return SplitEvent.SIZE;
	}
}
