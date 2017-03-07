package kieker.common.record.jvm;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.util.registry.IRegistry;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public final class MemoryRecordFactory implements IRecordFactory<MemoryRecord> {
	
	@Override
	public MemoryRecord create(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new MemoryRecord(deserializer, buffer, stringRegistry);
	}
	
	@Override
	public MemoryRecord create(final Object[] values) {
		return new MemoryRecord(values);
	}
	
	@Override
	public int getRecordSizeInBytes() {
		return MemoryRecord.SIZE;
	}
}
