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

import java.util.Locale;

/**
 * @author Jan Waller
 */
public final class LogFactory { // NOPMD (Implementation of an logger)

	public static final String CUSTOM_LOGGER_JVM = "kieker.common.logging.Log";

	private static final String JVM_LOGGER;
	private static final Logger DETECTED_LOGGER;

	private static enum Logger {
		NONE, JDK, COMMONS, WEBGUI, JUNIT,
	}

	static {
		final String systemPropertyLogger = System.getProperty(CUSTOM_LOGGER_JVM);
		if (null != systemPropertyLogger) {
			JVM_LOGGER = systemPropertyLogger.trim().toUpperCase(Locale.US);
		} else {
			JVM_LOGGER = null;
		}
		DETECTED_LOGGER = LogFactory.detectLogger();
		final Log log = LogFactory.getLog(LogFactory.class);
		if ((null != JVM_LOGGER) && !DETECTED_LOGGER.name().equals(JVM_LOGGER)) {
			log.warn("Failed to load Logger with property " + CUSTOM_LOGGER_JVM + "=" + JVM_LOGGER + ", using " + DETECTED_LOGGER.name() + " instead.");
		}
		// System.out.println(DETECTED_LOGGER.toString());
		if (log.isDebugEnabled()) {
			log.debug(DETECTED_LOGGER.toString());
		}
	}

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
		case NONE:
			return new LogImplNone(name);
		case JUNIT:
			return new LogImplJUnit(name);
		case JDK:
		default:
			return new LogImplJDK14(name);
		}
	}

	private static final Logger detectLogger() {
		if (null != JVM_LOGGER) {
			try {
				return Enum.valueOf(Logger.class, JVM_LOGGER);
			} catch (final IllegalArgumentException ex) { // NOPMD NOCS
				// Notify is handled above.
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
