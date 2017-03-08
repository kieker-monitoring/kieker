package kieker.common.record.flow.trace.concurrency.monitor;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.8
 */
public final class MonitorEntryEventFactory implements IRecordFactory<MonitorEntryEvent> {
	
	@Override
	public MonitorEntryEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new MonitorEntryEvent(buffer, stringRegistry);
	}
	
	@Override
	public MonitorEntryEvent create(final Object[] values) {
		return new MonitorEntryEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return MonitorEntryEvent.SIZE;
	}
}
