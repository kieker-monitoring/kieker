package kieker.common.record.jvm;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Nils Christian Ehmke
 *
 * @since 1.10
 */
public final class UptimeRecordFactory implements IRecordFactory<UptimeRecord> {

	@Override
	public UptimeRecord create(final IValueDeserializer deserializer) {
		return new UptimeRecord(deserializer);
	}

	@Override
	public UptimeRecord create(final Object[] values) {
		return new UptimeRecord(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return UptimeRecord.SIZE;
	}
}
