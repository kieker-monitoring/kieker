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

package org.apache.commons.logging.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Based upon the Apache commons logging class Jdk14Logger
 * Used to determine the correct calling method
 * 
 * @author Jan Waller
 */
public final class Jdk14LoggerPatched extends Jdk14Logger {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "unchecked", "deprecation" })
	public static final org.apache.commons.logging.Log getLog(final String name) {
		final org.apache.commons.logging.LogFactory commonsFactory = org.apache.commons.logging.LogFactory.getFactory();
		try {
			if (commonsFactory instanceof LogFactoryImpl) {
				final LogFactoryImpl commonsFactoryImpl = (LogFactoryImpl) commonsFactory;
				if ("org.apache.commons.logging.impl.Jdk14Logger".equals(commonsFactoryImpl.getLogClassName())) {
					// commons using Jdk14Logger
					org.apache.commons.logging.Log instance = (org.apache.commons.logging.Log) commonsFactoryImpl.instances.get(name);
					if (instance == null) {
						instance = new Jdk14LoggerPatched(name);
						commonsFactoryImpl.instances.put(name, instance);
					}
					return instance;
				}
			}
		} catch (final Exception ex) {
			// if anything goes wrong, use the default commons implementation
		}
		return commonsFactory.getInstance(name);
	}

	public Jdk14LoggerPatched(final String name) {
		super(name);
	}

	private final void log(final Level level, final String msg, final Throwable ex) {
		final Logger logger = this.getLogger();
		if (logger.isLoggable(level)) {
			final String sourceClass;
			final String sourceMethod;
			{ // detect calling class and method
				final StackTraceElement[] stackArray = new Throwable().getStackTrace();
				if ((stackArray != null) && (stackArray.length > 3)) { // our stackDepth
					sourceClass = stackArray[3].getClassName();
					sourceMethod = stackArray[3].getMethodName();
				} else {
					sourceClass = this.name;
					sourceMethod = "";
				}
			}
			if (ex == null) {
				logger.logp(level, sourceClass, sourceMethod, msg);
			} else {
				logger.logp(level, sourceClass, sourceMethod, msg, ex);
			}
		}
	}

	@Override
	public final void debug(final Object message) {
		this.log(Level.FINE, String.valueOf(message), null);
	}

	@Override
	public final void debug(final Object message, final Throwable exception) {
		this.log(Level.FINE, String.valueOf(message), exception);
	}

	@Override
	public final void info(final Object message) {
		this.log(Level.INFO, String.valueOf(message), null);
	}

	@Override
	public final void info(final Object message, final Throwable exception) {
		this.log(Level.INFO, String.valueOf(message), exception);
	}

	@Override
	public final void warn(final Object message) {
		this.log(Level.WARNING, String.valueOf(message), null);
	}

	@Override
	public final void warn(final Object message, final Throwable exception) {
		this.log(Level.WARNING, String.valueOf(message), exception);
	}

	@Override
	public final void error(final Object message) {
		this.log(Level.SEVERE, String.valueOf(message), null);
	}

	@Override
	public final void error(final Object message, final Throwable exception) {
		this.log(Level.SEVERE, String.valueOf(message), exception);
	}
}
