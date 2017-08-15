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

package kieker.monitoring.writer.filesystem;

import java.io.IOException;
import java.io.Writer;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
class MeasuringWriter extends Writer {

	private final Writer writer;

	private long charactersWritten;

	public MeasuringWriter(final Writer writer) {
		this.writer = writer;
	}

	public long getCharactersWritten() {
		return this.charactersWritten;
	}

	@Override
	public void write(final char[] cbuf, final int off, final int len) throws IOException {
		this.writer.write(cbuf, off, len);
		this.charactersWritten += len;
	}

	@Override
	public void flush() throws IOException {
		this.writer.flush();
	}

	@Override
	public void close() throws IOException {
		this.writer.close();
	}

}
