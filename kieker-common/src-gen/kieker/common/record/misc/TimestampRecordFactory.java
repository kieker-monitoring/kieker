package kieker.common.record.misc;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 1.5
 */
public final class TimestampRecordFactory implements IRecordFactory<TimestampRecord> {
	
	@Override
	public TimestampRecord create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new TimestampRecord(buffer, stringRegistry);
	}
	
	@Override
	public TimestampRecord create(final Object[] values) {
		return new TimestampRecord(values);
	}
	
	public int getRecordSizeInBytes() {
		return TimestampRecord.SIZE;
	}
}
