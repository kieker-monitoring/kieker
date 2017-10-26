package kieker.common.record.flow.trace;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Felix Eichhorst
 * 
 * @since 1.14
 */
public final class BeforeReceivedRemoteEventFactory implements IRecordFactory<BeforeReceivedRemoteEvent> {
	
	@Override
	public BeforeReceivedRemoteEvent create(final IValueDeserializer deserializer) {
		return new BeforeReceivedRemoteEvent(deserializer);
	}
	
	@Override
	@Deprecated
	public BeforeReceivedRemoteEvent create(final Object[] values) {
		return new BeforeReceivedRemoteEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return BeforeReceivedRemoteEvent.SIZE;
	}
}
