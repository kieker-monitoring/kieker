package kieker.common.record.flow.trace.operation.constructor.object;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public final class AfterConstructorObjectEventFactory implements IRecordFactory<AfterConstructorObjectEvent> {
	
	@Override
	public AfterConstructorObjectEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new AfterConstructorObjectEvent(buffer, stringRegistry);
	}
	
	@Override
	public AfterConstructorObjectEvent create(final Object[] values) {
		return new AfterConstructorObjectEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return AfterConstructorObjectEvent.SIZE;
	}
}
