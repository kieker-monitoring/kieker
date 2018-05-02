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

package kieker.test.tools.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Assert;

/**
 * @author Andre van Hoorn, Jan Waller
 *
 * @since 1.7
 */
public final class StringUtils {

	private static final String ENCODING = "UTF-8";

	/**
	 * Private constructor to avoid instantiation.
	 */
	private StringUtils() {
		// empty private constructor
	}

	/**
	 * A simple helper method reading the given file's content as a string (UTF-8).
	 *
	 * @param outputFile
	 *            The file to read.
	 *
	 * @return A string representation of the file's content.
	 *
	 * @throws IOException
	 *             If something went wrong during the reading.
	 */
	public static final String readOutputFileAsString(final File outputFile) throws IOException {
		final byte[] buffer = new byte[(int) outputFile.length()];
		BufferedInputStream f = null;
		try {
			f = new BufferedInputStream(new FileInputStream(outputFile));
			if (f.read(buffer) == -1) {
				Assert.fail("Failed to read file into buffer: " + outputFile.getAbsolutePath());
			}
		} finally {
			if (f != null) {
				f.close();
			}
		}
		return new String(buffer, ENCODING);
	}

}
