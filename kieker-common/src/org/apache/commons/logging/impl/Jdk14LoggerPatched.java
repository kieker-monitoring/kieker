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

package org.apache.commons.logging.impl;

/**
 * Based upon the Apache commons logging class Jdk14Logger
 * Used to determine the correct calling method.
 * 
 * @author Jan Waller
 * 
 * @since 1.5
 */
public final class Jdk14LoggerPatched extends Jdk14Logger {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param name
	 *            The name of the logger instance.
	 */
	public Jdk14LoggerPatched(final String name) {
		super(name);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public static final org.apache.commons.logging.Log getLog(final String name) {
		final org.apache.commons.logging.LogFactory commonsFactory = org.apache.commons.logging.LogFactory.getFactory();
		try {
			if (commonsFactory instanceof LogFactoryImpl) {
				final LogFactoryImpl commonsFactoryImpl = (LogFactoryImpl) commonsFactory;
				if (("org.apache.commons.logging.impl.Jdk14Logger".equals(commonsFactoryImpl.getLogClassName()))
						&& (commonsFactoryImpl.instances.get(name) == null)) {
					// commons using Jdk14Logger and not yet assigned
					final org.apache.commons.logging.Log instance = new Jdk14LoggerPatched(name);
					commonsFactoryImpl.instances.put(name, instance);
					return instance;
				}
			}
		} catch (final Exception ex) { // NOPMD NOCS (Exception)
			// if anything goes wrong, use the default commons implementation
		}
		return commonsFactory.getInstance(name);
	}

	/**
	 * Copy of {@link Jdk14Logger.log(level, msg, ex)} with correct stack depth for Kieker.
	 * 
	 * @param level
	 *            The level of the log entry (the severity).
	 * @param msg
	 *            The message to log.
	 * @param ex
	 *            The cause.
	 */
	private final void logpatched(final java.util.logging.Level level, final String msg, final Throwable ex) {
		final java.util.logging.Logger logger = this.getLogger();
		if (logger.isLoggable(level)) {
			final String cname = this.name;
			final String method;
			{ // NOCS detect calling class and method
				final StackTraceElement[] locations = new Throwable().getStackTrace(); // NOPMD (throwable)
				if (locations.length > 3) {
					method = locations[3].getMethodName();
				} else {
					method = "unknown";
				}
			}
			if (ex == null) {
				logger.logp(level, cname, method, msg);
			} else {
				logger.logp(level, cname, method, msg, ex);
			}
		}
	}

	@Override
	public final void debug(final Object message) {
		this.logpatched(java.util.logging.Level.FINE, String.valueOf(message), null);
	}

	@Override
	public final void debug(final Object message, final Throwable exception) {
		this.logpatched(java.util.logging.Level.FINE, String.valueOf(message), exception);
	}

	@Override
	public final void info(final Object message) {
		this.logpatched(java.util.logging.Level.INFO, String.valueOf(message), null);
	}

	@Override
	public final void info(final Object message, final Throwable exception) {
		this.logpatched(java.util.logging.Level.INFO, String.valueOf(message), exception);
	}

	@Override
	public final void warn(final Object message) {
		this.logpatched(java.util.logging.Level.WARNING, String.valueOf(message), null);
	}

	@Override
	public final void warn(final Object message, final Throwable exception) {
		this.logpatched(java.util.logging.Level.WARNING, String.valueOf(message), exception);
	}

	@Override
	public final void error(final Object message) {
		this.logpatched(java.util.logging.Level.SEVERE, String.valueOf(message), null);
	}

	@Override
	public final void error(final Object message, final Throwable exception) {
		this.logpatched(java.util.logging.Level.SEVERE, String.valueOf(message), exception);
	}
}
