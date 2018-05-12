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
 * Read a kieker log directory. The directory is received as input.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public class DirectoryReaderStage extends AbstractConsumerStage<File> {

	private final AbstractMapReader mapReader;
	private final FilenameFilter mapFilter = new MapFileFilter();
	private final OutputPort<IMonitoringRecord> outputPort = this.createOutputPort(IMonitoringRecord.class);
	private final Configuration configuration;

	public DirectoryReaderStage(final AbstractMapReader mapReader, final Configuration configuration) {
		this.mapReader = mapReader;
		this.configuration = configuration;
	}

	@Override
	protected void execute(final File directory) {
		final ReaderRegistry<String> registry = new ReaderRegistry<>();
		/** read all map files. */
		for (final File mapFile : directory.listFiles(this.mapFilter)) {
			this.mapReader.readMapFile(mapFile, registry);
		}

		/** read log files. */
		for (final File logFile : directory.listFiles()) {
			final String name = logFile.getName();

			try {
				this.readLogFile(new FileInputStream(logFile), name);
			} catch (final FileNotFoundException e) {
				this.logger.error("Cannot find log file {}.", name);
			}

		}
	}

	private void readLogFile(final InputStream inputStream, final String name) {
		final Class<? extends AbstractDecompressionFilter> decompressionClass = FSReaderUtil.findDecompressionFilterByExtension(name);

		final Class<? extends IEventDeserializer> deserializerClass;
		if (decompressionClass.equals(NoneDecompressionFilter.class)) {
			deserializerClass = FSReaderUtil.findEventDeserializer(name);
		} else {
			final String baseName = name.substring(0, name.lastIndexOf('.')-1);
			deserializerClass = FSReaderUtil.findEventDeserializer(baseName);
		}

		try {
			final AbstractDecompressionFilter decompressionFilter = decompressionClass.getConstructor(Configuration.class).newInstance(this.configuration);
			final IEventDeserializer deserializer = deserializerClass.getConstructor(Configuration.class).newInstance(this.configuration);
			deserializer.processDataStream(decompressionFilter.chainInputStream(inputStream), this.outputPort);
		} catch (final IOException e) {
			this.logger.error("Reading file {} failed.", name);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			this.logger.error("Cannot instantiate filter {} for decompression.", decompressionClass.getName());
		}
	}

	public OutputPort<IMonitoringRecord> getOutputPort() {
		return this.outputPort;
	}

}
