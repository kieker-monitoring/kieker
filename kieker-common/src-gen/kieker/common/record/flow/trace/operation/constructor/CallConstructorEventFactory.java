package kieker.common.record.flow.trace.operation.constructor;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public final class CallConstructorEventFactory implements IRecordFactory<CallConstructorEvent> {
	
	@Override
	public CallConstructorEvent create(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new CallConstructorEvent(deserializer, buffer, stringRegistry);
	}
	
	@Override
	public CallConstructorEvent create(final Object[] values) {
		return new CallConstructorEvent(values);
	}
	
	@Override
	public int getRecordSizeInBytes() {
		return CallConstructorEvent.SIZE;
	}
}
