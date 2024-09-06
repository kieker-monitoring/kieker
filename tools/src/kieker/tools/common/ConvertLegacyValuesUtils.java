/***************************************************************************
 * Copyright 2023 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.common;

import java.io.File;
import java.util.List;

/**
 * This utility class is used to support gluing old code to new infrastructure.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public final class ConvertLegacyValuesUtils {

	/**
	 * Do not instantiate utility class.
	 */
	private ConvertLegacyValuesUtils() {
		// nothing to do here
	}

	/**
	 * JCommander and modern service infrastructure passes file properties as files, but old filters expect them as strings.
	 * This helper allows to convert file lists to string arrays.
	 *
	 * @param directories
	 *            input directories
	 * @return string array containing all path names
	 */
	public static String[] fileListToStringArray(final List<File> directories) {
		final String[] array = new String[directories.size()];

		int i = 0;
		for (final File directory : directories) {
			array[i++] = directory.getAbsolutePath();
		}

		return array;
	}

	/**
	 * Convert a list of strings into an array of strings.
	 *
	 * @param list
	 *            list of strings
	 * @return returns an array of strings
	 */
	public static String[] listToArray(final List<String> list) {
		return list.toArray(new String[0]);
	}

}
