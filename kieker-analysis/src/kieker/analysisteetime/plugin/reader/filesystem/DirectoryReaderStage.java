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
package kieker.analysisteetime.plugin.reader.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import kieker.analysis.plugin.reader.depcompression.AbstractDecompressionFilter;
import kieker.analysis.plugin.reader.depcompression.NoneDecompressionFilter;
import kieker.analysis.plugin.reader.util.FSReaderUtil;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.registry.reader.ReaderRegistry;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * Read a kieker log directory. The filter receives a directory as input and
 * outputs all events collected in the directory.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public class DirectoryReaderStage extends AbstractConsumerStage<File> {

	private final FilenameFilter mapFilter = new MapFileFilter();
	private final OutputPort<IMonitoringRecord> outputPort = this.createOutputPort(IMonitoringRecord.class);
	private final Configuration configuration;

	/**
	 * Create a new directory reader stage.
	 *
	 * @param configuration configuration for the stage and its plugins
	 */
	public DirectoryReaderStage(final Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	protected void execute(final File directory) {
		final ReaderRegistry<String> registry = new ReaderRegistry<>();
		/** read all map files. */
		final File[] mapFiles = directory.listFiles(this.mapFilter);
		if (mapFiles == null) {
			this.logger.error("{} is not a proper directory.", directory.getAbsolutePath());
		} else {
			for (final File mapFile : mapFiles) {
				final String mapFileName = mapFile.getName();
				try {
					this.readMapFile(new FileInputStream(mapFile), mapFileName, registry);
				} catch (final FileNotFoundException e) {
					this.logger.error("Cannot find map file {}.", mapFileName);
				}
			}

			/** read log files. */
			for (final File logFile : directory.listFiles()) {
				final String logFileName = logFile.getName();

				try {
					this.readLogFile(new FileInputStream(logFile), logFileName, registry);
				} catch (final FileNotFoundException e) {
					this.logger.error("Cannot find log file {}.", logFileName);
				}

			}
		}
	}

	/**
	 * Read a map file stream and initialize the registry.
	 *
	 * @param inputStream the input stream
	 * @param logFileName the name of the log file used for user feedback
	 * @param registry string registry
	 */
	private void readMapFile(final InputStream inputStream, final String mapFileName, final ReaderRegistry<String> registry) {
		final Class<? extends AbstractDecompressionFilter> decompressionClass = FSReaderUtil.findDecompressionFilterByExtension(mapFileName);

		/** detecting correct map file deserializer. */
		final Class<? extends AbstractMapDeserializer> deserializerClass;
		if (decompressionClass.equals(NoneDecompressionFilter.class)) {
			deserializerClass = FSReaderUtil.findMapDeserializer(mapFileName);
		} else {
			final String baseName = mapFileName.substring(0, mapFileName.lastIndexOf('.') - 1);
			deserializerClass = FSReaderUtil.findMapDeserializer(baseName);
		}

		try {
			final AbstractDecompressionFilter decompressionFilter = decompressionClass.getConstructor(Configuration.class).newInstance(this.configuration);
			final AbstractMapDeserializer deserializer = deserializerClass.getConstructor(Configuration.class).newInstance(this.configuration);
			deserializer.processDataStream(decompressionFilter.chainInputStream(inputStream), registry, mapFileName);
		} catch (final IOException ex) {
			this.logger.error("Reading map file {} failed.", mapFileName);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			this.logger.error("Cannot instantiate filter {} for decompression.", decompressionClass.getName());
		}
	}

	/**
	 * Read a log file stream and produce Kieker events.
	 *
	 * @param inputStream the input stream
	 * @param logFileName the name of the log file used for user feedback
	 * @param registry string registry
	 */
	private void readLogFile(final InputStream inputStream, final String logFileName, final ReaderRegistry<String> registry) {
		final Class<? extends AbstractDecompressionFilter> decompressionClass = FSReaderUtil.findDecompressionFilterByExtension(logFileName);

		/** detecting correct log file deserializer. */
		final Class<? extends AbstractEventDeserializer> deserializerClass;
		if (decompressionClass.equals(NoneDecompressionFilter.class)) {
			deserializerClass = FSReaderUtil.findEventDeserializer(logFileName);
		} else {
			final String baseName = logFileName.substring(0, logFileName.lastIndexOf('.') - 1);
			deserializerClass = FSReaderUtil.findEventDeserializer(baseName);
		}

		if (deserializerClass != null) {
			try {
				final AbstractDecompressionFilter decompressionFilter = decompressionClass.getConstructor(Configuration.class).newInstance(this.configuration);
				final AbstractEventDeserializer deserializer =
						deserializerClass.getConstructor(Configuration.class, ReaderRegistry.class).newInstance(this.configuration, registry);
				deserializer.processDataStream(decompressionFilter.chainInputStream(inputStream), this.outputPort);
			} catch (final IOException e) {
				this.logger.error("Reading log file {} failed.", logFileName);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				this.logger.error("Cannot instantiate filter {} for decompression.", decompressionClass.getName());
			}
		} else {
			this.logger.debug("Skipping file {}, as the extension indicates that it is not a log file.", logFileName);
		}
	}

	public OutputPort<IMonitoringRecord> getOutputPort() {
		return this.outputPort;
	}

}
