package kieker.common.record.flow.trace.concurrency.monitor;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.8
 */
public final class MonitorNotifyAllEventFactory implements IRecordFactory<MonitorNotifyAllEvent> {
	
	@Override
	public MonitorNotifyAllEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new MonitorNotifyAllEvent(buffer, stringRegistry);
	}
	
	@Override
	public MonitorNotifyAllEvent create(final Object[] values) {
		return new MonitorNotifyAllEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return MonitorNotifyAllEvent.SIZE;
	}
}
