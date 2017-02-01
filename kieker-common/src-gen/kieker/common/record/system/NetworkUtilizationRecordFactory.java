package kieker.common.record.system;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Teerat Pitakrat
 * 
 * @since 1.12
 */
public final class NetworkUtilizationRecordFactory implements IRecordFactory<NetworkUtilizationRecord> {
	
	@Override
	public NetworkUtilizationRecord create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new NetworkUtilizationRecord(buffer, stringRegistry);
	}
	
	@Override
	public NetworkUtilizationRecord create(final Object[] values) {
		return new NetworkUtilizationRecord(values);
	}
	
	public int getRecordSizeInBytes() {
		return NetworkUtilizationRecord.SIZE;
	}
}
