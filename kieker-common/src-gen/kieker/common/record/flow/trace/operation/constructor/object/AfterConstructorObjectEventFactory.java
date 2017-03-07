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
public final class AfterConstructorObjectEventFactory implements IRecordFactory<AfterConstructorObjectEvent> {
	
	@Override
	public AfterConstructorObjectEvent create(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new AfterConstructorObjectEvent(deserializer, buffer, stringRegistry);
	}
	
	@Override
	public AfterConstructorObjectEvent create(final Object[] values) {
		return new AfterConstructorObjectEvent(values);
	}
	
	@Override
	public int getRecordSizeInBytes() {
		return AfterConstructorObjectEvent.SIZE;
	}
}
