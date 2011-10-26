/***************************************************************************
 * Copyright 2011 by
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

package kieker.common.logging;

/**
 * 
 * @author Jan Waller
 */
public final class LogFactory {

	private static enum Logger {
		JDK, COMMONS,
	}

	private static final Logger DETECTED_LOGGER;

	static {
		Logger logselectiontemp = LogFactory.Logger.JDK; // default to JDK logging
		try {
			if (Class.forName("org.apache.commons.logging.Log") != null) {
				logselectiontemp = LogFactory.Logger.COMMONS; // use commons logging
			}
		} catch (final Exception ex) { // NOCS // NOPMD
			// failed to find Apache commons logging
		}
		DETECTED_LOGGER = logselectiontemp; // NOCS (missing this)
	}

	private LogFactory() {
		// Nothing to do
	}

	public static final Log getLog(final Class<?> clazz) {
		return LogFactory.getLog(clazz.getName());
	}

	public static final Log getLog(final String name) {
		switch (LogFactory.DETECTED_LOGGER) { // NOPMD (no break needed)
		case COMMONS:
			return new LogImplCommonsLogging(name);
		case JDK:
		default:
			return new LogImplJDK14(name);
		}
	}
}
