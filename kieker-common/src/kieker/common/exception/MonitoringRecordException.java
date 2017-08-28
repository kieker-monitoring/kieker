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

package kieker.common.exception;

/**
 * This exception can be used to show that something in context with a monitoring record failed.
 * 
 * @author Jan Waller
 * 
 * @since 1.0
 */
public class MonitoringRecordException extends Exception {

	private static final long serialVersionUID = -619093518689867366L;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param messString
	 *            The message of this exception.
	 */
	public MonitoringRecordException(final String messString) {
		super(messString);
	}

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param messString
	 *            The message of this exception.
	 * @param cause
	 *            The cause of this exception.
	 */
	public MonitoringRecordException(final String messString, final Throwable cause) {
		super(messString, cause);
	}
}
