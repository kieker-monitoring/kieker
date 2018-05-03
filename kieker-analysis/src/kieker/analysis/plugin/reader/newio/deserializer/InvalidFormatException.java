/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.plugin.reader.newio.deserializer;

/**
 * Denotes that an invalid data format was found.
 *
 * @author Holger Knoche
 *
 * @since 1.13
 */
public class InvalidFormatException extends RuntimeException {

	private static final long serialVersionUID = 8559393355991197765L;

	/**
	 * Creates a new instance using the given data.
	 * 
	 * @param message
	 *            The message for this exception
	 */
	public InvalidFormatException(final String message) {
		super(message);
	}

	/**
	 * Creates a new instance using the given data.
	 * 
	 * @param message
	 *            The message for this exception
	 * @param cause
	 *            The cause of the exception
	 */
	public InvalidFormatException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
