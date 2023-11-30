/***************************************************************************
 * Copyright (C) 2021 OceanDSL (https://oceandsl.uni-kiel.de)
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
package kieker.analysis.generic;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Read a 2+n column file into a map. First column represents the key, all other the parameter of
 * the value.
 *
 * @author Reiner Jung
 * @since 1.1
 *
 * @param <T>
 *            key value class
 * @param <R>
 *            value value class
 */
public class MapFileReader<T, R> {

	private static final Logger LOGGER = LoggerFactory.getLogger(MapFileReader.class);

	private final BufferedReader reader;
	private final Map<T, R> map;
	private final String separator;

	private final IValueConverter<T> keyConverter;

	private final IValueConverter<R> valueConverter;

	public MapFileReader(final Path mapFilePath, final String separator, final Map<T, R> map,
			final IValueConverter<T> keyConverter, final IValueConverter<R> valueConverter) throws IOException {
		this.reader = Files.newBufferedReader(mapFilePath);
		this.separator = separator;
		this.keyConverter = keyConverter;
		this.valueConverter = valueConverter;
		this.map = map;
	}

	public void read() throws IOException {
		String line;
		while ((line = this.reader.readLine()) != null) {
			final String[] values = line.split(this.separator);
			if (values.length >= 2) {
				for (int i = 0; i < values.length; i++) {
					values[i] = values[i].trim();
				}
				this.map.put(this.keyConverter.getColumnValue(values), this.valueConverter.getColumnValue(values));
			} else {
				MapFileReader.LOGGER.error("Entry incomplete '{}'", line.trim()); // NOPMD no guard
																					// necessary
			}
		}
		this.reader.close();
	}
}
