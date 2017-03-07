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
public final class MonitorNotifyEventFactory implements IRecordFactory<MonitorNotifyEvent> {
	
	@Override
	public MonitorNotifyEvent create(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new MonitorNotifyEvent(deserializer, buffer, stringRegistry);
	}
	
	@Override
	public MonitorNotifyEvent create(final Object[] values) {
		return new MonitorNotifyEvent(values);
	}
	
	@Override
	public int getRecordSizeInBytes() {
		return MonitorNotifyEvent.SIZE;
	}
}
