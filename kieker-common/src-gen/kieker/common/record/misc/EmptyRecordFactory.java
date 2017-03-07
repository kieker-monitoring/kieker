package kieker.common.record.misc;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.util.registry.IRegistry;

/**
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 0.95a
 */
public final class EmptyRecordFactory implements IRecordFactory<EmptyRecord> {
	
	@Override
	public EmptyRecord create(final IValueDeserializer deserializer,  final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new EmptyRecord(deserializer, buffer, stringRegistry);
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
