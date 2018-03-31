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

package kieker.tools.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager; // NOCS

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Nils Christian Ehmke
 *
 * @since 1.10
 */
public final class ToolsUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ToolsUtil.class);

	private ToolsUtil() {
		// Private constructor to avoid instantiation
	}

	public static void loadVerboseLogger() {
		ToolsUtil.loadLogger("logging.verbose.properties");
	}

	public static void loadDebugLogger() {
		ToolsUtil.loadLogger("logging.debug.properties");
	}

	public static void loadDefaultLogger() {
		ToolsUtil.loadLogger("logging.properties");
	}

	private static void loadLogger(final String loggerProperties) {
		try {
			final LogManager logManager = LogManager.getLogManager();
			final InputStream configStream = ClassLoader.getSystemClassLoader().getResourceAsStream(loggerProperties);
			if (configStream != null) {
				logManager.readConfiguration(configStream);
			} else {
				LOGGER.warn("Could not load verbose/debug logger");
			}
		} catch (final SecurityException ex) {
			LOGGER.warn("Could not load verbose/debug logger", ex);
		} catch (final IOException ex) {
			LOGGER.warn("Could not load verbose/debug logger", ex);
		}
	}

}
