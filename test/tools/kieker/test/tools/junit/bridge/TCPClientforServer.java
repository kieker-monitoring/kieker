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

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Assert;

import kieker.common.record.MonitoringRecordFactory;

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
	public void run() {

		boolean connected = false;

		while (!connected) {
			try {
				final Socket connectionSocket = new Socket(ConfigurationParameters.HOSTNAME, this.port);
				connected = true;
				try {
					final DataOutputStream out = new DataOutputStream(connectionSocket.getOutputStream());

					// write initially one String message hostname
					this.writeStringMessage(out, ConfigurationParameters.TEST_HOSTNAME, 1);
					this.writeStringMessage(out, ConfigurationParameters.TEST_OPERATION_SIGNATURE, 2);
					this.writeStringMessage(out, ConfigurationParameters.TEST_SESSION_ID, 3);
					for (int i = 0; i < ConfigurationParameters.SEND_NUMBER_OF_RECORDS; i++) {
						out.writeInt(ConfigurationParameters.TEST_RECORD_ID);
						out.writeInt(2); // string id TEST_OPERATION_SIGNATURE
						out.writeInt(3); // string id TEST_SESSION_ID
						out.writeLong(ConfigurationParameters.TEST_TRACE_ID);
						out.writeLong(ConfigurationParameters.TEST_TIN);
						out.writeLong(ConfigurationParameters.TEST_TOUT);
						out.writeInt(1); // string id TEST_HOSTNAME
						out.writeInt(ConfigurationParameters.TEST_EOI);
						out.writeInt(ConfigurationParameters.TEST_ESS);
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

	private void writeStringMessage(final DataOutputStream out, final String string, final int id) throws IOException {
		out.writeInt(MonitoringRecordFactory.STRING_MAP_ID);
		out.writeInt(id);
		out.writeInt(string.length());
		out.writeBytes(string);
	}

}
