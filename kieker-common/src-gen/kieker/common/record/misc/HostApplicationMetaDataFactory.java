package kieker.common.record.misc;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
public final class HostApplicationMetaDataFactory implements IRecordFactory<HostApplicationMetaData> {

	@Override
	public HostApplicationMetaData create(final IValueDeserializer deserializer) {
		return new HostApplicationMetaData(deserializer);
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
