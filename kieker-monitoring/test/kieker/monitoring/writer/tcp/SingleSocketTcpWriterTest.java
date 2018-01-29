package kieker.monitoring.writer.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import kieker.common.configuration.Configuration;

public class SingleSocketTcpWriterTest {

	private static final String HOSTNAME = "localhost";
	private static final int PORT = 10444;

	private Configuration configuration;

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
		final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		try {
			serverSocketChannel.bind(new InetSocketAddress(HOSTNAME, PORT));
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.accept();// non-blocking accept

			final SingleSocketTcpWriter writer = new SingleSocketTcpWriter(this.configuration);
			try {
				writer.onStarting();
			} finally {
				writer.onTerminating();
			}

		} finally {
			serverSocketChannel.close();
		}
	}

	@Test(expected = ConnectionTimeoutException.class)
	public void reconnectingShouldFail() throws Exception {
		this.configuration.setProperty(SingleSocketTcpWriter.CONFIG_CONN_TIMEOUT_IN_MS, 500);

		final SingleSocketTcpWriter writer = new SingleSocketTcpWriter(this.configuration);
		try {
			writer.onStarting();
		} finally {
			writer.onTerminating();
		}
	}

	@Test
	public void reconnectingShouldWork() throws Exception {
		final int configTimeoutInMs = 1000;

		final Thread serverThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// give the writer time to fail some connection attempts
					Thread.sleep(configTimeoutInMs / 3);
				} catch (InterruptedException e) {
					throw new IllegalStateException(e);
				}

				try {
					final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
					serverSocketChannel.bind(new InetSocketAddress(HOSTNAME, PORT));
					serverSocketChannel.accept();// blocking accept
				} catch (IOException e) {
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
			serverThread.join(10000);
		}
	}

}
