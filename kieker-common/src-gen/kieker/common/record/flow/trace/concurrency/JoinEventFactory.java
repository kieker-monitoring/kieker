package kieker.common.record.flow.trace.concurrency;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Jan Waller
 *
 * @since 1.8
 */
public final class JoinEventFactory implements IRecordFactory<JoinEvent> {

	@Override
	public JoinEvent create(final IValueDeserializer deserializer) {
		return new JoinEvent(deserializer);
	}

	@Override
	public JoinEvent create(final Object[] values) {
		return new JoinEvent(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return JoinEvent.SIZE;
	}
}
