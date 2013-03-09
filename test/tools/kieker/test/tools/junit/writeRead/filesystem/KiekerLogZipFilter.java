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
 * Accepts Kieker file system monitoring logs.
 * 
 * @author Jan Waller
 * 
 * @since 1.7
 */
public class KiekerLogZipFilter implements FilenameFilter { // NOPMD (TestClassWithoutTestCases)
	public static final String LOG_ZIP_PREFIX = "kieker-"; // TODO: do we have this constant in the FS Writer(s)?
	public static final String LOG_ZIP_FILE_EXTENSION = ".zip"; // TODO: do we have this constant in the FS Writer(s)?

	public KiekerLogZipFilter() {
		// empty default constructor
	}

	public boolean accept(final File dir, final String name) {
		if (!dir.isDirectory() || !name.startsWith(KiekerLogZipFilter.LOG_ZIP_PREFIX) || !name.endsWith(LOG_ZIP_FILE_EXTENSION)) {
			return false;
		}
		final String potentialFn = dir.getAbsolutePath() + File.separatorChar + name;
		final File potentialFile = new File(potentialFn);
		if (!potentialFile.isFile()) {
			return false;
		}
		return true;
	}
}
