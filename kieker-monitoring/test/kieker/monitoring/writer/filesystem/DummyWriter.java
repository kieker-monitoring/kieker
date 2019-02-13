/**
 *
 */
package kieker.monitoring.writer.filesystem;

import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * @author Reiner Jung
 *
 */
public class DummyWriter extends Writer {
	// defining buffer to act as a file alternative
	final ByteBuffer dataBuffer = ByteBuffer.allocate(819200);
	CharBuffer buffers = this.dataBuffer.asCharBuffer();

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.Writer#write(char[], int, int)
	 */
	@Override
	public void write(final char[] cbuf, final int off, final int len) throws IOException {

		// write records in buffer
		this.buffers.append(CharBuffer.wrap(cbuf));

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.Writer#flush()
	 */
	@Override
	public void flush() throws IOException {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.Writer#close()
	 */
	@Override
	public void close() throws IOException {

	}

	public String getBufferAsString() {
		// returning back buffer data
		this.buffers.flip();
		return this.buffers.toString().trim();
	}

}
