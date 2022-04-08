/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.stage.adaptation;

/**
 * Thrown if the remote command could not be send.
 *
 * @author Marc Adolf
 *
 * @since 1.15
 *
 */
public class RemoteControlFailedException extends Exception {

	/**
	*
	*/
	private static final long serialVersionUID = 1L;

	/**
	 * Create exception with new message.
	 *
	 * @param message
	 *            the message
	 */
	public RemoteControlFailedException(final String message) {
		super(message);
	}

	/**
	 * Create a chained exception.
	 *
	 * @param e
	 *            previous exception
	 */
	public RemoteControlFailedException(final Exception e) {
		super(e);
	}

	/**
	 * Create a labeled and chained exception.
	 *
	 * @param message
	 *            message
	 * @param e
	 *            previous exception
	 */
	public RemoteControlFailedException(final String message, final Exception e) {
		super(message, e);
	}

}
