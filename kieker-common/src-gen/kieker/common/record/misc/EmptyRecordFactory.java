package kieker.common.record.misc;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 0.95a
 */
public final class EmptyRecordFactory implements IRecordFactory<EmptyRecord> {
	
	@Override
	public EmptyRecord create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new EmptyRecord(buffer, stringRegistry);
	}
	
	@Override
	public EmptyRecord create(final Object[] values) {
		return new EmptyRecord(values);
	}
	
	public int getRecordSizeInBytes() {
		return EmptyRecord.SIZE;
	}
}
