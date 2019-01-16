/**
 *
 */
package kieker.monitoring.writer.filesystem;

import java.io.IOException;
import java.io.Writer;

/**
 * @author Reiner Jung
 *
 */
public class DummyWriter extends Writer {

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.Writer#write(char[], int, int)
	 */
	@Override
	public void write(final char[] cbuf, final int off, final int len) throws IOException {

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

}
