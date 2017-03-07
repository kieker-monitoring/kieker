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
public final class CallOperationObjectEventFactory implements IRecordFactory<CallOperationObjectEvent> {
	
	@Override
	public CallOperationObjectEvent create(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new CallOperationObjectEvent(deserializer, buffer, stringRegistry);
	}
	
	@Override
	public CallOperationObjectEvent create(final Object[] values) {
		return new CallOperationObjectEvent(values);
	}
	
	@Override
	public int getRecordSizeInBytes() {
		return CallOperationObjectEvent.SIZE;
	}
}
