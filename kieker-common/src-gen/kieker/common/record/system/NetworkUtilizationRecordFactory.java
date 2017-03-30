package kieker.common.record.system;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Teerat Pitakrat
 *
 * @since 1.12
 */
public final class NetworkUtilizationRecordFactory implements IRecordFactory<NetworkUtilizationRecord> {

	@Override
	public NetworkUtilizationRecord create(final IValueDeserializer deserializer) {
		return new NetworkUtilizationRecord(deserializer);
	}

	@Override
	public NetworkUtilizationRecord create(final Object[] values) {
		return new NetworkUtilizationRecord(values);
	}

	@Override
	public int getRecordSizeInBytes() {
		return NetworkUtilizationRecord.SIZE;
	}
}
