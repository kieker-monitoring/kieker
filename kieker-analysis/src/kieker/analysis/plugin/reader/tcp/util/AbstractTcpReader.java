package kieker.analysis.plugin.reader.tcp.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import kieker.common.logging.Log;

// TODO remove abstract and extract the method onBufferReceived() into an interface
public abstract class AbstractTcpReader implements Runnable {

	private final int port;
	private final int bufferCapacity;
	@SuppressWarnings("PMD.LoggerIsNotStaticFinal")
	protected final Log logger;

	private volatile boolean terminated;

	public AbstractTcpReader(final int port, final int bufferCapacity, final Log logger) {
		super();
		this.port = port;
		this.bufferCapacity = bufferCapacity;
		this.logger = logger;
	}

	@Override
	public final void run() {
		ServerSocketChannel serversocket = null;
		try {
			serversocket = ServerSocketChannel.open();
			serversocket.socket().bind(new InetSocketAddress(this.port));
			if (this.logger.isDebugEnabled()) {
				this.logger.debug("Listening on port " + this.port);
			}

			final SocketChannel socketChannel = serversocket.accept();
			try {
				final ByteBuffer buffer = ByteBuffer.allocateDirect(this.bufferCapacity);
				while ((socketChannel.read(buffer) != -1) && !this.terminated) {
					this.process(buffer);
				}
			} finally {
				socketChannel.close();
			}
		} catch (final IOException ex) {
			this.logger.error("Error while reading.", ex);
		} finally {
			if (null != serversocket) {
				try {
					serversocket.close();
				} catch (final IOException e) {
					this.logger.debug("Failed to close TCP connection.", e);
				}
			}
		}
	}

	private void process(final ByteBuffer buffer) {
		buffer.flip();
		try {
			while (buffer.hasRemaining()) {
				buffer.mark();
				final boolean success = this.onBufferReceived(buffer);
				if (!success) {
					buffer.reset();
					buffer.compact();
					return;
				}
			}
			buffer.clear();
		} catch (final BufferUnderflowException ex) {
			this.logger.warn("Unexpected buffer underflow. Resetting and compacting buffer.", ex);
			buffer.reset();
			buffer.compact();
		}
	}

	/**
	 * @param buffer
	 *            to be read from
	 * @return <ul>
	 *         <li><code>true</code> when there were enough bytes to perform the read operation
	 *         <li><code>false</code> otherwise. In this case, the buffer is reset, compacted, and filled with new content.
	 */
	protected abstract boolean onBufferReceived(final ByteBuffer buffer);

	public void terminate() {
		this.terminated = true;
	}

	public int getPort() {
		return this.port;
	}
}
