/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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
package kieker.test.tools.junit.bridge;

/**
 * Common configuration parameters for all bridge tests.
 * 
 * @author Reiner Jung
 * 
 */
public class ConfigurationParameters {
	/**
	 * Number of messages to be send in this test.
	 */
	public static final int SEND_NUMBER_OF_RECORDS = 20;

	/**
	 * The hostname for all TCP and JMS connections in the tests.
	 */
	public static final String HOSTNAME = "localhost";

	/**
	 * The port for all TCP and JMS connections in the tests.
	 */
	public static final int PORT = 32444;
}
