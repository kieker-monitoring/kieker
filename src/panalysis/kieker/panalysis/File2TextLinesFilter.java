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

package kieker.panalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import kieker.panalysis.base.AbstractFilter;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class File2TextLinesFilter extends AbstractFilter<File2TextLinesFilter.INPUT_PORT, File2TextLinesFilter.OUTPUT_PORT> {

	static public enum INPUT_PORT {
		FILE
	}

	static public enum OUTPUT_PORT {
		TEXT_LINE
	}

	private String charset; // = "UTF-8"

	public File2TextLinesFilter() {
		super(INPUT_PORT.class, OUTPUT_PORT.class);
	}

	public void execute() {
		final File file = (File) this.take(INPUT_PORT.FILE);

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), this.charset));
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.length() != 0) {
					this.put(OUTPUT_PORT.TEXT_LINE, line);
				} // else ignore empty line
			}
		} catch (final FileNotFoundException e) {
			this.logger.error("", e);
		} catch (final IOException e) {
			this.logger.error("", e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (final IOException e) {
				this.logger.warn("", e);
			}
		}
	}
}
