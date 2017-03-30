package kieker.common.record.jvm;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Nils Christian Ehmke
 *
 * @since 1.10
 */
public final class CompilationRecordFactory implements IRecordFactory<CompilationRecord> {

	@Override
	public CompilationRecord create(final IValueDeserializer deserializer) {
		return new CompilationRecord(deserializer);
	}

	@Override
	public CompilationRecord create(final Object[] values) {
		return new CompilationRecord(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return CompilationRecord.SIZE;
	}
}
