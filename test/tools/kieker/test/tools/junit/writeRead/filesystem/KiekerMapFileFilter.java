/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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
 * Accepts kieker.map files.
 * 
 * @author Andre van Hoorn
 * 
 */
class KiekerMapFileFilter implements FilenameFilter { // NOPMD (TestClassWithoutTestCases)
	public static final String MAP_FILENAME = "kieker.map"; // TODO: do we have this constant in the FS Writer(s)?

	public KiekerMapFileFilter() {
		// empty default constructor
	}

	/**
	 * Accepts the {@value #MAP_FILENAME} file in a monitoring log directory.
	 */
	public boolean accept(final File dir, final String name) {
		return MAP_FILENAME.equals(name);
	}
}
