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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;
import kieker.common.registry.reader.ReaderRegistry;
import kieker.common.util.filesystem.FSUtil;

/**
 * Reader for text-based mapfiles. The accepted file format is a number on the left side and a escaped string on the right side terminated by a newline.
 * Both parts are separated by a equal sign (=). The number can be prefixed by a dollar sign ($). Format regex ^\$?[0-9]+=\a$
 *
 * In case the mapfile is compressed with one of the supported compression formats, it will be decompressed before interpretation.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public class TextMapDeserializer extends AbstractMapDeserializer {

	private static final Logger LOGGER = LoggerFactory.getLogger(TextMapDeserializer.class);

	/**
	 * Create a new map file deserializer.
	 *
	 * @param configuration configuration of the text deserializer.
	 */
	public TextMapDeserializer(final Configuration configuration) {
		super(configuration);
	}

	/* (non-Javadoc)
	 * @see kieker.analysisteetime.plugin.reader.filesystem.AbstractMapReader#processDataStream(java.io.File, kieker.common.registry.reader.ReaderRegistry)
	 */
	@Override
	public void processDataStream(final InputStream inputStream, final ReaderRegistry<String> registry, final String mapFileName) {
		// found any kind of mapping file
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(inputStream, FSUtil.ENCODING));
			String line;
			while ((line = in.readLine()) != null) { // NOPMD (assign)
				if (line.length() == 0) {
					continue; // ignore empty lines
				}
				LOGGER.debug("Read line: {}", line);

				final int split = line.indexOf('=');
				if (split == -1) {
					LOGGER.error("Failed to parse line: {} from file {}. Each line must contain ID=VALUE pairs.", line, mapFileName);
					continue; // continue on errors
				} else {
					this.registerEntry(registry, line.substring(0, split), line.substring(split + 1));
				}
			}
		} catch (final IOException e) {
			LOGGER.error("Error reading {}", mapFileName, e);
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

	private void registerEntry(final ReaderRegistry<String> registry, final String key, final String value) {
		// the leading $ is optional
		final Integer id;
		try {
			id = Integer.valueOf((key.charAt(0) == '$') ? key.substring(1) : key); // NOCS
		} catch (final NumberFormatException ex) {
			LOGGER.error("Error reading mapping file, id must be integer", ex);
			return; // continue on errors
		}
		final String prevVal = registry.register(id, FSUtil.decodeNewline(value));
		if (prevVal != null) {
			LOGGER.error("Found addional entry for id='{', old value was '{}' new value is '{}'", id, prevVal, value);
		}
	}

}
