package kieker.monitoring.writer.serializer;

import java.nio.ByteBuffer;

/**
 * No-op implementation of a binary wrapper.
 * 
 * @author Holger Knoche
 * @since 2.0
 */
public class NopBinaryWrapper extends AbstractBinaryDataWrapper {

	public NopBinaryWrapper(final int bufferSize) {
		// Do nothing
	}
	
	@Override
	public ByteBuffer wrap(final ByteBuffer data) {
		return data;
	}
	
}
