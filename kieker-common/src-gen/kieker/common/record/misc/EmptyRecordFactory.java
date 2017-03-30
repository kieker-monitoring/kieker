package kieker.common.record.misc;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Andre van Hoorn, Jan Waller
 *
 * @since 0.95a
 */
public final class EmptyRecordFactory implements IRecordFactory<EmptyRecord> {

	@Override
	public EmptyRecord create(final IValueDeserializer deserializer) {
		return new EmptyRecord(deserializer);
	}

	@Override
	public EmptyRecord create(final Object[] values) {
		return new EmptyRecord(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return EmptyRecord.SIZE;
	}
}
