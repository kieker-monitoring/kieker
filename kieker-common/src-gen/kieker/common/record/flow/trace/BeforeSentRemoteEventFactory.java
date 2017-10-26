package kieker.common.record.flow.trace;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Felix Eichhorst
 * 
 * @since 1.14
 */
public final class BeforeSentRemoteEventFactory implements IRecordFactory<BeforeSentRemoteEvent> {
	
	@Override
	public BeforeSentRemoteEvent create(final IValueDeserializer deserializer) {
		return new BeforeSentRemoteEvent(deserializer);
	}
	
	@Override
	@Deprecated
	public BeforeSentRemoteEvent create(final Object[] values) {
		return new BeforeSentRemoteEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return BeforeSentRemoteEvent.SIZE;
	}
}
