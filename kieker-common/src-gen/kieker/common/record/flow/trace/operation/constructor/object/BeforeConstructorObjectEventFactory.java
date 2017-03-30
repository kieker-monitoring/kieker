package kieker.common.record.flow.trace.operation.constructor.object;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Jan Waller
 *
 * @since 1.6
 */
public final class BeforeConstructorObjectEventFactory implements IRecordFactory<BeforeConstructorObjectEvent> {

	@Override
	public BeforeConstructorObjectEvent create(final IValueDeserializer deserializer) {
		return new BeforeConstructorObjectEvent(deserializer);
	}

	@Override
	public BeforeConstructorObjectEvent create(final Object[] values) {
		return new BeforeConstructorObjectEvent(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return BeforeConstructorObjectEvent.SIZE;
	}
}
