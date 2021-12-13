/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.core.signaturePattern;

/**
 * @author Bjoern Weissenfels, Jan Waller
 *
 * @since 1.6
 */
public class InvalidPatternException extends Exception {

	private static final long serialVersionUID = 7568907124941706485L;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param reason
	 *            The message for this exception.
	 */
	public InvalidPatternException(final String reason) {
		super(reason);
	}

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param reason
	 *            The message for this exception.
	 *
	 * @param cause
	 *            The cause of this exception.
	 */
	public InvalidPatternException(final String reason, final Throwable cause) {
		super(reason, cause);
	}
}
