/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.common.configuration.Configuration;

/**
 * @author Christian Wulf
 * @since 1.14
 */
public class SingleSocketTcpWriterTest {

	private static final String HOSTNAME = "localhost";
	private static final int PORT = 10444;

	private Configuration configuration;

	public SingleSocketTcpWriterTest() {
		super();
	}

	@Before
	public void before() throws IOException {
		this.configuration = new Configuration();
		this.configuration.setProperty(SingleSocketTcpWriter.CONFIG_HOSTNAME, HOSTNAME);
		this.configuration.setProperty(SingleSocketTcpWriter.CONFIG_PORT, PORT);
	}

	@After
	public void after() {
		// do nothing
	}

	@Test(expected = ConnectionTimeoutException.class)
	public void shouldFailConnectingWithDefault() throws Exception {
		final SingleSocketTcpWriter writer = new SingleSocketTcpWriter(this.configuration);

		try {
			writer.onStarting();
		} finally {
			writer.onTerminating();
		}
	}

	@Test
	public void shouldConnectWithDefault() throws Exception {
		try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
			serverSocketChannel.bind(new InetSocketAddress(HOSTNAME, PORT));
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.accept(); // non-blocking accept

			final SingleSocketTcpWriter writer = new SingleSocketTcpWriter(this.configuration);
			try {
				writer.onStarting();
			} finally {
				writer.onTerminating();
			}
		}

		Assert.assertTrue(true); // NOPMD (this test should not throw any exception)
	}

	@Test(expected = ConnectionTimeoutException.class)
	public void reconnectingShouldFail() throws Exception {
		this.configuration.setProperty(SingleSocketTcpWriter.CONFIG_CONN_TIMEOUT_IN_MS, 200); // NOFB

		final SingleSocketTcpWriter writer = new SingleSocketTcpWriter(this.configuration);
		try {
			writer.onStarting();
		} finally {
			writer.onTerminating();
		}
	}

	@Test
	public void reconnectingShouldWork() throws Exception {
		final InetSocketAddress bindAddress = new InetSocketAddress(HOSTNAME, PORT);
		final int configTimeoutInMs = 1000;

		final Thread serverThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// give the writer time to fail some connection attempts
					Thread.sleep(configTimeoutInMs / 3);
				} catch (final InterruptedException e) {
					throw new IllegalStateException(e);
				}

				try (final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
					serverSocketChannel.bind(bindAddress);
					serverSocketChannel.accept(); // blocking accept
				} catch (final IOException e) {
					throw new IllegalStateException(e);
				}
			}
		});
		serverThread.start();

		try {
			this.configuration.setProperty(SingleSocketTcpWriter.CONFIG_CONN_TIMEOUT_IN_MS, configTimeoutInMs);

			final SingleSocketTcpWriter writer = new SingleSocketTcpWriter(this.configuration);
			try {
				writer.onStarting();
			} finally {
				writer.onTerminating();
			}
		} finally {
			serverThread.join(2000);
		}

		MatcherAssert.assertThat(serverThread.isAlive(), Matchers.is(false)); // NOPMD (JUnit message is not necessary)
	}

}
