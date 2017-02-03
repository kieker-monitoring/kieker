package kieker.common.record.system;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 1.3
 */
public final class ResourceUtilizationRecordFactory implements IRecordFactory<ResourceUtilizationRecord> {
	
	@Override
	public ResourceUtilizationRecord create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new ResourceUtilizationRecord(buffer, stringRegistry);
	}
	
	@Override
	public ResourceUtilizationRecord create(final Object[] values) {
		return new ResourceUtilizationRecord(values);
	}
	
	public int getRecordSizeInBytes() {
		return ResourceUtilizationRecord.SIZE;
	}
}
