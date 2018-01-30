package kieker.monitoring.writer.tcp;

import java.io.IOException;
import java.net.*;
import java.nio.channels.SocketChannel;

import org.junit.*;

public class SocketChannelTest {

	private String hostname;
	private int port;

	@Before
	public void before() {
		this.hostname = "localhost";
		this.port = 45678;
	}

	@Test(expected = java.net.UnknownHostException.class)
	public void connectShouldFailWithUnresolvedInetAddress() throws Exception {
		SocketAddress socketAddress = InetSocketAddress.createUnresolved(this.hostname, this.port);
		int timeout = 0;

		Socket socket = createSocket();

		socket.connect(socketAddress, timeout);
	}

	@Test(expected = java.net.ConnectException.class)
	public void connectShouldFailWithoutServerAndTimeoutOfZero() throws Exception {
		SocketAddress socketAddress = new InetSocketAddress(this.hostname, this.port);
		int timeout = 0;

		Socket socket = createSocket();

		socket.connect(socketAddress, timeout);
	}

	@Test(expected = java.net.SocketTimeoutException.class)
	public void connectShouldFailDueToCustomTimeoutOf200() throws Exception {
		SocketAddress socketAddress = new InetSocketAddress(this.hostname, this.port);
		int timeout = 200;

		Socket socket = createSocket();

		socket.connect(socketAddress, timeout);
	}

	@Test(expected = java.net.ConnectException.class)
	public void connectShouldFailDueToInternalMaxTimeoutOf1000() throws Exception {
		SocketAddress socketAddress = new InetSocketAddress(this.hostname, this.port);
		// it seems as a timeout above 1000 is not used by socket the implementation
		int timeout = 2000;

		Socket socket = createSocket();

		socket.connect(socketAddress, timeout);
	}

	private Socket createSocket() throws IOException {
		SocketChannel socketChannel = SocketChannel.open();
		Socket socket = socketChannel.socket();
		return socket;
	}
}
