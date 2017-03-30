package kieker.common.record.flow.trace;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Jan Waller
 *
 * @since 1.5
 */
public final class ConstructionEventFactory implements IRecordFactory<ConstructionEvent> {

	@Override
	public ConstructionEvent create(final IValueDeserializer deserializer) {
		return new ConstructionEvent(deserializer);
	}

	@Override
	public ConstructionEvent create(final Object[] values) {
		return new ConstructionEvent(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return ConstructionEvent.SIZE;
	}
}
