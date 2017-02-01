package kieker.common.record.flow.trace.operation.object;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public final class AfterOperationFailedObjectEventFactory implements IRecordFactory<AfterOperationFailedObjectEvent> {
	
	@Override
	public AfterOperationFailedObjectEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new AfterOperationFailedObjectEvent(buffer, stringRegistry);
	}
	
	@Override
	public AfterOperationFailedObjectEvent create(final Object[] values) {
		return new AfterOperationFailedObjectEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return AfterOperationFailedObjectEvent.SIZE;
	}
}
