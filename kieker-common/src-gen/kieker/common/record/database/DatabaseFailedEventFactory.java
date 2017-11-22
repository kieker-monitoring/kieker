package kieker.common.record.database;

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Christian Zirkelbach (czi@informatik.uni-kiel.de)
 * 
 * @since 1.14
 */
public final class DatabaseFailedEventFactory implements IRecordFactory<DatabaseFailedEvent> {

	@Override
	public DatabaseFailedEvent create(IValueDeserializer deserializer) {
		return new DatabaseFailedEvent(deserializer);
	}

	@Override
	@Deprecated
	public DatabaseFailedEvent create(final Object[] values) {
		return new DatabaseFailedEvent(values);
	}

	public int getRecordSizeInBytes() {
		return DatabaseFailedEvent.SIZE;
	}
}
