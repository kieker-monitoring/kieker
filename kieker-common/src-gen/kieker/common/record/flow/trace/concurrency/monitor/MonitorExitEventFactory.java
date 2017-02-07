package kieker.common.record.flow.trace.concurrency.monitor;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.8
 */
public final class MonitorExitEventFactory implements IRecordFactory<MonitorExitEvent> {
	
	@Override
	public MonitorExitEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new MonitorExitEvent(buffer, stringRegistry);
	}
	
	@Override
	public MonitorExitEvent create(final Object[] values) {
		return new MonitorExitEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return MonitorExitEvent.SIZE;
	}
}
