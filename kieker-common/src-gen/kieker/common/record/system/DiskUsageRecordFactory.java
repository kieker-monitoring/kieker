package kieker.common.record.system;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Teerat Pitakrat
 *
 * @since 1.12
 */
public final class DiskUsageRecordFactory implements IRecordFactory<DiskUsageRecord> {

	@Override
	public DiskUsageRecord create(final IValueDeserializer deserializer) {
		return new DiskUsageRecord(deserializer);
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
