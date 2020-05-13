/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.trace.analysis.filter.visualization.exception;

/**
 * Generic exception which denotes that an error has occured during graph formatting.
 * 
 * @author Holger Knoche
 * 
 * @since 1.6
 */
public class GraphFormattingException extends RuntimeException {

	private static final long serialVersionUID = 5291455622267801758L;

	/**
	 * Creates a new exception with the given message.
	 * 
	 * @param message
	 *            The message to use
	 */
	public GraphFormattingException(final String message) {
		super(message);
	}

	/**
	 * Creates a new exception with the given cause.
	 * 
	 * @param cause
	 *            The cause of this exception
	 */
	public GraphFormattingException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Creates a new exception with a given message and a given cause.
	 * 
	 * @param message
	 *            The message to use
	 * @param cause
	 *            The cause of this exception
	 */
	public GraphFormattingException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
