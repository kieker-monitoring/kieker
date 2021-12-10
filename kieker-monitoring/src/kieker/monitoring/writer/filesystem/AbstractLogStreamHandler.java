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
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.IValueSerializer;
import kieker.common.registry.writer.WriterRegistry;
import kieker.monitoring.writer.compression.ICompressionFilter;

/**
 * Abstract class for log stream handler. As log stream handler share a lot of functionality,
 * we use an abstract class instead of an interface to define the API and
 * implement common functionality.
 *
 * @author Reiner Jung
 *
 * @since 1.14
 */
public abstract class AbstractLogStreamHandler {

	protected final boolean flushLogFile;
	protected final ICompressionFilter compressionFilter;
	protected int numOfEntries;
	protected final WriterRegistry writerRegistry;
	protected final Charset charset;

	protected IValueSerializer serializer;

	protected OutputStream serializedStream;
	protected WritableByteChannel outputChannel;
	protected int numOfBytes;

	protected String extension;

	/**
	 * Create an abstract log stream handler.
	 *
	 * @param flushLogFile
	 *            flag indicating whether the file should be flushed after every written record
	 * @param bufferSize
	 *            buffer sizer
	 * @param charset
	 *            character set used for serialization
	 * @param compressionFilter
	 *            compression filter
	 * @param writerRegistry
	 *            string registry
	 */
	public AbstractLogStreamHandler(final Boolean flushLogFile, final Integer bufferSize, final Charset charset, // NOPMD charset not used in binary
			final ICompressionFilter compressionFilter, final WriterRegistry writerRegistry) {
		this.flushLogFile = flushLogFile;
		this.compressionFilter = compressionFilter;
		this.charset = charset;
		this.writerRegistry = writerRegistry;
	}

	/**
	 * Initialize a new stream.
	 *
	 * @param serializedOutputStream
	 *            stream to be used
	 * @param fileName
	 *            file name of the stream, this is used by some compression filters
	 *
	 * @throws IOException
	 *             when the creation of the channel fails
	 */
	public void initialize(final OutputStream serializedOutputStream, final Path fileName) throws IOException {
		this.serializedStream = serializedOutputStream;
		this.outputChannel = Channels.newChannel(this.compressionFilter.chainOutputStream(serializedOutputStream, fileName));
		this.numOfEntries = 0;
	}

	/**
	 * Return the number of written entries.
	 *
	 * @return number of entries
	 */
	public int getNumOfEntries() {
		return this.numOfEntries;
	}

	/**
	 * Return the number of written bytes.
	 *
	 * @return number of bytes
	 */
	public long getNumOfBytes() {
		return this.numOfBytes;
	}

	/**
	 * Close the log file.
	 *
	 * @throws IOException
	 *             on io error
	 */
	public void close() throws IOException {
		this.outputChannel.close();
		this.serializedStream.close();
	}

	/**
	 * Get the file extension for the stream.
	 *
	 * @return return the file extension
	 */
	public String getFileExtension() {
		final String compressExtension = this.compressionFilter.getExtension();
		if (compressExtension == null) {
			return this.extension;
		} else {
			return compressExtension;
		}
	}

	/**
	 * Serialize a record.
	 *
	 * @param record
	 *            the record itself
	 * @param id
	 *            the type id from the string registry
	 *
	 * @throws IOException
	 *             on io errors, e.g., write errors
	 */
	public abstract void serialize(IMonitoringRecord record, int id) throws IOException;

}
