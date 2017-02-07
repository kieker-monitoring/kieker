package kieker.common.record.system;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Teerat Pitakrat
 * 
 * @since 1.12
 */
public final class DiskUsageRecordFactory implements IRecordFactory<DiskUsageRecord> {
	
	@Override
	public DiskUsageRecord create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new DiskUsageRecord(buffer, stringRegistry);
	}
	
	@Override
	public DiskUsageRecord create(final Object[] values) {
		return new DiskUsageRecord(values);
	}
	
	public int getRecordSizeInBytes() {
		return DiskUsageRecord.SIZE;
	}
}
