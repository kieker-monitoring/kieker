package kieker.common.record.database;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Christian Zirkelbach (czi@informatik.uni-kiel.de)
 * 
 * @since 1.14
 */
public final class AfterDatabaseEventFactory implements IRecordFactory<AfterDatabaseEvent> {

	@Override
	public AfterDatabaseEvent create(IValueDeserializer deserializer) {
		return new AfterDatabaseEvent(deserializer);
	}

	@Override
	@Deprecated
	public AfterDatabaseEvent create(final Object[] values) {
		return new AfterDatabaseEvent(values);
	}

	public int getRecordSizeInBytes() {
		return AfterDatabaseEvent.SIZE;
	}
}
