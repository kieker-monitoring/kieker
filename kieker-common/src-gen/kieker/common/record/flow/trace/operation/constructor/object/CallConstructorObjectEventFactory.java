package kieker.common.record.flow.trace.operation.constructor.object;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public final class CallConstructorObjectEventFactory implements IRecordFactory<CallConstructorObjectEvent> {
	
	@Override
	public CallConstructorObjectEvent create(final IValueDeserializer deserializer,  final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new CallConstructorObjectEvent(deserializer, buffer, stringRegistry);
	}
	
	@Override
	public CallConstructorObjectEvent create(final Object[] values) {
		return new CallConstructorObjectEvent(values);
	}
	
	@Override
	public int getRecordSizeInBytes() {
		return CallConstructorObjectEvent.SIZE;
	}
}
