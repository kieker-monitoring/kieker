package kieker.common.record.jvm;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public final class ClassLoadingRecordFactory implements IRecordFactory<ClassLoadingRecord> {
	
	@Override
	public ClassLoadingRecord create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new ClassLoadingRecord(buffer, stringRegistry);
	}
	
	@Override
	public ClassLoadingRecord create(final Object[] values) {
		return new ClassLoadingRecord(values);
	}
	
	public int getRecordSizeInBytes() {
		return ClassLoadingRecord.SIZE;
	}
}
