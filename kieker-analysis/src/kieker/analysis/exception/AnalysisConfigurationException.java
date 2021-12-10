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

package kieker.analysis.exception;

/**
 * An exception to show that something went wrong during the configuration of an analysis.
 * 
 * @author Jan Waller
 * 
 * @since 1.5
 */
public class AnalysisConfigurationException extends Exception {

	private static final long serialVersionUID = -9115316314866942458L;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param message
	 *            The message of the exception.
	 * @param cause
	 *            The cause of the exception.
	 */
	public AnalysisConfigurationException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param message
	 *            The message of the exception.
	 */
	public AnalysisConfigurationException(final String message) {
		super(message);
	}
}
