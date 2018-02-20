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
package kieker.test.tools.junit.bridge;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.Assert;

/**
 * TCP server to test the TCPClientConnector.
 *
 * @author Reiner Jung, Pascale Brandt
 *
 * @since 1.8
 *
 */
public class TCPServerForClient implements Runnable {

	private final int port;
	private volatile boolean listening;

	/**
	 * Constructor.
	 *
	 * @param port
	 *            the server port of this server
	 */
	public TCPServerForClient(final int port) {
		this.port = port;
		this.listening = false;
	}

	/**
	 * Check whether the server is available.
	 *
	 * @return true if the service is available
	 */
	public boolean isListening() {
		synchronized (this) {
			return this.listening;
		}
	}

	/**
	 * Main loop for the server.
	 */
	@Override
	public void run() {
		try {
			final ServerSocket serverSocket = new ServerSocket(this.port);
			try {
				this.listening = true;
				final Socket connectionSocket = serverSocket.accept();
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
					outToClient.writeInt(i); // send the current record id
					outToClient.writeInt(ConfigurationParameters.TEST_ESS);
				}

				connectionSocket.close();
			} catch (final IOException e) {
				// exception catch required, as run cannot have any additional throws
				Assert.fail("Communication failed: " + e.getMessage());
			} finally {
				serverSocket.close();
			}
		} catch (final IOException eServer) {
			// exception catch required, as run cannot have any additional throws
			Assert.fail("Server port not available: " + eServer.getMessage());
		}
	}

}
