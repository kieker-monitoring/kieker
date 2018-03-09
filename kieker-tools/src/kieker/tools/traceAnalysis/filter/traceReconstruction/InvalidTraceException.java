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

package kieker.tools.traceAnalysis.filter.traceReconstruction;

/**
 * This exception can be used to mark an invalid trace.
 * 
 * @author Andre van Hoorn
 * 
 * @since 0.95a
 */
public class InvalidTraceException extends Exception {

	private static final long serialVersionUID = 1893L;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param message
	 *            The message of this exception.
	 */
	public InvalidTraceException(final String message) {
		super(message);
	}

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param message
	 *            The message of this exception.
	 * @param t
	 *            The cause of this exception.
	 */
	public InvalidTraceException(final String message, final Throwable t) {
		super(message, t);
	}
}
