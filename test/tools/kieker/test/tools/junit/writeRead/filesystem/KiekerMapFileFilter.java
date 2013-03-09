/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.junit.writeRead.filesystem;

import java.io.File;
import java.io.FilenameFilter;

/**
 * This filter accepts only kieker.map files. All other files are declined.
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.6
 */
class KiekerMapFileFilter implements FilenameFilter { // NOPMD (TestClassWithoutTestCases)

	/** This constant determines the name of the kieker.map file. */
	public static final String MAP_FILENAME = "kieker.map"; // TODO: do we have this constant in the FS Writer(s)?

	/**
	 * Creates a new instance of this class.
	 */
	public KiekerMapFileFilter() {
		// empty default constructor
	}

	/**
	 * Accepts the {@value #MAP_FILENAME} file in a monitoring log directory.
	 * 
	 * @param dir
	 *            The directory of the file to be checked - it is currently not really used.
	 * @param name
	 *            the name of the file to be checked.
	 * 
	 * @return true if and only if the given file name is equals to the name of a kieker.map file.
	 */
	public boolean accept(final File dir, final String name) {
		return MAP_FILENAME.equals(name);
	}
}
