package kieker.common.record.system;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.util.registry.IRegistry;

/**
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 1.3
 */
public final class CPUUtilizationRecordFactory implements IRecordFactory<CPUUtilizationRecord> {
	
	@Override
	public CPUUtilizationRecord create(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new CPUUtilizationRecord(deserializer, buffer, stringRegistry);
	}
	
	@Override
	public CPUUtilizationRecord create(final Object[] values) {
		return new CPUUtilizationRecord(values);
	}
	
	@Override
	public int getRecordSizeInBytes() {
		return CPUUtilizationRecord.SIZE;
	}
}
