package kieker.common.record.flow.trace.concurrency;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.8
 */
public final class JoinEventFactory implements IRecordFactory<JoinEvent> {
	
	@Override
	public JoinEvent create(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new JoinEvent(deserializer, buffer, stringRegistry);
	}
	
	@Override
	public JoinEvent create(final Object[] values) {
		return new JoinEvent(values);
	}
	
	@Override
	public int getRecordSizeInBytes() {
		return JoinEvent.SIZE;
	}
}
