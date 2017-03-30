package kieker.common.record.controlflow;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Andre van Hoorn, Jan Waller
 *
 * @since 1.2
 */
public final class BranchingRecordFactory implements IRecordFactory<BranchingRecord> {

	@Override
	public BranchingRecord create(final IValueDeserializer deserializer) {
		return new BranchingRecord(deserializer);
	}

	@Override
	public BranchingRecord create(final Object[] values) {
		return new BranchingRecord(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return BranchingRecord.SIZE;
	}
}
