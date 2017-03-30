package kieker.common.record.system;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Teerat Pitakrat
 *
 * @since 1.12
 */
public final class LoadAverageRecordFactory implements IRecordFactory<LoadAverageRecord> {

	@Override
	public LoadAverageRecord create(final IValueDeserializer deserializer) {
		return new LoadAverageRecord(deserializer);
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
