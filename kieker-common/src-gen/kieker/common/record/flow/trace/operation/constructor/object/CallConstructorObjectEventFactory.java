package kieker.common.record.flow.trace.operation.constructor.object;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public final class CallConstructorObjectEventFactory implements IRecordFactory<CallConstructorObjectEvent> {
	
	@Override
	public CallConstructorObjectEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new CallConstructorObjectEvent(buffer, stringRegistry);
	}
	
	@Override
	public CallConstructorObjectEvent create(final Object[] values) {
		return new CallConstructorObjectEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return CallConstructorObjectEvent.SIZE;
	}
}
