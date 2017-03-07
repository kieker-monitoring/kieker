package kieker.common.record.system;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.util.registry.IRegistry;

/**
 * @author Teerat Pitakrat
 * 
 * @since 1.12
 */
public final class NetworkUtilizationRecordFactory implements IRecordFactory<NetworkUtilizationRecord> {
	
	@Override
	public NetworkUtilizationRecord create(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new NetworkUtilizationRecord(deserializer, buffer, stringRegistry);
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
