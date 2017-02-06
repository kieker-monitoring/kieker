package kieker.common.record.flow.trace.operation;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.5
 */
public final class AfterOperationFailedEventFactory implements IRecordFactory<AfterOperationFailedEvent> {
	
	@Override
	public AfterOperationFailedEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new AfterOperationFailedEvent(buffer, stringRegistry);
	}
	
	@Override
	public AfterOperationFailedEvent create(final Object[] values) {
		return new AfterOperationFailedEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return AfterOperationFailedEvent.SIZE;
	}
}
