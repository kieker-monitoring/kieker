package kieker.monitoring.writer.serializer;

import java.nio.CharBuffer;
import java.util.Collection;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.writer.raw.IRawDataWrapper;

public abstract class AbstractBinaryRecordSerializer extends AbstractMonitoringRecordSerializer {

	private static final String UNSUPPORTED_FORMAT_MESSAGE = "This serializer does not support character data.";
	
	public AbstractBinaryRecordSerializer(final Configuration configuration) {
		super(configuration);
	}
	
	@Override
	public final boolean producesBinaryData() {
		return true;
	}
	
	@Override
	public final boolean producesCharacterData() {
		return false;
	}	
	
	@Override
	public final void serializeRecordToCharBuffer(final IMonitoringRecord record, final CharBuffer buffer) {
		throw new UnsupportedOperationException(UNSUPPORTED_FORMAT_MESSAGE);
	}
	
	@Override
	public final void serializeRecordsToCharBuffer(final Collection<IMonitoringRecord> records, final CharBuffer buffer) {
		throw new UnsupportedOperationException(UNSUPPORTED_FORMAT_MESSAGE);
	}
	
	@Override
	public Class<? extends IRawDataWrapper<?>> getWrapperType() {
		return BinaryChunkLengthWrapper.class;
	}
	
}
