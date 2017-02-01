package kieker.common.record.flow.trace.operation.constructor;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public final class AfterConstructorEventFactory implements IRecordFactory<AfterConstructorEvent> {
	
	@Override
	public AfterConstructorEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new AfterConstructorEvent(buffer, stringRegistry);
	}
	
	@Override
	public AfterConstructorEvent create(final Object[] values) {
		return new AfterConstructorEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return AfterConstructorEvent.SIZE;
	}
}
