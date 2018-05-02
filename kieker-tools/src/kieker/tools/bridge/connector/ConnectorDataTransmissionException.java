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
package kieker.tools.bridge.connector;

/**
 * Exception to box any Exception in a connector during data transmission and related tasks.
 * 
 * @author Reiner Jung
 * @since 1.8
 */
public class ConnectorDataTransmissionException extends Exception {

	/**
	 * serial number of the type.
	 */
	private static final long serialVersionUID = -7621182316652816606L;

	/**
	 * Create an exception without an exception trail.
	 * 
	 * @param message
	 *            The message to explain the exception
	 */
	public ConnectorDataTransmissionException(final String message) {
		super(message);
	}

	/**
	 * Create an exception with an exception trail.
	 * 
	 * @param message
	 *            The message to explain the exception
	 * @param exception
	 *            The exception which caused this exception
	 */
	public ConnectorDataTransmissionException(final String message, final Exception exception) {
		super(message, exception);
	}

}
