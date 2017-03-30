package kieker.common.record.jvm;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Nils Christian Ehmke
 *
 * @since 1.10
 */
public final class MemoryRecordFactory implements IRecordFactory<MemoryRecord> {

	@Override
	public MemoryRecord create(final IValueDeserializer deserializer) {
		return new MemoryRecord(deserializer);
	}

	@Override
	public MemoryRecord create(final Object[] values) {
		return new MemoryRecord(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return MemoryRecord.SIZE;
	}
}
