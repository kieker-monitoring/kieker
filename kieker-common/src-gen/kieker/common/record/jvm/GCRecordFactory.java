package kieker.common.record.jvm;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public final class GCRecordFactory implements IRecordFactory<GCRecord> {
	
	@Override
	public GCRecord create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new GCRecord(buffer, stringRegistry);
	}
	
	@Override
	public GCRecord create(final Object[] values) {
		return new GCRecord(values);
	}
	
	public int getRecordSizeInBytes() {
		return GCRecord.SIZE;
	}
}
