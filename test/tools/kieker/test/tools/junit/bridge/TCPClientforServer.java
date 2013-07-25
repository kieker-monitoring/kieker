package kieker.test.tools.junit.bridge;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Assert;

public class TCPClientforServer implements Runnable {

	public void run() {

		boolean connected = false;

		while (!connected) {
			try {
				final Socket connectionSocket = new Socket(ConfigurationParameters.HOSTNAME, ConfigurationParameters.PORT);
				connected = true;
				try {
					final DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

					for (int i = 0; i < ConfigurationParameters.SEND_NUMBER_OF_RECORDS; i++) {
						outToClient.writeInt(1); // ID of test record type
						outToClient.writeInt(ConfigurationParameters.TEST_OPERATION_SIGNATURE.length());
						outToClient.writeBytes(ConfigurationParameters.TEST_OPERATION_SIGNATURE);
						outToClient.writeInt(ConfigurationParameters.TEST_SESSION_ID.length());
						outToClient.writeBytes(ConfigurationParameters.TEST_SESSION_ID);
						outToClient.writeLong(ConfigurationParameters.TEST_TRACE_ID);
						outToClient.writeLong(ConfigurationParameters.TEST_TIN);
						outToClient.writeLong(ConfigurationParameters.TEST_TOUT);
						outToClient.writeInt(ConfigurationParameters.TEST_HOSTNAME.length());
						outToClient.writeBytes(ConfigurationParameters.TEST_HOSTNAME);
						outToClient.writeInt(ConfigurationParameters.TEST_EOI);
						outToClient.writeInt(ConfigurationParameters.TEST_ESS);
					}

					connectionSocket.close();

				} catch (final IOException e) {
					// TODO I suggest to use Assert.fail(...) instead (Nils)
					Assert.assertTrue("Sending data to server failed. " + e.getMessage(), false);
				}
			} catch (final UnknownHostException e) {
				// TODO I suggest to use Assert.fail(...) instead (Nils)
				Assert.assertTrue("Unknown host " + e.getMessage(), false);
			} catch (final IOException e) {
				try {
					Thread.sleep(1000);
				} catch (final InterruptedException e1) {
				}
			}

		}
	}

}
