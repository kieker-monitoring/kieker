package kieker.common.record.controlflow;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 1.2
 */
public final class BranchingRecordFactory implements IRecordFactory<BranchingRecord> {
	
	@Override
	public BranchingRecord create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new BranchingRecord(buffer, stringRegistry);
	}
	
	@Override
	public BranchingRecord create(final Object[] values) {
		return new BranchingRecord(values);
	}
	
	public int getRecordSizeInBytes() {
		return BranchingRecord.SIZE;
	}
}
