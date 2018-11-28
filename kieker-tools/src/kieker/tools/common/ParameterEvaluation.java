/***************************************************************************
 * Copyright (C) 2018 Kieker Project (https://kieker-monitoring.net)
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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;

/**
 * Collection of command line parameter evaluation functions.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 *
 */
public final class ParameterEvaluation {

	private static final Logger LOGGER = LoggerFactory.getLogger(ParameterEvaluation.class);

	private ParameterEvaluation() {
		// private constructor for utility class
	}

	/**
	 * Check whether the given handle refers to an existing directory.
	 *
	 * @param location
	 *            path to the directory
	 * @param label
	 *            noun or phrase indicating the parameter of the given location, e.g., "input directory"
	 * @param commander
	 *            command line handler used to plot the usage message
	 *
	 * @return returns true if the directory exists, else false
	 */
	public static boolean checkDirectory(final File location, final String label, final JCommander commander) {
		if (location == null) {
			ParameterEvaluation.LOGGER.error("{} path not specified.", label);
			commander.usage();
			return false;
		}
		try {
			if (!location.exists()) {
				ParameterEvaluation.LOGGER.error("{} path {} does not exist.", label,
						location.getCanonicalPath());
				commander.usage();
				return false;
			}
			if (!location.isDirectory()) {
				ParameterEvaluation.LOGGER.error("{} path {} is not a directory.", label,
						location.getCanonicalPath());
				commander.usage();
				return false;
			}

			return true;
		} catch (final IOException e) {
			ParameterEvaluation.LOGGER.error("{} path {} cannot be checked. Cause {}", label, location, e.getLocalizedMessage());
			return false;
		}
	}

	/**
	 * Check whether a specified file is readable.
	 *
	 * @param file
	 *            the file handle
	 * @param label
	 *            noun or phrase indicating the parameter of the given location, e.g., "configuration file"
	 * @param commander
	 *            command line handler used to plot the usage message
	 *
	 * @return true when the file is readable
	 */
	public static boolean isFileReadable(final File file, final String label, final JCommander commander) {
		if (file == null) {
			ParameterEvaluation.LOGGER.error("{} path not specified.", label);
			commander.usage();
			return false;
		}
		try {
			if (!file.exists()) {
				ParameterEvaluation.LOGGER.error("{} {} does not exist.", label, file.getCanonicalPath());
				return false;
			}
			if (!file.isFile()) {
				ParameterEvaluation.LOGGER.error("{} {} is not a file.", label, file.getCanonicalPath());
				return false;
			}
			if (!file.canRead()) {
				ParameterEvaluation.LOGGER.error("{} {} cannot be read.", label, file.getCanonicalPath());
				return false;
			}

			return true;
		} catch (final IOException e) {
			ParameterEvaluation.LOGGER.error("{} {} cannot be checked. Cause {}", label, file, e.getLocalizedMessage());
			return false;
		}
	}

	/**
	 * Create an URL from a given string.
	 *
	 * @param urlString
	 *            the url string
	 * @param label
	 *            label used to indicate which string
	 * @return returns an URL or null on error
	 */
	public static URL createURL(final String urlString, final String label) {
		try {
			return new URL(urlString);
		} catch (final MalformedURLException e) {
			ParameterEvaluation.LOGGER.error("{} Malformend URL {}", label, urlString);
			return null;
		}

	}
}
