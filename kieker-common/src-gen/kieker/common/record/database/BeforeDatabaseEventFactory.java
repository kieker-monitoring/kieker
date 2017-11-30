package kieker.common.record.database;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Christian Zirkelbach (czi@informatik.uni-kiel.de)
 * 
 * @since 1.14
 */
public final class BeforeDatabaseEventFactory implements IRecordFactory<BeforeDatabaseEvent> {

	@Override
	public BeforeDatabaseEvent create(IValueDeserializer deserializer) {
		return new BeforeDatabaseEvent(deserializer);
	}

	@Override
	@Deprecated
	public BeforeDatabaseEvent create(final Object[] values) {
		return new BeforeDatabaseEvent(values);
	}

	public int getRecordSizeInBytes() {
		return BeforeDatabaseEvent.SIZE;
	}
}
