package kieker.common.record.flow.trace.operation;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Andre van Hoorn, Holger Knoche, Jan Waller
 * 
 * @since 1.5
 */
public final class CallOperationEventFactory implements IRecordFactory<CallOperationEvent> {
	
	@Override
	public CallOperationEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new CallOperationEvent(buffer, stringRegistry);
	}
	
	@Override
	public CallOperationEvent create(final Object[] values) {
		return new CallOperationEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return CallOperationEvent.SIZE;
	}
}
