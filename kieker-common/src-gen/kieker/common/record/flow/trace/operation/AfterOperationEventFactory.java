package kieker.common.record.flow.trace.operation;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public final class AfterOperationEventFactory implements IRecordFactory<AfterOperationEvent> {
	
	@Override
	public AfterOperationEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new AfterOperationEvent(buffer, stringRegistry);
	}
	
	@Override
	public AfterOperationEvent create(final Object[] values) {
		return new AfterOperationEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return AfterOperationEvent.SIZE;
	}
}
