/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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
 * The four logging levels used by <code>Log</code> are (in order):
 * <ol>
 * <li>debug (the least serious)</li>
 * <li>info</li>
 * <li>warn</li>
 * <li>error (the most serious)</li>
 * </ol>
 * </p>
 * 
 * @author Jan Waller
 * 
 * @since 1.5
 */
public interface Log { // NOCS (Name of Interface)

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
	 */
	public boolean isDebugEnabled();

	/**
	 * <p>
	 * Log a message with debug log level.
	 * </p>
	 * 
	 * @param message
	 *            log this message
	 */
	public void debug(String message);

	/**
	 * <p>
	 * Log an error with debug log level.
	 * </p>
	 * 
	 * @param message
	 *            log this message
	 * @param t
	 *            log this cause
	 */
	public void debug(String message, Throwable t);

	/**
	 * <p>
	 * Log a message with info log level.
	 * </p>
	 * 
	 * @param message
	 *            log this message
	 */
	public void info(String message);

	/**
	 * <p>
	 * Log an error with info log level.
	 * </p>
	 * 
	 * @param message
	 *            log this message
	 * @param t
	 *            log this cause
	 */
	public void info(String message, Throwable t);

	/**
	 * <p>
	 * Log a message with warn log level.
	 * </p>
	 * 
	 * @param message
	 *            log this message
	 */
	public void warn(String message);

	/**
	 * <p>
	 * Log an error with warn log level.
	 * </p>
	 * 
	 * @param message
	 *            log this message
	 * @param t
	 *            log this cause
	 */
	public void warn(String message, Throwable t);

	/**
	 * <p>
	 * Log a message with error log level.
	 * </p>
	 * 
	 * @param message
	 *            log this message
	 */
	public void error(String message);

	/**
	 * <p>
	 * Log an error with error log level.
	 * </p>
	 * 
	 * @param message
	 *            log this message
	 * @param t
	 *            log this cause
	 */
	public void error(String message, Throwable t);
}
