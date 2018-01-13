package kieker.monitoring.writer.serializer;

import java.nio.ByteBuffer;

public class BinaryChunkLengthWrapper extends AbstractBinaryDataWrapper {	

	private final ByteBuffer outputBuffer;
	
	public BinaryChunkLengthWrapper(final int sourceBufferSize) {
		// The wrapper adds an int, i.e. 4 bytes
		final int requiredOutputBufferSize = sourceBufferSize + 4;		
		this.outputBuffer = ByteBuffer.allocate(requiredOutputBufferSize);
	}
	
	@Override
	public ByteBuffer wrap(final ByteBuffer data) {
		final ByteBuffer targetBuffer = this.outputBuffer;
		final int dataSize = data.limit();
		
		// Rewind the target buffer and prepend the data length
		targetBuffer.rewind();
		targetBuffer.putInt(dataSize);
		targetBuffer.put(data);
		
		return targetBuffer;
	}

}
