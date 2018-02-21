/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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
 * This factory class should be used to get new logger instances. It initializes the logging system based on the available classes.
 * 
 * @author Jan Waller
 * 
 * @since 1.5
 */
public final class OLDLogFactory { // NOPMD (Implementation of an logger)

	/** This constant should point to the fully qualified class name of the custom JVM logger. */
	public static final String CUSTOM_LOGGER_JVM = "kieker.common.logging.Log";

	private static final String JVM_LOGGER;
	private static final Logger DETECTED_LOGGER;

	private static enum Logger {
		NONE, JDK, COMMONS, SLF4J, WEBGUI, JUNIT,
	}

	static {
		final String systemPropertyLogger = System.getProperty(CUSTOM_LOGGER_JVM);
		if (null != systemPropertyLogger) {
			JVM_LOGGER = systemPropertyLogger.trim().toUpperCase(Locale.US);
		} else {
			JVM_LOGGER = null;
		}
		DETECTED_LOGGER = OLDLogFactory.detectLogger();
		final OLDLog log = OLDLogFactory.getLog(OLDLogFactory.class);
		if ((null != JVM_LOGGER) && !DETECTED_LOGGER.name().equals(JVM_LOGGER)) {
			log.warn("Failed to load Logger with property " + CUSTOM_LOGGER_JVM + "=" + JVM_LOGGER + ", using " + DETECTED_LOGGER.name() + " instead.");
		}
		// System.out.println(DETECTED_LOGGER.toString());
		if (log.isDebugEnabled()) {
			log.debug(DETECTED_LOGGER.toString());
		}
	}

	/**
	 * Private constructor to avoid instantiation.
	 */
	private OLDLogFactory() {
		// Nothing to do
	}

	/**
	 * Delivers the log for the given class or creates a new one if it doesn't exist already.
	 * 
	 * @param clazz
	 *            The corresponding class.
	 * 
	 * @return A logger for the given class.
	 */
	public static final OLDLog getLog(final Class<?> clazz) {
		return OLDLogFactory.getLog(clazz.getName());
	}

	/**
	 * Delivers the log for the given name or creates a new one if it doesn't exist already.
	 * 
	 * @param name
	 *            The corresponding name.
	 * 
	 * @return A logger for the given name.
	 */
	public static final OLDLog getLog(final String name) {
		switch (DETECTED_LOGGER) { // NOPMD (no break needed)
		case NONE:
			return new LogImplNone(name);
		case JDK:
			return new LogImplJDK14(name);
		case COMMONS:
			return new LogImplCommonsLogging(name);
		case SLF4J:
			return new LogImplSLF4JLogging(name);
		case WEBGUI:
			return new LogImplWebguiLogging(name);
		case JUNIT:
			return new LogImplJUnit(name);
		default:
			return new LogImplJDK14(name);
		}
	}

	/**
	 * This method tries to detect the available loggers.
	 * 
	 * @return The found logger.
	 */
	private static final Logger detectLogger() {
		if (null != JVM_LOGGER) {
			try {
				return Enum.valueOf(Logger.class, JVM_LOGGER);
			} catch (final IllegalArgumentException ex) { // NOPMD NOCS
				// Notify is handled above.
			}
		}
		try {
			if (Class.forName("org.slf4j.impl.StaticLoggerBinder") != null) {
				return Logger.SLF4J; // use SLF4J logging
			}
		} catch (final Exception ex) { // NOPMD NOCS (catch Exception)
			// use default in case of errors ...
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
