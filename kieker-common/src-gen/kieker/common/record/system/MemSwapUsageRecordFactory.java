package kieker.common.record.system;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.util.registry.IRegistry;

/**
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 1.3
 */
public final class MemSwapUsageRecordFactory implements IRecordFactory<MemSwapUsageRecord> {
	
	@Override
	public MemSwapUsageRecord create(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new MemSwapUsageRecord(deserializer, buffer, stringRegistry);
	}
	
	@Override
	public MemSwapUsageRecord create(final Object[] values) {
		return new MemSwapUsageRecord(values);
	}
	
	@Override
	public int getRecordSizeInBytes() {
		return MemSwapUsageRecord.SIZE;
	}
}
