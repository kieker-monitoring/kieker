/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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
package kieker.monitoring.writer.filesystem;

import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * @author Reiner Jung
 *
 * @since 1.15
 */
public class DummyWriter extends Writer {

	// defining buffer to act as a file alternative
	private final ByteBuffer dataBuffer = ByteBuffer.allocate(819200);
	private final CharBuffer buffers = this.dataBuffer.asCharBuffer();

	public DummyWriter() {
		super();
	}

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
	public void flush() throws IOException {}

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
