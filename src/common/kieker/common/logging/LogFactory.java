/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.common.logging;

/**
 * @author Jan Waller
 */
public final class LogFactory { // NOPMD (Implementation of an logger)

	public static final String CUSTOM_LOGGER_JVM = "kieker.common.logging.Log";

	private static final Logger DETECTED_LOGGER = LogFactory.detectLogger();

	private static enum Logger {
		JDK, COMMONS, WEBGUI,
	}

	// static {
	// System.out.println(DETECTED_LOGGER.toString());
	// }

	private LogFactory() {
		// Nothing to do
	}

	public static final Log getLog(final Class<?> clazz) {
		return LogFactory.getLog(clazz.getName());
	}

	public static final Log getLog(final String name) {
		switch (DETECTED_LOGGER) { // NOPMD (no break needed)
		case WEBGUI:
			return new LogImplWebguiLogging(name);
		case COMMONS:
			return new LogImplCommonsLogging(name);
		case JDK:
		default:
			return new LogImplJDK14(name);
		}
	}

	private static final Logger detectLogger() {
		final String systemPropertyLogger = System.getProperty(CUSTOM_LOGGER_JVM);
		if (null != systemPropertyLogger) {
			final String strLogger = systemPropertyLogger.trim().toUpperCase();
			try {
				return Enum.valueOf(Logger.class, strLogger);
			} catch (final IllegalArgumentException ex) { // NOPMD NOCS
				// TODO: How to notify of incorrectly chosen logger?
			}
		}
		try {
			if (Class.forName("org.apache.commons.logging.Log") != null) {
				return Logger.COMMONS; // use commons logging
			}
		} catch (final Exception ex) { // NOPMD NOCS (catch Exception)
			// use default in case of errors ...
		}
		return Logger.JDK;
	}
}
