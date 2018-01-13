package kieker.monitoring.writer.serializer;

import java.nio.ByteBuffer;
import java.util.Collection;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;

public abstract class AbstractCharacterRecordSerializer extends AbstractMonitoringRecordSerializer {

	private static final String UNSUPPORTED_FORMAT_MESSAGE = "This serializer does not support binary data.";
	
	public AbstractCharacterRecordSerializer(final Configuration configuration) {
		super(configuration);
	}

	@Override
	public final boolean producesBinaryData() {
		return false;
	}
	
	@Override
	public final boolean producesCharacterData() {
		return true;
	}	
	
	@Override
	public final void serializeRecordToByteBuffer(final IMonitoringRecord record, final ByteBuffer buffer) {
		throw new UnsupportedOperationException(UNSUPPORTED_FORMAT_MESSAGE);
	}
	
	@Override
	public final void serializeRecordsToByteBuffer(final Collection<IMonitoringRecord> records, final ByteBuffer buffer) {
		throw new UnsupportedOperationException(UNSUPPORTED_FORMAT_MESSAGE);
	}
	
}
