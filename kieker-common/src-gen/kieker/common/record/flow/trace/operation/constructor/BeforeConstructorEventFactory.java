package kieker.common.record.flow.trace.operation.constructor;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Jan Waller
 *
 * @since 1.6
 */
public final class BeforeConstructorEventFactory implements IRecordFactory<BeforeConstructorEvent> {

	@Override
	public BeforeConstructorEvent create(final IValueDeserializer deserializer) {
		return new BeforeConstructorEvent(deserializer);
	}

	@Override
	public BeforeConstructorEvent create(final Object[] values) {
		return new BeforeConstructorEvent(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return BeforeConstructorEvent.SIZE;
	}
}
