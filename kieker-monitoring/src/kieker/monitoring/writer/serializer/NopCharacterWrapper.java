package kieker.monitoring.writer.serializer;

import java.nio.CharBuffer;

/**
 * No-op implementation of a character wrapper.
 * 
 * @author Holger Knoche
 * @since 2.0
 */
public class NopCharacterWrapper extends AbstractCharacterDataWrapper {

	@Override
	public CharBuffer wrap(final CharBuffer data) {
		return data;
	}
	
}
