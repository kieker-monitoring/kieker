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
public final class AfterConstructorFailedObjectEventFactory implements IRecordFactory<AfterConstructorFailedObjectEvent> {
	
	@Override
	public AfterConstructorFailedObjectEvent create(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new AfterConstructorFailedObjectEvent(deserializer, buffer, stringRegistry);
	}
	
	@Override
	public AfterConstructorFailedObjectEvent create(final Object[] values) {
		return new AfterConstructorFailedObjectEvent(values);
	}
	
	@Override
	public int getRecordSizeInBytes() {
		return AfterConstructorFailedObjectEvent.SIZE;
	}
}
