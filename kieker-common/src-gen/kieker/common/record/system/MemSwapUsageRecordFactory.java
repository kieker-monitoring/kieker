package kieker.common.record.system;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 1.3
 */
public final class MemSwapUsageRecordFactory implements IRecordFactory<MemSwapUsageRecord> {
	
	@Override
	public MemSwapUsageRecord create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new MemSwapUsageRecord(buffer, stringRegistry);
	}
	
	@Override
	public MemSwapUsageRecord create(final Object[] values) {
		return new MemSwapUsageRecord(values);
	}
	
	public int getRecordSizeInBytes() {
		return MemSwapUsageRecord.SIZE;
	}
}
