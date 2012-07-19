/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.tools.kdm.manager.exception;

/**
 * his class represents an exception that occurs if an invalid package name has been passed to a method.
 * 
 * @author Nils Christian Ehmke
 * @version 1.0
 */
public class InvalidPackageException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public InvalidPackageException() {
		super();
	}

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param message
	 *            The message for this exception.
	 */
	public InvalidPackageException(final String message) {
		super(message);
	}

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param cause
	 *            The cause for this exception.
	 */
	public InvalidPackageException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param message
	 *            The message for this exception.
	 * @param cause
	 *            The cause for this exception.
	 */
	public InvalidPackageException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
