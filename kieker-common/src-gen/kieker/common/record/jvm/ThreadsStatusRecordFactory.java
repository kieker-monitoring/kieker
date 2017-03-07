package kieker.common.record.jvm;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.util.registry.IRegistry;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public final class ThreadsStatusRecordFactory implements IRecordFactory<ThreadsStatusRecord> {
	
	@Override
	public ThreadsStatusRecord create(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new ThreadsStatusRecord(deserializer, buffer, stringRegistry);
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
