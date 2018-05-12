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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.plugin.reader.depcompression.AbstractDecompressionFilter;
import kieker.analysis.plugin.reader.util.FSReaderUtil;
import kieker.common.configuration.Configuration;
import kieker.common.registry.reader.ReaderRegistry;
import kieker.common.util.filesystem.FSUtil;

/**
 * Text-based mapfile reader.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public class TextMapReader extends AbstractMapReader {

	private final static Logger LOGGER = LoggerFactory.getLogger(TextMapReader.class);

	public TextMapReader() {
		super();
	}

	/* (non-Javadoc)
	 * @see kieker.analysisteetime.plugin.reader.filesystem.AbstractMapReader#readMapFile(java.io.File, kieker.common.registry.reader.ReaderRegistry)
	 */
	@Override
	public void readMapFile(final File mapFile, final ReaderRegistry<String> registry) {
		final Class<? extends AbstractDecompressionFilter> decompressionClass = FSReaderUtil.findDecompressionFilterByExtension(mapFile.getName());
		try {
			final Configuration configuration = new Configuration();
			final AbstractDecompressionFilter decompressionFilter = decompressionClass.getConstructor(Configuration.class).newInstance(configuration);
			this.processDataStream(decompressionFilter.chainInputStream(new FileInputStream(mapFile)), registry, mapFile.getName());
		} catch (final IOException e) {
			TextMapReader.LOGGER.error("Reading file {} failed.", mapFile.getName());
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			TextMapReader.LOGGER.error("Cannot instantiate filter {} for decompression.", decompressionClass.getName());
		}
	}

	private void processDataStream(final InputStream chainInputStream, final ReaderRegistry<String> registry, final String filePathName) {
		// found any kind of mapping file
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(chainInputStream, FSUtil.ENCODING));
			String line;
			while ((line = in.readLine()) != null) { // NOPMD (assign)
				if (line.length() == 0) {
					continue; // ignore empty lines
				}
				LOGGER.debug("Read line: {}", line);

				final int split = line.indexOf('=');
				if (split == -1) {
					LOGGER.error("Failed to parse line: {} from file {}. Each line must contain ID=VALUE pairs.", line, filePathName);
					continue; // continue on errors
				}
				final String key = line.substring(0, split);
				final String value = FSUtil.decodeNewline(line.substring(split + 1));
				// the leading $ is optional
				final Integer id;
				try {
					id = Integer.valueOf((key.charAt(0) == '$') ? key.substring(1) : key); // NOCS
				} catch (final NumberFormatException ex) {
					LOGGER.error("Error reading mapping file, id must be integer", ex);
					continue; // continue on errors
				}
				final String prevVal = registry.register(id, value);
				if (prevVal != null) {
					LOGGER.error("Found addional entry for id='{', old value was '{}' new value is '{}'", id, prevVal, value);
				}
			}
		} catch (final IOException e) {
			LOGGER.error("Error reading {}", filePathName, e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (final IOException ex) {
					LOGGER.error("Exception while closing input stream for mapping file", ex);
				}
			}
		}
	}

}
