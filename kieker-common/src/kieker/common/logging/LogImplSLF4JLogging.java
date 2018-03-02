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
 *
 * This is an actual implementation of the logging interface used by SLF4J logger.
 *
 * @author Jan Waller
 *
 * @since 1.6
 */
public class LogImplSLF4JLogging extends AbstractLogImpl {

	private final org.slf4j.Logger log;

	/**
	 * Creates a new instance of this class.
	 *
	 * @param name
	 *            The name of the logger.
	 */
	public LogImplSLF4JLogging(final String name) {
		this.log = org.slf4j.LoggerFactory.getLogger(name);
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
		this.log.debug(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final String message, final Throwable t) {
		this.log.debug(message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final String message) {
		this.log.info(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final String message, final Throwable t) {
		this.log.info(message, t);
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
		this.log.warn(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final String message, final Throwable t) {
		this.log.warn(message, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final String message) {
		this.log.error(message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final String message, final Throwable t) {
		this.log.error(message, t);
	}

}
