package kieker.monitoring.writer.tcp;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import kieker.common.configuration.Configuration;

public class SingleSocketTcpWriterTest {

	private Configuration configuration;

	@Before
	public void before() throws IOException {
		this.configuration = new Configuration();
		this.configuration.setProperty(SingleSocketTcpWriter.CONFIG_HOSTNAME, "localhost");
		this.configuration.setProperty(SingleSocketTcpWriter.CONFIG_PORT, 15469);
	}

	@After
	public void after() {
		// do nothing
	}

	@Test(expected = IllegalStateException.class)
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
//		final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()
//				.bind(new InetSocketAddress("localhost", 15469));
//		serverSocketChannel.accept();

		final SingleSocketTcpWriter writer = new SingleSocketTcpWriter(this.configuration);

		try {
			writer.onStarting();
		} finally {
			writer.onTerminating();
		}
	}

	@Test
	public void shouldReconnect() throws Exception {
		this.configuration.setProperty(SingleSocketTcpWriter.CONFIG_CONN_TIMEOUT_IN_MS, 1000);

		final SingleSocketTcpWriter writer = new SingleSocketTcpWriter(this.configuration);

		try {
			writer.onStarting();
		} finally {
			writer.onTerminating();
		}
	}

	@Test
	public void shouldFailReconnection() throws Exception {
		this.configuration.setProperty(SingleSocketTcpWriter.CONFIG_CONN_TIMEOUT_IN_MS, 1000);

		final SingleSocketTcpWriter writer = new SingleSocketTcpWriter(this.configuration);

		try {
			writer.onStarting();
		} finally {
			writer.onTerminating();
		}
	}
}
