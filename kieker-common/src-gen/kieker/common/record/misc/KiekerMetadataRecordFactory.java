package kieker.common.record.misc;

import java.nio.ByteBuffer;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.7
 */
public final class KiekerMetadataRecordFactory implements IRecordFactory<KiekerMetadataRecord> {
	
	@Override
	public KiekerMetadataRecord create(final IValueDeserializer deserializer, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new KiekerMetadataRecord(deserializer, buffer, stringRegistry);
	}
	
	@Override
	public KiekerMetadataRecord create(final Object[] values) {
		return new KiekerMetadataRecord(values);
	}
	
	@Override
	public int getRecordSizeInBytes() {
		return KiekerMetadataRecord.SIZE;
	}
}
