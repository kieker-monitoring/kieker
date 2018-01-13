package kieker.monitoring.writer.serializer;

import java.nio.ByteBuffer;

import kieker.monitoring.writer.raw.IRawDataWrapper;

public abstract class AbstractBinaryDataWrapper implements IRawDataWrapper<ByteBuffer> {
				
	@Override
	public final boolean supportsBinaryData() {
		return true;
	}

	@Override
	public final boolean supportsCharacterData() {
		return false;
	}

}
