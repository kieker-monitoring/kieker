package kieker.common.record.flow.trace.operation.object;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public final class BeforeOperationObjectEventFactory implements IRecordFactory<BeforeOperationObjectEvent> {
	
	@Override
	public BeforeOperationObjectEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new BeforeOperationObjectEvent(buffer, stringRegistry);
	}
	
	@Override
	public BeforeOperationObjectEvent create(final Object[] values) {
		return new BeforeOperationObjectEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return BeforeOperationObjectEvent.SIZE;
	}
}
