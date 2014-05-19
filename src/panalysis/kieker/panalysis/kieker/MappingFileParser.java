/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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
package kieker.panalysis.kieker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import kieker.common.logging.Log;
import kieker.common.util.filesystem.FSUtil;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class MappingFileParser {

	protected Log logger;
	protected Map<Integer, String> stringRegistry;

	public void parse(final InputStream inputStream) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(inputStream, FSUtil.ENCODING));
			String line;
			while ((line = in.readLine()) != null) { // NOPMD (assign)
				if (line.length() == 0) {
					continue; // ignore empty lines
				}
				final int split = line.indexOf('=');
				if (split == -1) {
					this.logger.error("Failed to find character '=' in line: {" + line + "}. It must consist of a ID=VALUE pair.");
					continue; // continue on errors
				}
				final String key = line.substring(0, split);
				final String value = FSUtil.decodeNewline(line.substring(split + 1));
				// the leading $ is optional
				final Integer id;
				try {
					id = Integer.valueOf((key.charAt(0) == '$') ? key.substring(1) : key); // NOCS
				} catch (final NumberFormatException ex) {
					this.logger.error("Error reading mapping file, id must be integer", ex);
					continue; // continue on errors
				}
				final String prevVal = this.stringRegistry.put(id, value);
				if (prevVal != null) {
					this.logger.error("Found addional entry for id='" + id + "', old value was '" + prevVal + "' new value is '" + value + "'");
				}
			}
		} catch (final IOException ex) {
			this.logger.error("Error reading mapping file", ex);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (final IOException ex) {
					this.logger.error("Exception while closing input stream for mapping file", ex);
				}
			}
		}
	}
}
