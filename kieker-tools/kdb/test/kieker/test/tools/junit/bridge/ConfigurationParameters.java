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
package kieker.test.tools.junit.bridge;

import kieker.analysis.tt.writeRead.jms.FakeInitialContextFactory;

/**
 * Common configuration parameters for all bridge tests.
 * 
 * @author Reiner Jung
 * 
 * @since 1.8
 */
public final class ConfigurationParameters {

	/**
	 * The ports for all TCP and JMS connections in the tests.
	 */
	public static final int TCP_CLIENT_PORT = 32443;
	public static final int TCP_MULTI_PORT = 32444; // NOCS
	public static final int TCP_SINGLE_PORT = 32445; // NOCS
	public static final int JMS_PORT = 32446; // NOCS
	public static final int JMS_EMBEDDED_PORT = 32447; // NOCS

	/**
	 * Number of messages to be send in this test.
	 */
	public static final int SEND_NUMBER_OF_RECORDS = 200;

	/**
	 * The hostname for all TCP and JMS connections in the tests.
	 */
	public static final String HOSTNAME = "localhost";

	/** JMS test user name. */
	public static final String JMS_USERNAME = "testuser";
	/** JMS test user password. */
	public static final String JMS_PASSWORD = "testpw";
	/** JMS test URI. */
	public static final String JMS_URI = "tcp://localhost:" + JMS_PORT;
	/** JMS embedded test URI. */
	public static final String JMS_EMBEDDED_URI = "tcp://localhost:" + JMS_EMBEDDED_PORT;

	/**
	 * Values for test records.
	 */
	public static final int TEST_RECORD_ID = 1;
	public static final String TEST_OPERATION_SIGNATURE = "some.operation.signature(final int a, final int b)"; // NOCS
	public static final String TEST_SESSION_ID = "Sessions"; // NOCS
	public static final long TEST_TRACE_ID = 4; // NOCS
	public static final long TEST_TIN = 2; // NOCS
	public static final long TEST_TOUT = 13; // NOCS
	public static final String TEST_HOSTNAME = "Kieker"; // NOCS
	public static final int TEST_EOI = 10; // NOCS
	public static final int TEST_ESS = 9; // NOCS

	/** The number of threads to start. */
	public static final int NUMBER_OF_TEST_THREADS = 5;
	/** The JMS factory to use for the JMS client connector test. */
	public static final String JMS_CLIENT_FACTORY_LOOKUP_NAME = FakeInitialContextFactory.class.getName();
	/** The JMS factory to use for the JMS embedded connector test. */
	public static final String JMS_EMBEDDED_FACTORY_LOOKUP_NAME = "org.apache.activemq.jndi.ActiveMQInitialContextFactory";

	/**
	 * Private default constructor for utility class.
	 */
	private ConfigurationParameters() {

	}

}
