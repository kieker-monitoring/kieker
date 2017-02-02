package kieker.common.record.flow.trace.concurrency.monitor;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.8
 */
public final class MonitorRequestEventFactory implements IRecordFactory<MonitorRequestEvent> {
	
	@Override
	public MonitorRequestEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new MonitorRequestEvent(buffer, stringRegistry);
	}
	
	@Override
	public MonitorRequestEvent create(final Object[] values) {
		return new MonitorRequestEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return MonitorRequestEvent.SIZE;
	}
}
