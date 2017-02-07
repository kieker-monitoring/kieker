package kieker.common.record.system;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 1.3
 */
public final class CPUUtilizationRecordFactory implements IRecordFactory<CPUUtilizationRecord> {
	
	@Override
	public CPUUtilizationRecord create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new CPUUtilizationRecord(buffer, stringRegistry);
	}
	
	@Override
	public CPUUtilizationRecord create(final Object[] values) {
		return new CPUUtilizationRecord(values);
	}
	
	public int getRecordSizeInBytes() {
		return CPUUtilizationRecord.SIZE;
	}
}
