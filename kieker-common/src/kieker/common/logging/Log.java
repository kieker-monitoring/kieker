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

package kieker.common.logging;

/**
 * <p>
 * A simple logging interface abstracting logging APIs.
 * </p>
 *
 * <p>
 * The five logging levels used by <code>Log</code> are (in order):
 * <ol>
 * <li>trace (the least serious)</li>
 * <li>debug</li>
 * <li>info</li>
 * <li>warn</li>
 * <li>error (the most serious)</li>
 * </ol>
 * </p>
 *
 * @author Jan Waller, Christian Wulf
 *
 * @since 1.5
 */
public interface Log { // NOCS (Name of Interface)

	/**
	 * <p>
	 * Is trace logging currently enabled?
	 * </p>
	 *
	 * <p>
	 * Call this method to prevent having to perform expensive operations (for example, <code>String</code> concatenation) when the log level is more than trace.
	 * </p>
	 *
	 * @return true if trace is enabled in the underlying logger.
	 *
	 * @since 1.10
	 */
	public boolean isTraceEnabled();

	/**
	 * <p>
	 * Log a message with trace log level.
	 * </p>
	 *
	 * <p>
	 * Use this log level for the development mode to log a single step, e.g., "Loaded user 'Helen'" (cf. {@link #debug(String)}).
	 * </p>
	 *
	 * @param message
	 *            log this message
	 *
	 * @since 1.10
	 */
	public void trace(String message);

	/**
	 * <p>
	 * Log an error with trace log level.
	 * </p>
	 *
	 * <p>
	 * Use this log level for the development mode to log a single step, e.g., "Loaded user 'Helen'" (cf. {@link #debug(String, Throwable)}).
	 * </p>
	 *
	 * @param message
	 *            log this message
	 * @param t
	 *            log this cause
	 *
	 * @since 1.10
	 */
	public void trace(String message, Throwable t);

	/**
	 * <p>
	 * Is debug logging currently enabled?
	 * </p>
	 *
	 * <p>
	 * Call this method to prevent having to perform expensive operations (for example, <code>String</code> concatenation) when the log level is more than debug.
	 * </p>
	 *
	 * @return true if debug is enabled in the underlying logger.
	 *
	 * @since 1.5
	 */
	public boolean isDebugEnabled();

	/**
	 * <p>
	 * Log a message with debug log level.
	 * </p>
	 *
	 * <p>
	 * Use this log level for the development mode to log a set of single steps, e.g., "Loaded 10 users from file" (cf. {@link #trace(String)}).
	 * </p>
	 *
	 * @param message
	 *            log this message
	 *
	 * @since 1.5
	 */
	public void debug(String message);

	/**
	 * <p>
	 * Log an error with debug log level.
	 * </p>
	 *
	 * <p>
	 * Use this log level for the development mode to log a set of single steps, e.g., "Loaded 10 users from file" (cf. {@link #trace(String, Throwable)}).
	 * </p>
	 *
	 * @param message
	 *            log this message
	 * @param t
	 *            log this cause
	 *
	 * @since 1.5
	 */
	public void debug(String message, Throwable t);

	/**
	 * <p>
	 * Log a message with info log level.
	 * </p>
	 *
	 * <p>
	 * Use this log level for the production mode.
	 * </p>
	 *
	 * @param message
	 *            log this message
	 *
	 * @since 1.5
	 */
	public void info(String message);

	/**
	 * <p>
	 * Log an error with info log level.
	 * </p>
	 *
	 * <p>
	 * Use this log level for the production mode.
	 * </p>
	 *
	 * @param message
	 *            log this message
	 * @param t
	 *            log this cause
	 *
	 * @since 1.5
	 */
	public void info(String message, Throwable t);

	/**
	 * <p>
	 * Log a message with warn log level.
	 * </p>
	 *
	 * <p>
	 * Use this log level for the production mode.
	 * </p>
	 *
	 * @param message
	 *            log this message
	 *
	 * @since 1.5
	 */
	public void warn(String message);

	/**
	 * <p>
	 * Log an error with warn log level.
	 * </p>
	 *
	 * <p>
	 * Use this log level for the production mode.
	 * </p>
	 *
	 * @param message
	 *            log this message
	 * @param t
	 *            log this cause
	 *
	 * @since 1.5
	 */
	public void warn(String message, Throwable t);

	/**
	 * <p>
	 * Log a message with error log level.
	 * </p>
	 *
	 * <p>
	 * Use this log level for the production mode.
	 * </p>
	 *
	 * @param message
	 *            log this message
	 *
	 * @since 1.5
	 */
	public void error(String message);

	/**
	 * <p>
	 * Log an error with error log level.
	 * </p>
	 *
	 * <p>
	 * Use this log level for the production mode.
	 * </p>
	 *
	 * @param message
	 *            log this message
	 * @param t
	 *            log this cause
	 *
	 * @since 1.5
	 */
	public void error(String message, Throwable t);
}
