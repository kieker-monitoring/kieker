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
public final class AfterOperationObjectEventFactory implements IRecordFactory<AfterOperationObjectEvent> {
	
	@Override
	public AfterOperationObjectEvent create(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new AfterOperationObjectEvent(deserializer, buffer, stringRegistry);
	}
	
	@Override
	public AfterOperationObjectEvent create(final Object[] values) {
		return new AfterOperationObjectEvent(values);
	}
	
	@Override
	public int getRecordSizeInBytes() {
		return AfterOperationObjectEvent.SIZE;
	}
}
