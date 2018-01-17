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

package kieker.test.toolsteetime.junit.writeRead.filesystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Assert;

import kieker.common.util.filesystem.FSUtil;

import kieker.test.tools.util.StringUtils;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
final class FileContentUtil {

	private FileContentUtil() {
		// utility class
	}

	/**
	 * Replaces the given search String by the given replacement String in all given files.
	 *
	 * @param dirs
	 *            The directories containing the files in question.
	 * @param findString
	 *            The string to search for.
	 * @param replaceByString
	 *            The string that will be used as a substitution.
	 *
	 * @throws IOException
	 *             If something during the file accesses went wrong.
	 */
	public static void replaceStringInMapFiles(final String[] dirs, final String findString, final String replaceByString) throws IOException {
		for (final String curLogDir : dirs) {
			final String[] mapFilesInDir = new File(curLogDir).list(new KiekerMapFileFilter());
			Assert.assertNotNull(mapFilesInDir);
			Assert.assertEquals("Unexpected number of map files", 1, mapFilesInDir.length);

			final String curMapFile = curLogDir + File.separator + mapFilesInDir[0];

			FileContentUtil.searchReplaceInFile(curMapFile, findString, replaceByString);
		}

	}

	/**
	 * Replaces the given search String by the given replacement String in the given file.
	 *
	 * @param filename
	 *            The name of the file to be modified.
	 * @param findString
	 *            The string to search for.
	 * @param replaceByString
	 *            The string that will be used as a substitution.
	 *
	 * @throws IOException
	 *             If something during the file access went wrong.
	 */
	private static void searchReplaceInFile(final String filename, final String findString, final String replaceByString) throws IOException {
		final String mapFileContent = StringUtils.readOutputFileAsString(new File(filename));
		final String manipulatedContent = mapFileContent.replaceAll(findString, replaceByString);
		PrintStream printStream = null;
		try {
			printStream = new PrintStream(new FileOutputStream(filename), false, FSUtil.ENCODING);
			printStream.print(manipulatedContent);
		} finally {
			if (printStream != null) {
				printStream.close();
			}
		}
	}
}
