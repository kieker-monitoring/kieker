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
package kieker.analysis.source.file;

import java.io.IOException;
import java.io.InputStream;
import java.nio.CharBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysisteetime.plugin.reader.filesystem.util.MappingException;
import kieker.common.configuration.Configuration;
import kieker.common.exception.MonitoringRecordException;
import kieker.common.exception.UnknownRecordTypeException;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.factory.CachedRecordFactoryCatalog;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.TextValueDeserializer;
import kieker.common.registry.reader.ReaderRegistry;

import teetime.framework.OutputPort;

/**
 * @author Reiner Jung
 *
 * @since 1.15
 */
public class DatEventDeserializer extends AbstractEventDeserializer {

	public static final String PREFIX = BinaryEventDeserializer.class.getCanonicalName() + ".";

	public static final String BUFFER_SIZE = PREFIX + "bufferSize";

	public static final int DEFAULT_BUFFER_SIZE = 102400;

	private static final String CHARSET = PREFIX + "charset";

	private static final String DEFAULT_CHARSET = "UTF-8";

	private static final Logger LOGGER = LoggerFactory.getLogger(DatEventDeserializer.class);

	private final CachedRecordFactoryCatalog recordFactories = CachedRecordFactoryCatalog.getInstance();

	private final CharBuffer charBuffer;

	private final String charset; // NOPMD will be used for a future feature

	private long lineNumber;

	public DatEventDeserializer(final Configuration configuration, final ReaderRegistry<String> registry) {
		super(configuration, registry);
		final int bufferSize = configuration.getIntProperty(BinaryEventDeserializer.BUFFER_SIZE, DEFAULT_BUFFER_SIZE);
		this.charBuffer = CharBuffer.allocate(bufferSize);
		this.charset = configuration.getStringProperty(CHARSET, DEFAULT_CHARSET);
	}

	@Override
	public void processDataStream(final InputStream inputStream, final OutputPort<IMonitoringRecord> outputPort) throws IOException {
		final byte[] buffer = new byte[this.charBuffer.length() * 2]; // NOPMD

		boolean endOfFile = false;
		int offset = 0;
		this.lineNumber = 0;
		while (!endOfFile) {
			final int numOfReadBytes = inputStream.read(buffer, offset, buffer.length - offset);
			if (numOfReadBytes == -1) { /** end of line. */
				endOfFile = true;
			} else {
				final int numOfBufferedBytes = numOfReadBytes + offset;
				final int mark = this.processBuffer(buffer, offset, numOfBufferedBytes, outputPort);
				/** move remaining left. */
				for (int j = mark + 1; j < numOfBufferedBytes; j++) {
					buffer[j - mark - 1] = buffer[j];
				}
				offset = numOfBufferedBytes - mark;
			}
		}
		if (this.charBuffer.position() > 0) {
			this.createRecord(outputPort);
		}
	}

	/**
	 * Process input buffer and create records.
	 *
	 * @param buffer
	 *            byte array containing the last read blob
	 * @param offset
	 *            byte buffer offset
	 * @param numOfBufferedBytes
	 *            number of read bytes from the last read operation
	 * @param outputPort
	 *            output port to be used for deserialized records
	 *
	 * @return mark in the buffer
	 *
	 * @throws MappingException
	 *             teetime exception
	 * @throws MonitoringRecordException
	 *             record creation failed
	 * @throws UnknownRecordTypeException
	 *             record type is unknown
	 *
	 *             TODO for UTF-8 strings this will not work properly, as these might use 2 bytes
	 */
	private int processBuffer(final byte[] buffer, final int offset,
			final int numOfBufferedBytes, final OutputPort<IMonitoringRecord> outputPort) {
		int i = offset;
		int mark = 0;
		while (i < numOfBufferedBytes) {
			final char ch = (char) buffer[i];
			if (ch == '\n') {
				this.lineNumber++;
				this.createRecord(outputPort);
				mark = i;
			} else if (ch == '\r') {
				if (buffer[i + 1] == '\n') {
					i++;
				}
				this.lineNumber++;
				this.createRecord(outputPort);
				mark = i;
			} else {
				this.charBuffer.append(ch);
			}
			i++;
		}

		return mark;
	}

	private void createRecord(final OutputPort<IMonitoringRecord> outputPort) {
		this.charBuffer.flip();
		final char lead = this.charBuffer.get();
		if (lead == '$') {
			final TextValueDeserializer deserializer = TextValueDeserializer.create(this.charBuffer);
			final int id = deserializer.getInt();
			final String classname = this.registry.get(id);
			if (classname == null) {
				LOGGER.error("Missing classname mapping for record type id '{}'", id);
			} else {
				final long loggingTimestamp = deserializer.getLong();
				final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.recordFactories.get(classname);
				final IMonitoringRecord event = recordFactory.create(deserializer);
				event.setLoggingTimestamp(loggingTimestamp);
				outputPort.send(event);
				this.charBuffer.clear();
			}
		} else {
			LOGGER.error("Malformed entry in file at line {}.", this.lineNumber);
		}
	}

}
