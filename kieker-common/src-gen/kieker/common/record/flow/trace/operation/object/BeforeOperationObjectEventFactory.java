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
public final class BeforeOperationObjectEventFactory implements IRecordFactory<BeforeOperationObjectEvent> {
	
	@Override
	public BeforeOperationObjectEvent create(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new BeforeOperationObjectEvent(deserializer, buffer, stringRegistry);
	}
	
	@Override
	public BeforeOperationObjectEvent create(final Object[] values) {
		return new BeforeOperationObjectEvent(values);
	}
	
	@Override
	public int getRecordSizeInBytes() {
		return BeforeOperationObjectEvent.SIZE;
	}
}
