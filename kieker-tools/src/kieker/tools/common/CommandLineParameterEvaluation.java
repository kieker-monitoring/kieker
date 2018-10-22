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
 * @since 0.0.3
 *
 */
public final class CommandLineParameterEvaluation {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommandLineParameterEvaluation.class);

	private CommandLineParameterEvaluation() {
		// private constructor for utility class
	}

	/**
	 * Check whether the given handle refers to an existing directory.
	 *
	 * @param location
	 *            path to the directory
	 * @param locationLabel
	 *            label indicating the parameter of the given location
	 * @param commander
	 *            command line handler
	 *
	 * @return returns true if the directory exists, else false
	 * @throws IOException
	 *             on io error
	 */
	public static boolean checkDirectory(final File location, final String locationLabel, final JCommander commander)
			throws IOException {
		if (location == null) {
			CommandLineParameterEvaluation.LOGGER.error("{} path not specified.", locationLabel);
			commander.usage();
			return false;
		}
		if (!location.exists()) {
			CommandLineParameterEvaluation.LOGGER.error("{} path {} does not exist.", locationLabel,
					location.getCanonicalPath());
			commander.usage();
			return false;
		}
		if (!location.isDirectory()) {
			CommandLineParameterEvaluation.LOGGER.error("{} path {} is not a directory.", locationLabel,
					location.getCanonicalPath());
			commander.usage();
			return false;
		}

		return true;
	}

	/**
	 * Check whether a specified file is readable.
	 *
	 * @param file
	 *            the file handle
	 * @param label
	 *            label indicating the parameter of the given location
	 * @return true on success else false
	 * @throws IOException
	 *             on io error
	 */
	public static boolean isFileReadable(final File file, final String label) throws IOException {
		if (!file.exists()) {
			if (CommandLineParameterEvaluation.LOGGER.isErrorEnabled()) {
				CommandLineParameterEvaluation.LOGGER.error(label + " " + file.getCanonicalPath() + " does not exist.");
			}
			return false;
		}
		if (!file.isFile()) {
			if (CommandLineParameterEvaluation.LOGGER.isErrorEnabled()) {
				CommandLineParameterEvaluation.LOGGER.error(label + " " + file.getCanonicalPath() + " is not a file.");
			}
			return false;
		}
		if (!file.canRead()) {
			if (CommandLineParameterEvaluation.LOGGER.isErrorEnabled()) {
				CommandLineParameterEvaluation.LOGGER.error(label + " " + file.getCanonicalPath() + " cannot be read.");
			}
			return false;
		}

		return true;
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
			if (CommandLineParameterEvaluation.LOGGER.isErrorEnabled()) {
				CommandLineParameterEvaluation.LOGGER.error(label + " Malformend URL " + urlString);
			}
			return null;
		}

	}
}
