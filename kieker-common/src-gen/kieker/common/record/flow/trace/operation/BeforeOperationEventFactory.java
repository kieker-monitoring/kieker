package kieker.common.record.flow.trace.operation;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.5
 */
public final class BeforeOperationEventFactory implements IRecordFactory<BeforeOperationEvent> {
	
	@Override
	public BeforeOperationEvent create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new BeforeOperationEvent(buffer, stringRegistry);
	}
	
	@Override
	public BeforeOperationEvent create(final Object[] values) {
		return new BeforeOperationEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return BeforeOperationEvent.SIZE;
	}
}
