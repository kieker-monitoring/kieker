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

	public void run() {
		final String clientSentence;
		final String capitalizedSentence;
		ServerSocket welcomeSocket;
		final String testOperationSignature = "Signatur";
		final String testSessionId = "Sessions";
		final long testTraceId = 4;
		final long testTin = 2;
		final long testTout = 13;
		final String testHostName = "Kieker";
		final int testEoi = 10;
		final int testEss = 9;

		try {
			welcomeSocket = new ServerSocket(6789);
			final Socket connectionSocket = welcomeSocket.accept();
			final DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

			for (int i = 0; i < 21; i++) {
				outToClient.writeInt(1); // ID of test record type
				outToClient.writeInt(testOperationSignature.length());
				outToClient.writeChars(testOperationSignature);
				outToClient.writeInt(testSessionId.length());
				outToClient.writeChars(testSessionId);
				outToClient.writeLong(testTraceId);
				outToClient.writeLong(testTin);
				outToClient.writeLong(testTout);
				outToClient.writeInt(testHostName.length());
				outToClient.writeChars(testHostName);
				outToClient.writeInt(testEoi);
				outToClient.writeInt(testEss);
			}
			connectionSocket.close();
			welcomeSocket.close();

		} catch (final IOException e) {
			e.printStackTrace();
			Assert.assertTrue("Connection to Server failed", false);
		}
	}
}
