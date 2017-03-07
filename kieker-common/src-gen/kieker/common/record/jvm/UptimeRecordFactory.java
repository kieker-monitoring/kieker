package kieker.common.record.jvm;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.util.registry.IRegistry;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public final class UptimeRecordFactory implements IRecordFactory<UptimeRecord> {
	
	@Override
	public UptimeRecord create(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new UptimeRecord(deserializer, buffer, stringRegistry);
	}
	
	@Override
	public UptimeRecord create(final Object[] values) {
		return new UptimeRecord(values);
	}
	
	@Override
	public int getRecordSizeInBytes() {
		return UptimeRecord.SIZE;
	}
}
