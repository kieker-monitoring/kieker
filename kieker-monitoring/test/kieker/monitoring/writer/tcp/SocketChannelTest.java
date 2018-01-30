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
package kieker.monitoring.writer.tcp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Christian Wulf
 * @since 1.14
 */
public class SocketChannelTest {

	private String hostname;
	private int port;

	public SocketChannelTest() {
		super();
	}

	@Before
	public void before() {
		this.hostname = "localhost";
		this.port = 45678;
	}

	@Test(expected = UnknownHostException.class)
	public void connectShouldFailWithUnresolvedInetAddress() throws Exception {
		final SocketAddress socketAddress = InetSocketAddress.createUnresolved(this.hostname, this.port);
		final int timeout = 0;

		final Socket socket = this.createSocket();

		socket.connect(socketAddress, timeout);
	}

	@Test(expected = ConnectException.class)
	public void connectShouldFailWithoutServerAndTimeoutOfZero() throws Exception {
		final SocketAddress socketAddress = new InetSocketAddress(this.hostname, this.port);
		final int timeout = 0;

		final Socket socket = this.createSocket();

		socket.connect(socketAddress, timeout);
	}

	@Test(expected = SocketTimeoutException.class)
	public void connectShouldFailDueToCustomTimeoutBelow1000() throws Exception {
		final SocketAddress socketAddress = new InetSocketAddress(this.hostname, this.port);
		final int timeout = 1;

		final Socket socket = this.createSocket();

		socket.connect(socketAddress, timeout);
	}

	@Test(expected = ConnectException.class)
	public void connectShouldFailDueToInternalMaxTimeoutOf1000() throws Exception {
		final SocketAddress socketAddress = new InetSocketAddress(this.hostname, this.port);
		// it seems as a timeout above 1000 is not used by socket the implementation
		final int timeout = 2000;

		final Socket socket = this.createSocket();

		socket.connect(socketAddress, timeout);
	}

	@Test
	public void reconnectShouldWork() throws Exception {
		final SocketAddress socketAddress = new InetSocketAddress(this.hostname, this.port);
		final int timeout = 1;

		final Socket socket = SocketChannel.open().socket();
		try {
			socket.connect(socketAddress, timeout);
			fail("The previous connect should throw an exception.");
		} catch (SocketTimeoutException | ConnectException e) { // NOPMD (empty catch block)
			// both of the exceptions indicate a connection timeout
			// => ignore to reconnect
		}

		assertThat(socket.isClosed(), is(true));

		final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open().bind(socketAddress);
		try {
			// start server in a non-blocking way
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.accept();

			// reconnect to the server (requires a new channel)
			final Socket anotherSocket = SocketChannel.open().socket();
			try {
				anotherSocket.connect(socketAddress);
			} finally {
				anotherSocket.close();
			}
		} finally {
			serverSocketChannel.close();
		}
	}

	@Test
	public void socketChannelShouldAlwaysReturnTheSameSocket() throws Exception {
		final SocketAddress socketAddress = new InetSocketAddress(this.hostname, this.port);
		final int timeout = 1;

		final SocketChannel socketChannel = SocketChannel.open();

		final Socket socket = socketChannel.socket();
		try {
			socket.connect(socketAddress, timeout);
			fail("The previous connect should throw an exception.");
		} catch (SocketTimeoutException | ConnectException e) { // NOPMD (empty catch block)
			// both of the exceptions indicate a connection timeout
			// => ignore to reconnect
		}

		final Socket anotherSocket = socketChannel.socket();

		assertThat(anotherSocket, is(sameInstance(socket)));
	}

	/**
	 * Creates a new channel and returns the associated socket.
	 *
	 * @return a new socket
	 * @throws IOException
	 *             If an I/O error occurs while opening a new channel
	 */
	private Socket createSocket() throws IOException {
		final SocketChannel socketChannel = SocketChannel.open();
		return socketChannel.socket();
	}

}
