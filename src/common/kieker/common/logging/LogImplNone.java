/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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
 *
 * @since 1.6
 */
public final class LogImplNone implements Log {
	/**
	 * Creates a new instance of this class.
	 *
	 * @param name
	 *            The name of the logger.
	 */
	protected LogImplNone(final String name) { // NOPMD (does nothing)
		// nothing to do here
	}

	@Override
	public boolean isTraceEnabled() {
		return false;
	}

	@Override
	public void trace(final String message) {} // NOPMD (does nothing)

	@Override
	public void trace(final String message, final Throwable t) {} // NOPMD (does nothing)

	@Override
	public boolean isDebugEnabled() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void debug(final String message) {} // NOPMD (does nothing)

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void debug(final String message, final Throwable t) {} // NOPMD (does nothing)

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void info(final String message) {} // NOPMD (does nothing)

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void info(final String message, final Throwable t) {} // NOPMD (does nothing)

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void warn(final String message) {} // NOPMD (does nothing)

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void warn(final String message, final Throwable t) {} // NOPMD (does nothing)

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void error(final String message) {} // NOPMD (does nothing)

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void error(final String message, final Throwable t) {} // NOPMD (does nothing)

}
