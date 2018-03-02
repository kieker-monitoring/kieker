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

/**
 * This is an actual implementation of the logging interface used by the common logger of the JVM.
 *
 * @author Jan Waller
 *
 * @since 1.5
 */
public final class LogImplCommonsLogging extends AbstractLogImpl {

	private static final boolean JDK14PATCH;

	private final org.apache.commons.logging.Log log;

	static {
		final org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog("KiekerTest");
		if ("org.apache.commons.logging.impl.Jdk14Logger".equals(log.getClass().getName())) {
			JDK14PATCH = true;
		} else {
			JDK14PATCH = false;
		}
	}

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param name
	 *            The name of the logger to be used.
	 */
	protected LogImplCommonsLogging(final String name) {
		if (JDK14PATCH) {
			this.log = org.apache.commons.logging.impl.Jdk14LoggerPatched.getLog(name);
		} else {
			this.log = org.apache.commons.logging.LogFactory.getLog(name);
		}
	}

	@Override
	public boolean isTraceEnabled() {
		return this.log.isTraceEnabled();
	}

	@Override
	public void trace(final String message) {
		this.log.trace(message);
	}

	@Override
	public void trace(final String message, final Throwable t) {
		this.log.trace(message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDebugEnabled() {
		return this.log.isDebugEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final String message) {
		if (this.log.isDebugEnabled()) {
			this.log.debug(message);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final String message, final Throwable t) {
		if (this.log.isDebugEnabled()) {
			this.log.debug(message, t);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final String message) {
		if (this.log.isInfoEnabled()) {
			this.log.info(message);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final String message, final Throwable t) {
		if (this.log.isInfoEnabled()) {
			this.log.info(message, t);
		}
	}

	@Override
	public boolean isWarnEnabled() {
		return this.log.isWarnEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final String message) {
		if (this.log.isWarnEnabled()) {
			this.log.warn(message);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final String message, final Throwable t) {
		if (this.log.isWarnEnabled()) {
			this.log.warn(message, t);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final String message) {
		if (this.log.isErrorEnabled()) {
			this.log.error(message);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final String message, final Throwable t) {
		if (this.log.isErrorEnabled()) {
			this.log.error(message, t);
		}
	}

}
