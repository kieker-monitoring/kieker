package kieker.common.record.controlflow;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Andre van Hoorn, Jan Waller
 *
 * @since 0.91
 */
public final class OperationExecutionRecordFactory implements IRecordFactory<OperationExecutionRecord> {

	@Override
	public OperationExecutionRecord create(final IValueDeserializer deserializer) {
		return new OperationExecutionRecord(deserializer);
	}

	@Override
	public OperationExecutionRecord create(final Object[] values) {
		return new OperationExecutionRecord(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return OperationExecutionRecord.SIZE;
	}
}
