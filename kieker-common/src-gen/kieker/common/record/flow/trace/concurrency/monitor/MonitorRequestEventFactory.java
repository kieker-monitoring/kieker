package kieker.common.record.flow.trace.concurrency.monitor;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.8
 */
public final class MonitorRequestEventFactory implements IRecordFactory<MonitorRequestEvent> {
	
	@Override
	public MonitorRequestEvent create(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new MonitorRequestEvent(deserializer, buffer, stringRegistry);
	}
	
	@Override
	public MonitorRequestEvent create(final Object[] values) {
		return new MonitorRequestEvent(values);
	}
	
	@Override
	public int getRecordSizeInBytes() {
		return MonitorRequestEvent.SIZE;
	}
}
