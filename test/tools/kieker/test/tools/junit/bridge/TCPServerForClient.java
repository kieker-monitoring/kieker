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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import junit.framework.Assert;

/**
 * TCP server to test the TCPClientConnector
 * 
 * @author Reiner Jung, Pascale Brandt
 * 
 */
@SuppressWarnings("deprecation")
public class TCPServerForClient implements Runnable {

	public void run() {
		String clientSentence;
		String capitalizedSentence;
		ServerSocket welcomeSocket;

		try {
			welcomeSocket = new ServerSocket(6789);

			while (true)
			{
				final Socket connectionSocket = welcomeSocket.accept();
				final BufferedReader inFromClient =
						new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				final DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
				clientSentence = inFromClient.readLine();
				System.out.println("Received: " + clientSentence);
				capitalizedSentence = clientSentence.toUpperCase() + '\n';
				outToClient.writeBytes(capitalizedSentence);
			}
		} catch (final IOException e) {
			e.printStackTrace();
			Assert.assertTrue("Connection to Server failed", false);
		}
	}
}
