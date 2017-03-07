package kieker.common.record.flow.trace.operation.object;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public final class AfterOperationFailedObjectEventFactory implements IRecordFactory<AfterOperationFailedObjectEvent> {
	
	@Override
	public AfterOperationFailedObjectEvent create(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new AfterOperationFailedObjectEvent(deserializer, buffer, stringRegistry);
	}
	
	@Override
	public AfterOperationFailedObjectEvent create(final Object[] values) {
		return new AfterOperationFailedObjectEvent(values);
	}
	
	@Override
	public int getRecordSizeInBytes() {
		return AfterOperationFailedObjectEvent.SIZE;
	}
}
