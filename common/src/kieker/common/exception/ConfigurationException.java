/***************************************************************************
 * Copyright (C) 2018 Kieker Project (https://kieker-monitoring.net)
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
 * Configuration exception for command line and configuration file parameters.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public class ConfigurationException extends Exception {

	private static final long serialVersionUID = -6121223240278366389L;

	/**
	 * Configuration exception.
	 *
	 * @param message
	 *            message to be reported
	 */
	public ConfigurationException(final String message) {
		super(message);
	}

	/**
	 * Configuration exception.
	 *
	 * @param e
	 *            chained exception
	 */
	public ConfigurationException(final Exception e) {
		super(e);
	}

}
