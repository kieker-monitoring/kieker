package kieker.common.record.misc;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Andre van Hoorn, Jan Waller
 *
 * @since 1.5
 */
public final class TimestampRecordFactory implements IRecordFactory<TimestampRecord> {

	@Override
	public TimestampRecord create(final IValueDeserializer deserializer) {
		return new TimestampRecord(deserializer);
	}

	@Override
	public TimestampRecord create(final Object[] values) {
		return new TimestampRecord(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return TimestampRecord.SIZE;
	}
}
