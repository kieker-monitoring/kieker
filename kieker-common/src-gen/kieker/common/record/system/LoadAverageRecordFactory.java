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
public final class LoadAverageRecordFactory implements IRecordFactory<LoadAverageRecord> {
	
	@Override
	public LoadAverageRecord create(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new LoadAverageRecord(deserializer, buffer, stringRegistry);
	}
	
	@Override
	public LoadAverageRecord create(final Object[] values) {
		return new LoadAverageRecord(values);
	}
	
	@Override
	public int getRecordSizeInBytes() {
		return LoadAverageRecord.SIZE;
	}
}
