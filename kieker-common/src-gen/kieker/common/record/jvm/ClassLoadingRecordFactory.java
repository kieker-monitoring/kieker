package kieker.common.record.jvm;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Nils Christian Ehmke
 *
 * @since 1.10
 */
public final class ClassLoadingRecordFactory implements IRecordFactory<ClassLoadingRecord> {

	@Override
	public ClassLoadingRecord create(final IValueDeserializer deserializer) {
		return new ClassLoadingRecord(deserializer);
	}

	@Override
	public ClassLoadingRecord create(final Object[] values) {
		return new ClassLoadingRecord(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return ClassLoadingRecord.SIZE;
	}
}
