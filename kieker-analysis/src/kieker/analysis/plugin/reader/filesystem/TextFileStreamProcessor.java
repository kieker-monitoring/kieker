/***************************************************************************
 * Copyright 2018 iObserve Project (https://www.iobserve-devops.net)
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
package kieker.analysis.plugin.reader.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.nio.CharBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.plugin.reader.util.IMonitoringRecordReceiver;
import kieker.analysisteetime.plugin.reader.filesystem.util.MappingException;
import kieker.common.exception.MonitoringRecordException;
import kieker.common.exception.UnknownRecordTypeException;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.factory.CachedRecordFactoryCatalog;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.TextValueDeserializer;
import kieker.common.registry.reader.ReaderRegistry;

/**
 * @author Reiner Jung
 *
 * @since 1.15
 *
 */
public class TextFileStreamProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(TextFileStreamProcessor.class);

	private static final int BUFFER_SIZE = 10240;

	private final boolean ignoreUnknownRecordTypes;

	private final CachedRecordFactoryCatalog recordFactories = CachedRecordFactoryCatalog.getInstance();
	private final CharBuffer charBuffer = CharBuffer.allocate(TextFileStreamProcessor.BUFFER_SIZE);

	private boolean terminated;

	private final IMonitoringRecordReceiver recordReceiver;

	private final ReaderRegistry<String> stringRegistry;

	public TextFileStreamProcessor(final boolean ignoreUnknownRecordTypes, final ReaderRegistry<String> stringRegistry, final IMonitoringRecordReceiver recordReceiver) {
		this.ignoreUnknownRecordTypes = ignoreUnknownRecordTypes;
		this.recordReceiver = recordReceiver;
		this.terminated = false;
		this.stringRegistry = stringRegistry;
	}

	/**
	 * Process an input stream.
	 *
	 * @param inputStream
	 *            input stream to be parsed
	 * @throws IOException
	 *             on IO errors reading the file
	 * @throws MappingException
	 *             when type mapping fails
	 * @throws MonitoringRecordException
	 *             issues with monitoring records
	 * @throws UnknownRecordTypeException
	 *             if the type is not known
	 */
	public void processInputChannel(final InputStream inputStream)
			throws IOException, MappingException, MonitoringRecordException, UnknownRecordTypeException {
		final byte[] buffer = new byte[TextFileStreamProcessor.BUFFER_SIZE];

		boolean endOfFile = false;
		int offset = 0;
		while (!endOfFile && !this.terminated) {
			final int numOfReadBytes = inputStream.read(buffer, offset, TextFileStreamProcessor.BUFFER_SIZE - offset);
			if (numOfReadBytes == -1) { /** end of line. */
				endOfFile = true;
			} else {
				final int numOfBufferedBytes = numOfReadBytes + offset;
				final int mark = this.processBuffer(buffer, offset, numOfBufferedBytes);
				/** move remaining left. */
				for (int j = mark + 1; j < numOfBufferedBytes; j++) {
					buffer[j - mark - 1] = buffer[j];
				}
				offset = numOfBufferedBytes - mark;
			}
		}
		if (this.charBuffer.position() > 0) {
			this.createRecord();
		}
	}

	/**
	 * Process input buffer and create records.
	 *
	 * @param classNameRegistry
	 *            registry for class names
	 * @param buffer
	 *            byte array containing the last read blob
	 * @param offset
	 *            byte buffer offset
	 * @param numOfBufferedBytes
	 *            number of read bytes from the last read operation
	 *
	 * @return mark in the buffer
	 *
	 * @throws MappingException
	 *             teetime exception
	 * @throws MonitoringRecordException
	 *             record creation failed
	 * @throws UnknownRecordTypeException
	 *             record type is unknown
	 */
	private int processBuffer(final byte[] buffer, final int offset,
			final int numOfBufferedBytes)
					throws MappingException, MonitoringRecordException, UnknownRecordTypeException {
		int i = offset;
		int mark = 0;
		while (i < numOfBufferedBytes) {
			final char ch = (char) buffer[i];
			if (ch == '\n') {
				this.createRecord();
				mark = i;
			} else if (ch == '\r') {
				if (buffer[i + 1] == '\n') {
					i++;
				}
				this.createRecord();
				mark = i;
			} else {
				this.charBuffer.append(ch);
			}
			i++;
		}

		return mark;
	}

	private void createRecord()
			throws MappingException, MonitoringRecordException, UnknownRecordTypeException {
		this.charBuffer.flip();
		if (this.charBuffer.hasRemaining()) {
			final char lead = this.charBuffer.get();
			if (lead == '$') {
				final TextValueDeserializer deserializer = TextValueDeserializer.create(this.charBuffer);
				final int id = deserializer.getInt();
				final String classname = this.stringRegistry.get(id);
				if (classname == null) {
					if (this.ignoreUnknownRecordTypes) {
						return;
					} else {
						throw new MappingException("Missing classname mapping for record type id " + "'" + id + "'");
					}
				}
				final long loggingTimestamp = deserializer.getLong();
				final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.recordFactories.get(classname);
				if (recordFactory != null) {
					try {
						final IMonitoringRecord event = recordFactory.create(deserializer);
						event.setLoggingTimestamp(loggingTimestamp);

						if (!this.recordReceiver.newMonitoringRecord(event)) {
							this.terminated = true;
						}
					} catch (final NumberFormatException ex) {
						LOGGER.error("Record of type {} format error.", classname);
					}
				} else {
					LOGGER.info("Record type {} unkown", classname);
				}
				this.charBuffer.clear();
			}
		}
	}
}
