package kieker.common.record.system;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.util.registry.IRegistry;

/**
 * @author Teerat Pitakrat
 * 
 * @since 1.12
 */
public final class DiskUsageRecordFactory implements IRecordFactory<DiskUsageRecord> {
	
	@Override
	public DiskUsageRecord create(final IValueDeserializer deserializer,  final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new DiskUsageRecord(deserializer, buffer, stringRegistry);
	}
	
	@Override
	public DiskUsageRecord create(final Object[] values) {
		return new DiskUsageRecord(values);
	}
	
	@Override
	public int getRecordSizeInBytes() {
		return DiskUsageRecord.SIZE;
	}
}
