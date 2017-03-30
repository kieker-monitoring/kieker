package kieker.common.record.system;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Andre van Hoorn, Jan Waller
 *
 * @since 1.3
 */
public final class CPUUtilizationRecordFactory implements IRecordFactory<CPUUtilizationRecord> {

	@Override
	public CPUUtilizationRecord create(final IValueDeserializer deserializer) {
		return new CPUUtilizationRecord(deserializer);
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
