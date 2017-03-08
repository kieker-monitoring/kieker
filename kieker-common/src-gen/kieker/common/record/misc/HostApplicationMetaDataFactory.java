package kieker.common.record.misc;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Christian Wulf
 * 
 * @since 1.13
 */
public final class HostApplicationMetaDataFactory implements IRecordFactory<HostApplicationMetaData> {
	
	@Override
	public HostApplicationMetaData create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new HostApplicationMetaData(buffer, stringRegistry);
	}
	
	@Override
	public HostApplicationMetaData create(final Object[] values) {
		return new HostApplicationMetaData(values);
	}
	
	public int getRecordSizeInBytes() {
		return HostApplicationMetaData.SIZE;
	}
}
