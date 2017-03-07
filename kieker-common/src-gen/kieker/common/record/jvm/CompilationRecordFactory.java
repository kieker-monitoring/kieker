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
public final class CompilationRecordFactory implements IRecordFactory<CompilationRecord> {
	
	@Override
	public CompilationRecord create(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new CompilationRecord(deserializer, buffer, stringRegistry);
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
