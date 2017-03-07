package kieker.common.record.controlflow;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.util.registry.IRegistry;

/**
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 0.91
 */
public final class OperationExecutionRecordFactory implements IRecordFactory<OperationExecutionRecord> {
	
	@Override
	public OperationExecutionRecord create(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new OperationExecutionRecord(deserializer, buffer, stringRegistry);
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
