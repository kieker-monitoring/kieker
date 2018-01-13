package kieker.analysis.plugin.reader.newio.deserializer;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import kieker.analysis.plugin.reader.newio.AbstractBinaryDataUnwrapper;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

public class BinaryChunkLengthUnwrapper extends AbstractBinaryDataUnwrapper {

	private static final int BUFFER_SIZE = 65536;

	private static final Log LOG = LogFactory.getLog(BinaryChunkLengthUnwrapper.class);
	
	private final byte[] returnBytes;
	private final ByteBuffer returnBuffer;
	
	private final DataInputStream inputStream;
		
	public BinaryChunkLengthUnwrapper(final InputStream inputStream) {
		this.inputStream = new DataInputStream(inputStream);
		
		this.returnBytes = new byte[BUFFER_SIZE];
		this.returnBuffer = ByteBuffer.wrap(this.returnBytes);
	}
	
	@Override
	public ByteBuffer fetchBinaryData() throws IOException {
		DataInputStream dataStream = this.inputStream;
		byte[] bytes = this.returnBytes;
		ByteBuffer buffer = this.returnBuffer;
		
		// Retrieves the size of the next valid chunk, and skips
		// invalid chunks in between
		int chunkLength = this.getNextValidChunkSize(bytes.length);

		if (chunkLength < 0) {
			return null;
		}
		
		dataStream.readFully(bytes, 0, chunkLength);
		
		// Reset the buffer according to the received data
		buffer.rewind();
		buffer.limit(chunkLength);
		
		return buffer;
	}
	
	private int getNextValidChunkSize(final int bufferCapacity) throws IOException {
		DataInputStream dataStream = this.inputStream;
		
		boolean invalidChunk;		
		int chunkSize;
		
		do {
			invalidChunk = false;
						
			try {
				chunkSize = dataStream.readInt();			
			} catch (EOFException e) {
				// If an EOF occurs at this position, the stream is considered to have ended gracefully
				return -1;
			}
			
			// Check for negative chunk sizes
			if (chunkSize < 0) {
				throw new IOException("Chunk size less than zero encountered, aborting.");
			}
			
			// Check if there is enough space for the data
			if (chunkSize > bufferCapacity) {
				// If there is insufficient capacity, try to skip until a right-sized chunk is encountered
				LOG.warn("Insufficient buffer capacity to read chunk of size " + chunkSize + ", skipping.");
				this.skipChunk(chunkSize);
				invalidChunk = true;
			}
		} while (invalidChunk);
		
		return chunkSize;
	}
	
	private void skipChunk(final int chunkSize) throws IOException {
		DataInputStream dataStream = this.inputStream;
		long remainingBytes = chunkSize;
		
		while (remainingBytes > 0) {
			long bytesSkipped = dataStream.skip(remainingBytes);
			
			// If no bytes were skipped, check for EOF
			if (bytesSkipped == 0) {
				int probeValue = inputStream.read();
				
				// If EOF is (prematurely) encountered, throw an exception as to avoid an infinite loop 
				if (probeValue < 0) {
					throw new IOException("Premature end of stream encountered, aborting.");
				}
			}
			
			remainingBytes -= bytesSkipped;
		}
	}

}
