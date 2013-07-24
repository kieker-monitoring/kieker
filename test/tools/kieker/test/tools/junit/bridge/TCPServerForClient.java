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
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.Assert;

/**
 * TCP server to test the TCPClientConnector
 * 
 * @author Reiner Jung, Pascale Brandt
 * 
 */
public class TCPServerForClient implements Runnable {

	final String testOperationSignature = "Signatur";
	final String testSessionId = "Sessions";
	final long testTraceId = 4;
	final long testTin = 2;
	final long testTout = 13;
	final String testHostName = "Kieker";
	final int testEoi = 10;
	final int testEss = 9;

	public void run() {
		ServerSocket welcomeSocket;
		try {
			welcomeSocket = new ServerSocket(ConfigurationParameters.PORT);
			final Socket connectionSocket = welcomeSocket.accept();
			final DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

			for (int i = 0; i < ConfigurationParameters.SEND_NUMBER_OF_RECORDS; i++) {
				outToClient.writeInt(1); // ID of test record type
				outToClient.writeInt(this.testOperationSignature.length());
				outToClient.writeChars(this.testOperationSignature);
				outToClient.writeInt(this.testSessionId.length());
				outToClient.writeChars(this.testSessionId);
				outToClient.writeLong(this.testTraceId);
				outToClient.writeLong(this.testTin);
				outToClient.writeLong(this.testTout);
				outToClient.writeInt(this.testHostName.length());
				outToClient.writeChars(this.testHostName);
				outToClient.writeInt(this.testEoi);
				outToClient.writeInt(this.testEss);
			}
			connectionSocket.close();
			welcomeSocket.close();

		} catch (final IOException e) {
			Assert.assertTrue("Connection to Server failed" + e.getMessage(), false);
		}

	}

}
