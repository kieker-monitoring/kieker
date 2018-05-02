/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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
package kieker.common.record.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;

/**
 * Represents a TCP reader without any knowledge about Kieker in general and records in particular.
 *
 * @author Christian Wulf (chw)
 *
 * @since 1.13
 */
// BETTER remove abstract and extract the method onBufferReceived() into an interface
public abstract class AbstractTcpReader implements Runnable {

	private static final int CONNECTION_CLOSED_BY_CLIENT = -1;

	protected final Logger logger;

	private final int port;
	private final int bufferCapacity;
	private volatile boolean terminated;

	/**
	 * Constructs a new TCP reader.
	 *
	 * @param port
	 *            on which to listen for requests
	 * @param bufferCapacity
	 *            of the used read buffer
	 * @param logger
	 *            for notification to users and developers
	 */
	public AbstractTcpReader(final int port, final int bufferCapacity, final Logger logger) {
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
			this.logger.debug("Listening on port {}", this.port);

			final SocketChannel socketChannel = serversocket.accept();
			try {
				final ByteBuffer buffer = ByteBuffer.allocateDirect(this.bufferCapacity);
				while ((socketChannel.read(buffer) != CONNECTION_CLOSED_BY_CLIENT) && !this.terminated) {
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
	 * @return
	 *         <ul>
	 *         <li><code>true</code> when there were enough bytes to perform the read operation
	 *         <li><code>false</code> otherwise. In this case, the buffer is reset, compacted, and filled with new content.
	 */
	protected abstract boolean onBufferReceived(final ByteBuffer buffer);

	/**
	 * Gracefully terminates this TCP reader.
	 */
	public void terminate() {
		this.terminated = true;
	}

	public int getPort() {
		return this.port;
	}
}
