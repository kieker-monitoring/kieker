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
package kieker.analysisteetime.plugin.reader.filesystem.format.text.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.CharBuffer;

import kieker.analysisteetime.plugin.reader.filesystem.className.ClassNameRegistry;
import kieker.analysisteetime.plugin.reader.filesystem.className.ClassNameRegistryRepository;
import kieker.analysisteetime.plugin.reader.filesystem.util.MappingException;
import kieker.common.exception.MonitoringRecordException;
import kieker.common.exception.UnknownRecordTypeException;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.factory.CachedRecordFactoryCatalog;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.TextValueDeserializer;

import teetime.stage.basic.AbstractTransformation;

/**
 * Read a dat file and output records.
 * Replaces @{link DatFile2RecordFilter}
 *
 * @author Reiner Jung
 *
 * @since 1.14
 */
public class DatFileToRecordStage extends AbstractTransformation<File, IMonitoringRecord> {

	private static final int BUFFER_SIZE = 10240;
	private final String charset;
	private final ClassNameRegistryRepository classNameRegistryRepository;
	private final CachedRecordFactoryCatalog recordFactories = CachedRecordFactoryCatalog.getInstance();
	private final CharBuffer charBuffer = CharBuffer.allocate(DatFileToRecordStage.BUFFER_SIZE);

	/**
	 * Create a dat file to record stage.
	 *
	 * @param classNameRegistryRepository
	 *            repository of class name registries
	 * @param charset
	 *            to be used when interpreting text files
	 *
	 * @since 1.14
	 */
	public DatFileToRecordStage(final ClassNameRegistryRepository classNameRegistryRepository, final String charset) {
		super();
		this.classNameRegistryRepository = classNameRegistryRepository;
		this.charset = charset;
	}

	@Override
	protected void execute(final File textFile) {
		final ClassNameRegistry classNameRegistry = this.classNameRegistryRepository.get(textFile.getParentFile());
		try {
			final FileInputStream inputStream = new FileInputStream(textFile);
			if (inputStream != null) {
				this.processInputChannel(classNameRegistry, inputStream);
				inputStream.close();
			}
		} catch (final FileNotFoundException e) {
			this.logger.error("", e);
		} catch (final IOException e) {
			this.logger.error("", e);
		} catch (final MappingException e) {
			this.logger.error("", e);
		} catch (final MonitoringRecordException e) {
			this.logger.error("", e);
		} catch (final UnknownRecordTypeException e) {
			this.logger.error("", e);
		}
	}

	/**
	 * Process an input stream.
	 *
	 * @param classNameRegistry
	 *            class registry to be used
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
	private void processInputChannel(final ClassNameRegistry classNameRegistry, final InputStream inputStream)
			throws IOException, MappingException, MonitoringRecordException, UnknownRecordTypeException {
		final byte[] buffer = new byte[DatFileToRecordStage.BUFFER_SIZE];

		boolean endOfFile = false;
		int offset = 0;
		while (!endOfFile) {
			final int numOfReadBytes = inputStream.read(buffer, offset, DatFileToRecordStage.BUFFER_SIZE - offset);
			if (numOfReadBytes == -1) { /** end of line. */
				endOfFile = true;
			} else {
				final int numOfBufferedBytes = numOfReadBytes + offset;
				final int mark = this.processBuffer(classNameRegistry, buffer, offset, numOfBufferedBytes);
				/** move remaining left. */
				for (int j = mark + 1; j < numOfBufferedBytes; j++) {
					buffer[j - mark - 1] = buffer[j];
				}
				offset = numOfBufferedBytes - mark;
			}
		}
		if (this.charBuffer.position() > 0) {
			this.createRecord(classNameRegistry);
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
	private int processBuffer(final ClassNameRegistry classNameRegistry, final byte[] buffer, final int offset,
			final int numOfBufferedBytes)
					throws MappingException, MonitoringRecordException, UnknownRecordTypeException {
		int i = offset;
		int mark = 0;
		while (i < numOfBufferedBytes) {
			final char ch = (char) buffer[i];
			if (ch == '\n') {
				this.createRecord(classNameRegistry);
				mark = i;
			} else if (ch == '\r') {
				if (buffer[i + 1] == '\n') {
					i++;
				}
				this.createRecord(classNameRegistry);
				mark = i;
			} else {
				this.charBuffer.append(ch);
			}
			i++;
		}

		return mark;
	}

	private void createRecord(final ClassNameRegistry classNameRegistry)
			throws MappingException, MonitoringRecordException, UnknownRecordTypeException {
		this.charBuffer.flip();
		final char lead = this.charBuffer.get();
		if (lead == '$') {
			final TextValueDeserializer deserializer = TextValueDeserializer.create(this.charBuffer);
			final int id = deserializer.getInt();
			final String classname = classNameRegistry.get(id);
			if (classname == null) {
				throw new MappingException("Missing classname mapping for record type id " + "'" + id + "'");
			}
			final long loggingTimestamp = deserializer.getLong();
			final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.recordFactories.get(classname);
			final IMonitoringRecord event = recordFactory.create(deserializer);
			event.setLoggingTimestamp(loggingTimestamp);
			this.outputPort.send(event);
			this.charBuffer.clear();
		}
	}

	public String getCharset() {
		return this.charset;
	}

}
