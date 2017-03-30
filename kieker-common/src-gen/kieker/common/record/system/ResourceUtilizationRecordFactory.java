package kieker.common.record.system;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Andre van Hoorn, Jan Waller
 *
 * @since 1.3
 */
public final class ResourceUtilizationRecordFactory implements IRecordFactory<ResourceUtilizationRecord> {

	@Override
	public ResourceUtilizationRecord create(final IValueDeserializer deserializer) {
		return new ResourceUtilizationRecord(deserializer);
	}

	@Override
	public ResourceUtilizationRecord create(final Object[] values) {
		return new ResourceUtilizationRecord(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return ResourceUtilizationRecord.SIZE;
	}
}
