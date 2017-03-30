package kieker.common.record.system;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Andre van Hoorn, Jan Waller
 *
 * @since 1.3
 */
public final class MemSwapUsageRecordFactory implements IRecordFactory<MemSwapUsageRecord> {

	@Override
	public MemSwapUsageRecord create(final IValueDeserializer deserializer) {
		return new MemSwapUsageRecord(deserializer);
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
