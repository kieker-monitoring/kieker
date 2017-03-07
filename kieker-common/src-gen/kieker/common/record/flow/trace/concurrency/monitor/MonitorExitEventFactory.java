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
public final class MonitorExitEventFactory implements IRecordFactory<MonitorExitEvent> {
	
	@Override
	public MonitorExitEvent create(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new MonitorExitEvent(deserializer, buffer, stringRegistry);
	}
	
	@Override
	public MonitorExitEvent create(final Object[] values) {
		return new MonitorExitEvent(values);
	}
	
	@Override
	public int getRecordSizeInBytes() {
		return MonitorExitEvent.SIZE;
	}
}
