package kieker.common.record.misc;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Jan Waller
 *
 * @since 1.7
 */
public final class KiekerMetadataRecordFactory implements IRecordFactory<KiekerMetadataRecord> {

	@Override
	public KiekerMetadataRecord create(final IValueDeserializer deserializer) {
		return new KiekerMetadataRecord(deserializer);
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
