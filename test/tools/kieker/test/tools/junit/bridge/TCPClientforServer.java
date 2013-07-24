package kieker.test.tools.junit.bridge;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.junit.Assert;

public class TCPClientforServer implements Runnable {

	public void run() {

		try {
			Socket connectionSocket;
			for (connectionSocket = new Socket(ConfigurationParameters.HOSTNAME, ConfigurationParameters.PORT); !connectionSocket.isConnected();) {
				Thread.sleep(1000);
			}
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
			Assert.assertTrue("Connection to Server failed" + e.getMessage(), false);
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
