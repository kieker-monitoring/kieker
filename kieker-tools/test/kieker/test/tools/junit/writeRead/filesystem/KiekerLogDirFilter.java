/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

import kieker.common.util.filesystem.FSUtil;

/**
 * Accepts Kieker file system monitoring logs.
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.6
 */
public class KiekerLogDirFilter implements FilenameFilter { // NOPMD (TestClassWithoutTestCases)

	/**
	 * Default constructor.
	 */
	public KiekerLogDirFilter() {
		// empty default constructor
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean accept(final File dir, final String name) {
		if (!name.startsWith(FSUtil.FILE_PREFIX)) {
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
			@Override
			public boolean accept(final File dir, final String name) {
				return name.equals(FSUtil.MAP_FILENAME);
			}
		});
		return kiekerMapFiles.length == 1;
	}
}
