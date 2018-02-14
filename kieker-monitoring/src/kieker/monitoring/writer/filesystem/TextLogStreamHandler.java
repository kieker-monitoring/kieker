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
import java.io.OutputStream;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.TextValueSerializer;
import kieker.monitoring.registry.WriterRegistry;
import kieker.monitoring.writer.WriterUtil;
import kieker.monitoring.writer.filesystem.compression.ICompressionFilter;

/**
 * @author Reiner Jung
 *
 * @since 1.14
 *
 */
public class TextLogStreamHandler implements ILogStreamHandler {

	/** line separator string. */
	private static final String LINE_SEPARATOR = System.lineSeparator();

	private static final Log LOGGER = LogFactory.getLog(TextLogStreamHandler.class);

	private final boolean flushLogFile;
	private final ICompressionFilter compressionFilter;
	private int numOfEntries;
	private final CharBuffer buffer;
	private final TextValueSerializer serializer;
	private final Charset charset;
	private WritableByteChannel outputChannel;

	private int numOfBytes;
	private OutputStream serializedStream;

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
		this.flushLogFile = flushLogFile;
		this.buffer = CharBuffer.allocate(bufferSize);
		this.charset = charset;

		this.compressionFilter = compressionFilter;
		this.serializer = TextValueSerializer.create(this.buffer);
	}

	@Override
	public void initialize(final OutputStream serializedStream, final Path fileName) throws IOException {
		this.serializedStream = serializedStream;
		this.outputChannel = Channels.newChannel(this.compressionFilter.chainOutputStream(serializedStream, fileName));
		this.numOfEntries = 0;
	}

	@Override
	public void close() throws IOException {
		this.outputChannel.close();
	}

	@Override
	public int getNumOfEntries() {
		return this.numOfEntries;
	}

	@Override
	public long getNumOfBytes() {
		return this.numOfBytes;
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

	@Override
	public String getFileExtension() {
		final String extension = this.compressionFilter.getExtension();
		if (extension == null) {
			return this.serializer.getFileExtension();
		} else {
			return extension;
		}
	}

}
