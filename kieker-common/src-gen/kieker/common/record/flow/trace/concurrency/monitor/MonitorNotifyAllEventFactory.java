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
public final class MonitorNotifyAllEventFactory implements IRecordFactory<MonitorNotifyAllEvent> {
	
	@Override
	public MonitorNotifyAllEvent create(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new MonitorNotifyAllEvent(deserializer, buffer, stringRegistry);
	}
	
	@Override
	public MonitorNotifyAllEvent create(final Object[] values) {
		return new MonitorNotifyAllEvent(values);
	}
	
	@Override
	public int getRecordSizeInBytes() {
		return MonitorNotifyAllEvent.SIZE;
	}
}
