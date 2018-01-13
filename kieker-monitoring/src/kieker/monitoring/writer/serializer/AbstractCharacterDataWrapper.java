package kieker.monitoring.writer.serializer;

import java.nio.CharBuffer;

import kieker.monitoring.writer.raw.IRawDataWrapper;

public abstract class AbstractCharacterDataWrapper implements IRawDataWrapper<CharBuffer> {

	@Override
	public final boolean supportsBinaryData() {
		return false;
	}

	@Override
	public final boolean supportsCharacterData() {
		return true;
	}

}
