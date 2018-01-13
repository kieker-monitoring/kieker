package kieker.analysis.plugin.reader.newio;

import java.nio.ByteBuffer;

public abstract class AbstractCharacterDataUnwrapper implements IRawDataUnwrapper {
	
	@Override
	public final boolean supportsBinaryData() {
		return false;
	}
	
	@Override
	public final boolean supportsCharacterData() {
		return true;
	}
	
	@Override
	public final ByteBuffer fetchBinaryData() {
		throw new UnsupportedOperationException("This unwrapper does not support binary data.");
	}

}
