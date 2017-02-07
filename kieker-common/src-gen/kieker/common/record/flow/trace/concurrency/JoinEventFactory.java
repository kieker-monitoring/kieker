package kieker.common.record.flow.trace.concurrency;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.8
 */
public final class JoinEventFactory implements IRecordFactory<JoinEvent> {
	
	@Override
	public JoinEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new JoinEvent(buffer, stringRegistry);
	}
	
	@Override
	public JoinEvent create(final Object[] values) {
		return new JoinEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return JoinEvent.SIZE;
	}
}
