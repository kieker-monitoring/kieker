/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package kieker.analysis.plugin.reader.newio.deserializer;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import kieker.analysis.plugin.reader.newio.AbstractBinaryDataUnwrapper;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * Binary unwrapper which expects an int (big-endian) preceding the data block
 * specifying its size in bytes.
 *
 * @author Holger Knoche
 * @since 2.0
 */
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
		final byte[] bytes = this.returnBytes;

		// Retrieves the size of the next valid chunk, and skips
		// invalid chunks in between
		final int chunkLength = this.getNextValidChunkSize(bytes.length);

		if (chunkLength < 0) {
			return null;
		}

		final DataInputStream dataStream = this.inputStream;
		dataStream.readFully(bytes, 0, chunkLength);

		// Reset the buffer according to the received data
		final ByteBuffer buffer = this.returnBuffer;
		buffer.rewind();
		buffer.limit(chunkLength);

		return buffer;
	}

	private int getNextValidChunkSize(final int bufferCapacity) throws IOException {
		final DataInputStream dataStream = this.inputStream;

		boolean invalidChunk;
		int chunkSize;

		do {
			invalidChunk = false;

			try {
				chunkSize = dataStream.readInt();
			} catch (final EOFException e) {
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
				if (LOG.isWarnEnabled()) {
					LOG.warn("Insufficient buffer capacity to read chunk of size " + chunkSize + ", skipping.");
				}

				this.skipChunk(chunkSize);
				invalidChunk = true;
			}
		} while (invalidChunk);

		return chunkSize;
	}

	private void skipChunk(final int chunkSize) throws IOException {
		final DataInputStream dataStream = this.inputStream;
		long remainingBytes = chunkSize;

		while (remainingBytes > 0) {
			final long bytesSkipped = dataStream.skip(remainingBytes);

			// If no bytes were skipped, check for EOF
			if (bytesSkipped == 0) {
				final int probeValue = dataStream.read();

				// If EOF is (prematurely) encountered, throw an exception as to avoid an infinite loop
				if (probeValue < 0) {
					throw new IOException("Premature end of stream encountered, aborting.");
				}
			}

			remainingBytes -= bytesSkipped;
		}
	}

}
