package kieker.common.record.controlflow;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 0.91
 */
public final class OperationExecutionRecordFactory implements IRecordFactory<OperationExecutionRecord> {
	
	@Override
	public OperationExecutionRecord create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new OperationExecutionRecord(buffer, stringRegistry);
	}
	
	@Override
	public OperationExecutionRecord create(final Object[] values) {
		return new OperationExecutionRecord(values);
	}
	
	public int getRecordSizeInBytes() {
		return OperationExecutionRecord.SIZE;
	}
}
