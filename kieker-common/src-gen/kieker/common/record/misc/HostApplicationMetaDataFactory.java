package kieker.common.record.misc;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.util.registry.IRegistry;

/**
 * @author Christian Wulf
 * 
 * @since 1.13
 */
public final class HostApplicationMetaDataFactory implements IRecordFactory<HostApplicationMetaData> {
	
	@Override
	public HostApplicationMetaData create(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new HostApplicationMetaData(deserializer, buffer, stringRegistry);
	}
	
	@Override
	public HostApplicationMetaData create(final Object[] values) {
		return new HostApplicationMetaData(values);
	}
	
	@Override
	public int getRecordSizeInBytes() {
		return HostApplicationMetaData.SIZE;
	}
}
