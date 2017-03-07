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
public final class MonitorEntryEventFactory implements IRecordFactory<MonitorEntryEvent> {
	
	@Override
	public MonitorEntryEvent create(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new MonitorEntryEvent(deserializer, buffer, stringRegistry);
	}
	
	@Override
	public MonitorEntryEvent create(final Object[] values) {
		return new MonitorEntryEvent(values);
	}
	
	@Override
	public int getRecordSizeInBytes() {
		return MonitorEntryEvent.SIZE;
	}
}
