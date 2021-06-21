/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.tt.reader.filesystem.format.binary.file;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import kieker.analysis.plugin.reader.depcompression.AbstractDecompressionFilter;
import kieker.analysis.plugin.reader.util.FSReaderUtil;
import kieker.analysis.tt.reader.filesystem.className.ClassNameRegistryRepository;
import kieker.common.configuration.Configuration;
import kieker.common.exception.MonitoringRecordException;
import kieker.common.record.IMonitoringRecord;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * @author Christian Wulf
 *
 * @since 1.10
 *
 * @deprecated since 1.15 removed 1.16
 */
@Deprecated
public class BinaryFile2RecordFilter extends AbstractConsumerStage<File> {

	private final OutputPort<IMonitoringRecord> outputPort = this.createOutputPort();

	private RecordFromBinaryFileCreator recordFromBinaryFileCreator;

	private ClassNameRegistryRepository classNameRegistryRepository;

	/**
	 * @since 1.10
	 */
	public BinaryFile2RecordFilter(final ClassNameRegistryRepository classNameRegistryRepository) {
		this();
		this.classNameRegistryRepository = classNameRegistryRepository;
	}

	/**
	 * @since 1.10
	 */
	public BinaryFile2RecordFilter() {
		super();
	}

	@Override
	public void onStarting() {
		super.onStarting();
		this.recordFromBinaryFileCreator = new RecordFromBinaryFileCreator(this.classNameRegistryRepository);
	}

	public ClassNameRegistryRepository getClassNameRegistryRepository() {
		return this.classNameRegistryRepository;
	}

	public void setClassNameRegistryRepository(final ClassNameRegistryRepository classNameRegistryRepository) {
		this.classNameRegistryRepository = classNameRegistryRepository;
	}

	@Override
	protected void execute(final File binaryFile) {
		final String name = binaryFile.getName();

		final Class<? extends AbstractDecompressionFilter> clazz = FSReaderUtil.findDecompressionFilterByExtension(name);

		try {
			final AbstractDecompressionFilter filter = clazz.getConstructor(Configuration.class).newInstance(new Configuration());
			final DataInputStream inputStream = new DataInputStream(filter.chainInputStream(Files.newInputStream(binaryFile.toPath(), StandardOpenOption.READ)));
			try {
				this.recordFromBinaryFileCreator.createRecordsFromBinaryFile(binaryFile, inputStream, this.outputPort);
			} catch (final MonitoringRecordException e) {
				this.logger.error("Error reading file: " + binaryFile, e);
			} finally {
				if (inputStream != null) {
					this.closeStream(inputStream);
				}
			}
		} catch (final IOException e) {
			this.logger.error("Error reading file: " + binaryFile, e);
		} catch (final IllegalArgumentException e) {
			this.logger.warn("Unknown file extension for file: " + binaryFile);
		} catch (final InstantiationException e1) {
			this.logger.error("Decompression filter instantiation failed. {}", e1);
		} catch (final IllegalAccessException e1) {
			this.logger.error("Decompression filter instantiation failed. {}", e1);
		} catch (final InvocationTargetException e1) {
			this.logger.error("Decompression filter instantiation failed. {}", e1);
		} catch (final NoSuchMethodException e1) {
			this.logger.error("Decompression filter instantiation failed. {}", e1);
		} catch (final SecurityException e1) {
			this.logger.error("Decompression filter instantiation failed. {}", e1);
		}
	}

	private void closeStream(final DataInputStream dataInputStream) {
		try {
			dataInputStream.close();
		} catch (final IOException ex) {
			this.logger.error("Exception while closing input stream for processing input file", ex);
		}
	}

	public OutputPort<IMonitoringRecord> getOutputPort() {
		return this.outputPort;
	}

}
