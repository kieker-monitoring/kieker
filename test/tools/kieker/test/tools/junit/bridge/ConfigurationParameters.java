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
public final class ConfigurationParameters {

	/**
	 * The port for all TCP and JMS connections in the tests.
	 */
	public static final int PORT = 32444;

	/**
	 * Number of messages to be send in this test.
	 */
	public static final int SEND_NUMBER_OF_RECORDS = 200;

	/**
	 * The hostname for all TCP and JMS connections in the tests.
	 */
	public static final String HOSTNAME = "localhost";

	public static final int ID = 1;
	public static final String TEST_OPERATION_SIGNATURE = "Signature";
	public static final String TEST_SESSION_ID = "Sessions";
	public static final long TEST_TRACE_ID = 4;
	public static final long TEST_TIN = 2;
	public static final long TEST_TOUT = 13;
	public static final String TEST_HOSTNAME = "Kieker";
	public static final int TEST_EOI = 10;
	public static final int TEST_ESS = 9;

	/**
	 * The number of started threads
	 */
	public static final int STARTED_CLIENTS = 5;

	public static int COUNT_RECORDS = 0;

	private ConfigurationParameters() {}

}
