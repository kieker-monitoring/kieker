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
 * Accepts Kieker file system monitoring logs.
 * 
 * @author Andre van Hoorn
 * 
 */
public class KiekerLogDirFilter implements FilenameFilter { // NOPMD (TestClassWithoutTestCases)
	public static final String LOG_DIR_PREFIX = "kieker-"; // TODO: do we have this constant in the FS Writer(s)?
	public static final String MAP_FILENAME = "kieker.map"; // TODO: do we have this constant in the FS Writer(s)?

	public KiekerLogDirFilter() {
		// empty default constructor
	}

	public boolean accept(final File dir, final String name) {
		if (!name.startsWith(KiekerLogDirFilter.LOG_DIR_PREFIX)) {
			return false;
		}

		final String potentialDirFn = dir.getAbsolutePath() + File.separatorChar + name;

		final File potentialDir = new File(potentialDirFn);

		if (!potentialDir.isDirectory()) {
			return false;
		}

		final String[] kiekerMapFiles = potentialDir.list(new FilenameFilter() {
			/**
			 * Accepts directories containing a `kieker.map` file.
			 */
			public boolean accept(final File dir, final String name) {
				return name.equals(KiekerLogDirFilter.MAP_FILENAME);
			}
		});
		return kiekerMapFiles.length == 1;
	}
}
