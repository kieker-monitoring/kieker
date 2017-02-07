package kieker.common.record.flow.trace.operation.object;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public final class AfterOperationObjectEventFactory implements IRecordFactory<AfterOperationObjectEvent> {
	
	@Override
	public AfterOperationObjectEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new AfterOperationObjectEvent(buffer, stringRegistry);
	}
	
	@Override
	public AfterOperationObjectEvent create(final Object[] values) {
		return new AfterOperationObjectEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return AfterOperationObjectEvent.SIZE;
	}
}
