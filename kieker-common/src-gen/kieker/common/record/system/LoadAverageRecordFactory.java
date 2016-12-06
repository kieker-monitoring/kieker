package kieker.common.record.system;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Teerat Pitakrat
 * 
 * @since 1.12
 */
public final class LoadAverageRecordFactory implements IRecordFactory<LoadAverageRecord> {
	
	@Override
	public LoadAverageRecord create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new LoadAverageRecord(buffer, stringRegistry);
	}
	
	@Override
	public LoadAverageRecord create(final Object[] values) {
		return new LoadAverageRecord(values);
	}
	
	public int getRecordSizeInBytes() {
		return LoadAverageRecord.SIZE;
	}
}
