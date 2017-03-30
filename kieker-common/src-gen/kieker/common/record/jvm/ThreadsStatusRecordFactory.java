package kieker.common.record.jvm;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Nils Christian Ehmke
 *
 * @since 1.10
 */
public final class ThreadsStatusRecordFactory implements IRecordFactory<ThreadsStatusRecord> {

	@Override
	public ThreadsStatusRecord create(final IValueDeserializer deserializer) {
		return new ThreadsStatusRecord(deserializer);
	}

	@Override
	public ThreadsStatusRecord create(final Object[] values) {
		return new ThreadsStatusRecord(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return ThreadsStatusRecord.SIZE;
	}
}
