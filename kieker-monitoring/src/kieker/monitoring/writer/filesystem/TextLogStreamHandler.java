/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.TextValueSerializer;
import kieker.common.registry.writer.WriterRegistry;
import kieker.common.util.filesystem.FSUtil;
import kieker.monitoring.writer.WriterUtil;
import kieker.monitoring.writer.compression.ICompressionFilter;

/**
 * Create log files following the Kieker DAT format of semicolon separated values.
 * The handler supports compression.
 *
 * @author Reiner Jung
 *
 * @since 1.14
 *
 */
public class TextLogStreamHandler extends AbstractLogStreamHandler {

	/** line separator string. */
	private static final String LINE_SEPARATOR = System.lineSeparator();

	private static final Logger LOGGER = LoggerFactory.getLogger(TextLogStreamHandler.class);

	private final CharBuffer buffer;

	/**
	 * Create a text log stream handler.
	 *
	 * @param flushLogFile
	 *            activate flush after every record
	 * @param bufferSize
	 *            write buffer size
	 * @param charset
	 *            charset
	 * @param compressionFilter
	 *            compression filter
	 * @param writerRegistry
	 *            dummy writer registry, not used in the text log
	 */
	public TextLogStreamHandler(final Boolean flushLogFile, final Integer bufferSize, final Charset charset, final ICompressionFilter compressionFilter,
			final WriterRegistry writerRegistry) { // NOPMD writerRegistry is API and not used for text serialization
		super(flushLogFile, bufferSize, charset, compressionFilter, writerRegistry);

		this.buffer = CharBuffer.allocate(bufferSize);
		this.serializer = TextValueSerializer.create(this.buffer);
		this.extension = FSUtil.DAT_FILE_EXTENSION;
	}

	@Override
	public void serialize(final IMonitoringRecord record, final int id) throws IOException {
		final String header = String.format("$%d;%d", id, record.getLoggingTimestamp());

		this.buffer.put(header);

		record.serialize(this.serializer);

		this.buffer.put(LINE_SEPARATOR);

		this.buffer.flip();

		try {
			while (this.buffer.hasRemaining()) {
				this.numOfBytes += this.outputChannel.write(this.charset.encode(this.buffer));
			}
			this.numOfEntries++;

			this.buffer.clear();
			if (this.flushLogFile) {
				this.serializedStream.flush();
			}
		} catch (final IOException e) {
			LOGGER.error("Caught exception while writing to the channel.", e);
			WriterUtil.close(this.outputChannel, LOGGER);
		}
	}

}
