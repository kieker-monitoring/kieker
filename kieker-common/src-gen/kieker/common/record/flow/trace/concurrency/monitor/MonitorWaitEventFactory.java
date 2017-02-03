package kieker.common.record.flow.trace.concurrency.monitor;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.8
 */
public final class MonitorWaitEventFactory implements IRecordFactory<MonitorWaitEvent> {
	
	@Override
	public MonitorWaitEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new MonitorWaitEvent(buffer, stringRegistry);
	}
	
	@Override
	public MonitorWaitEvent create(final Object[] values) {
		return new MonitorWaitEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return MonitorWaitEvent.SIZE;
	}
}
