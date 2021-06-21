/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Assert;

/**
 * Client for the TCP server tests, providing records.
 *
 * @author Reiner Jung, Pascale Brandt
 *
 * @since 1.8
 */

public class TCPClientforServer implements Runnable {

	private final int port;

	/**
	 * Constructor.
	 *
	 * @param port
	 *            the server port this client connects to
	 */
	public TCPClientforServer(final int port) {
		this.port = port;
	}

	/**
	 * Main run loop.
	 */
	@Override
	public void run() {
		boolean connected = false;

		while (!connected) {
			try {
				final Socket connectionSocket = new Socket(ConfigurationParameters.HOSTNAME, this.port);
				connected = true;
				try {
					final DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

					for (int i = 0; i < ConfigurationParameters.SEND_NUMBER_OF_RECORDS; i++) {
						outToClient.writeInt(ConfigurationParameters.TEST_RECORD_ID);
						outToClient.writeInt(ConfigurationParameters.TEST_OPERATION_SIGNATURE.length());
						outToClient.writeBytes(ConfigurationParameters.TEST_OPERATION_SIGNATURE);
						outToClient.writeInt(ConfigurationParameters.TEST_SESSION_ID.length());
						outToClient.writeBytes(ConfigurationParameters.TEST_SESSION_ID);
						outToClient.writeLong(ConfigurationParameters.TEST_TRACE_ID);
						outToClient.writeLong(ConfigurationParameters.TEST_TIN);
						outToClient.writeLong(ConfigurationParameters.TEST_TOUT);
						outToClient.writeInt(ConfigurationParameters.TEST_HOSTNAME.length());
						outToClient.writeBytes(ConfigurationParameters.TEST_HOSTNAME);
						outToClient.writeInt(i); // send the record ID
						outToClient.writeInt(ConfigurationParameters.TEST_ESS);
					}

					connectionSocket.close();

				} catch (final IOException e) {
					// exception catch required, as run cannot have any additional throws
					Assert.fail("Sending data to server failed: " + e.getMessage());
				}
			} catch (final UnknownHostException e) {
				Assert.fail("Unknown host " + e.getMessage());
			} catch (final IOException e) {
				// polling for the server
				try {
					Thread.sleep(1000);
				} catch (final InterruptedException e1) {
					// can be ignored
				}
			}

		}
	}

}
