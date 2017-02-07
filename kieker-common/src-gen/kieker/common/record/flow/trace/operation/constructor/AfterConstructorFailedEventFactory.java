package kieker.common.record.flow.trace.operation.constructor;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public final class AfterConstructorFailedEventFactory implements IRecordFactory<AfterConstructorFailedEvent> {
	
	@Override
	public AfterConstructorFailedEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new AfterConstructorFailedEvent(buffer, stringRegistry);
	}
	
	@Override
	public AfterConstructorFailedEvent create(final Object[] values) {
		return new AfterConstructorFailedEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return AfterConstructorFailedEvent.SIZE;
	}
}
