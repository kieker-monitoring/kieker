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
package kieker.analysis.exception;

/**
 * Exception for errors that occur due to design errors or misuse of classes or are otherwise unexpected.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class InternalErrorException extends Exception {

	private static final long serialVersionUID = -1937288066794917847L;

	public InternalErrorException(final String message) {
		super(message);
	}

	public InternalErrorException(final String message, final Exception e) {
		super(message, e);
	}

	public InternalErrorException() {
		super();
	}
}
