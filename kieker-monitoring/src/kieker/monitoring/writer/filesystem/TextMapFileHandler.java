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
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.controller.ControllerFactory;
import kieker.monitoring.core.controller.ReceiveUnfilteredConfiguration;
import kieker.monitoring.writer.filesystem.compression.ICompressionFilter;
import kieker.monitoring.writer.filesystem.compression.NoneCompressionFilter;

/**
 * Handler for the map file used in Kieker.
 * Note: This version does not support compression for the map file right now.
 *
 * @author Reiner Jung
 *
 * @since 1.14
 */
@ReceiveUnfilteredConfiguration
public class TextMapFileHandler implements IMapFileHandler {

	public static final String PREFIX = TextMapFileHandler.class.getName() + ".";
	/** The name of the configuration key to select a compression for the record log files. */
	public static final String CONFIG_COMPRESSION_FILTER = PREFIX + "compression";
	/** The name of the configuration determining whether to flush upon each incoming registry entry. */
	public static final String CONFIG_FLUSH_MAPFILE = PREFIX + "flush";
	/** The name of the configuration key determining the buffer size of the output file stream. */
	public static final String CONFIG_BUFFERSIZE = PREFIX + "bufferSize";

	private final ICompressionFilter compressionFilter;
	private PrintWriter printWriter;
	private final boolean flushMapFile;

	public TextMapFileHandler(final Configuration configuration) {
		/** get compression filter main data. */
		final String compressionFilterClassName = configuration.getStringProperty(CONFIG_COMPRESSION_FILTER, NoneCompressionFilter.class.getName());
		this.compressionFilter = ControllerFactory.getInstance(configuration).createAndInitialize(ICompressionFilter.class,
				compressionFilterClassName, configuration);
		this.flushMapFile = configuration.getBooleanProperty(CONFIG_FLUSH_MAPFILE, true);
	}

	@Override
	public void create(final Path location, final Charset charset) {
		try {
			final Writer w = Files.newBufferedWriter(location, charset, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
			this.printWriter = new PrintWriter(w);
		} catch (final IOException e) {
			throw new IllegalStateException("Error on creating Kieker's mapping file.", e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see kieker.monitoring.writer.filesystem.IMapFileHandler#close()
	 */
	@Override
	public void close() {
		this.printWriter.close();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see kieker.monitoring.writer.filesystem.IMapFileHandler#add(int, java.lang.String)
	 */
	@Override
	public void add(final int id, final String eventClassName) {
		this.printWriter.print('$');
		this.printWriter.print(id);
		this.printWriter.print('=');
		this.printWriter.print(eventClassName);
		this.printWriter.println();

		if (this.flushMapFile) {
			this.printWriter.flush();
		}
	}

}
