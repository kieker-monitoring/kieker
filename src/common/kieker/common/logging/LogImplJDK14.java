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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Jan Waller
 */
public final class LogImplJDK14 implements Log {
	private final Logger logger;
	private final String name;

	protected LogImplJDK14(final String name) {
		this.name = name;
		this.logger = Logger.getLogger(name);
	}

	private final void log(final Level level, final String message, final Throwable t) {
		if (this.logger.isLoggable(level)) {
			final String sourceClass;
			final String sourceMethod;
			{ // NOCS detect calling class and method
				final StackTraceElement[] stackArray = new Throwable().getStackTrace(); // NOPMD
				if ((stackArray != null) && (stackArray.length > 2)) { // our stackDepth
					sourceClass = stackArray[2].getClassName();
					sourceMethod = stackArray[2].getMethodName();
				} else {
					sourceClass = this.name;
					sourceMethod = "";
				}
			}
			if (t != null) {
				this.logger.logp(level, sourceClass, sourceMethod, message, t);
			} else {
				this.logger.logp(level, sourceClass, sourceMethod, message);
			}
		}
	}

	public boolean isDebugEnabled() {
		return this.logger.isLoggable(Level.FINE);
	}

	public final void debug(final String message) {
		this.log(Level.FINE, message, null);
	}

	public final void debug(final String message, final Throwable t) {
		this.log(Level.FINE, message, t);
	}

	public final void info(final String message) {
		this.log(Level.INFO, message, null);

	}

	public final void info(final String message, final Throwable t) {
		this.log(Level.INFO, message, t);
	}

	public final void warn(final String message) {
		this.log(Level.WARNING, message, null);
	}

	public final void warn(final String message, final Throwable t) {
		this.log(Level.WARNING, message, t);
	}

	public final void error(final String message) {
		this.log(Level.SEVERE, message, null);
	}

	public final void error(final String message, final Throwable t) {
		this.log(Level.SEVERE, message, t);
	}
}
