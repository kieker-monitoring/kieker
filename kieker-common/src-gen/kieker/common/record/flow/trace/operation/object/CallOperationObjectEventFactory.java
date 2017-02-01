package kieker.common.record.flow.trace.operation.object;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public final class CallOperationObjectEventFactory implements IRecordFactory<CallOperationObjectEvent> {
	
	@Override
	public CallOperationObjectEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new CallOperationObjectEvent(buffer, stringRegistry);
	}
	
	@Override
	public CallOperationObjectEvent create(final Object[] values) {
		return new CallOperationObjectEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return CallOperationObjectEvent.SIZE;
	}
}
