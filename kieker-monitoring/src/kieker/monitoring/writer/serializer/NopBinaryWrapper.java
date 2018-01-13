package kieker.monitoring.writer.serializer;

import java.nio.ByteBuffer;

/**
 * No-op implementation of a binary wrapper.
 * 
 * @author Holger Knoche
 * @since 2.0
 */
public class NopBinaryWrapper extends AbstractBinaryDataWrapper {

	@Override
	public ByteBuffer wrap(final ByteBuffer data) {
		return data;
	}
	
}
