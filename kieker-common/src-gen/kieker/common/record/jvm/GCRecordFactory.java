package kieker.common.record.jvm;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Nils Christian Ehmke
 *
 * @since 1.10
 */
public final class GCRecordFactory implements IRecordFactory<GCRecord> {

	@Override
	public GCRecord create(final IValueDeserializer deserializer) {
		return new GCRecord(deserializer);
	}

	@Override
	public GCRecord create(final Object[] values) {
		return new GCRecord(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return GCRecord.SIZE;
	}
}
