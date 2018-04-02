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

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import org.slf4j.Logger;

/**
 *
 * @author Reiner Jung
 *
 * @since 1.14
 *
 * @deprecated 1.14 should be removed in 1.15 replaced by new FileWriter API
 */
@Deprecated
public class AsciiPooledFileChannel extends AbstractPooledFileChannel<CharBuffer> {

	private final PrintWriter printWriter;
	private final MeasuringWriter currentMeasuringWriter;

	public AsciiPooledFileChannel(final OutputStream outputStream, final Charset charset, final CharBuffer buffer) {
		super(buffer);

		final Writer writer = new OutputStreamWriter(outputStream, charset);
		final BufferedWriter bufferedWriter = new BufferedWriter(writer);
		this.currentMeasuringWriter = new MeasuringWriter(bufferedWriter);
		this.printWriter = new PrintWriter(this.currentMeasuringWriter);
	}

	@Override
	public long getBytesWritten() {
		return this.currentMeasuringWriter.getCharactersWritten();
	}

	@Override
	public void flush(final Logger logger) {
		this.getBuffer().flip();
		this.printWriter.print(this.getBuffer().toString());
		this.printWriter.println();
	}

	@Override
	public void close(final Logger logger) {
		this.flush(logger);
		this.printWriter.close();
	}

}
