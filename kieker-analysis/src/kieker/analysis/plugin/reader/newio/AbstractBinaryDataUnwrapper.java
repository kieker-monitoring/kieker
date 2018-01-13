package kieker.analysis.plugin.reader.newio;

import java.nio.CharBuffer;

public abstract class AbstractBinaryDataUnwrapper implements IRawDataUnwrapper {

	@Override
	public final boolean supportsBinaryData() {
		return true;
	}
	
	@Override
	public final boolean supportsCharacterData() {
		return false;
	}
	
	@Override
	public final CharBuffer fetchCharacterData() {
		throw new UnsupportedOperationException("This unwrapper does not support character data.");
	}
	
}
