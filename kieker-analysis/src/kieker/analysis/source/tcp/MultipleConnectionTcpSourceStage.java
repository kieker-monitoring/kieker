/***************************************************************************
 * Copyright 2019 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.source.tcp;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import kieker.analysis.source.rewriter.ITraceMetadataRewriter;
import kieker.common.record.IMonitoringRecord;

import teetime.framework.AbstractProducerStage;

/**
 * This is a multi connection tcp source stage.
 *
 * @author Reiner Jung
 * @since 1.15
 */
public class MultipleConnectionTcpSourceStage extends AbstractProducerStage<IMonitoringRecord> {

	/** server input port. */
	private final int inputPort;
	private final int bufferSize;

	private final ITraceMetadataRewriter recordRewriter;

	/**
	 * Create a single threaded multi connection tcp reader stage.
	 *
	 * @param inputPort
	 *            used to accept <code>IMonitoringRecord</code>s and string registry entries.
	 * @param bufferSize
	 *            capacity of the receiving buffer
	 * @param recordRewriter
	 *            rewriting records
	 */
	public MultipleConnectionTcpSourceStage(final int inputPort, final int bufferSize,
			final ITraceMetadataRewriter recordRewriter) {
		this.inputPort = inputPort;
		this.bufferSize = bufferSize;
		this.recordRewriter = recordRewriter;
	}

	@Override
	protected void execute() {
		try {
			final ServerSocketChannel serverSocket = ServerSocketChannel.open();
			serverSocket.bind(new InetSocketAddress(this.inputPort));
			serverSocket.configureBlocking(true);
			final Selector readSelector = Selector.open();
			final ReaderThread reader = new ReaderThread(this.logger, readSelector, this.recordRewriter, this.outputPort);
			reader.start();

			while (this.isActive()) {
				final SocketChannel socketChannel = serverSocket.accept();

				if (socketChannel != null) {
					this.logger.debug("Connection from {}.", socketChannel.getRemoteAddress().toString());
					// add socketChannel to list of channels
					socketChannel.configureBlocking(false);

					readSelector.wakeup();
					final SelectionKey key = socketChannel.register(readSelector, SelectionKey.OP_READ);
					final Connection connection = new Connection(socketChannel, this.bufferSize);
					key.attach(connection);
				}
			}
			reader.terminate();
			reader.join();
		} catch (final ClosedByInterruptException e) {
			this.logger.info("External shutdown called");
		} catch (final BindException e) {
			this.logger.error("Cannot estabilsh listening port: Address {} is already in use.", this.inputPort);
		} catch (final IOException e) {
			this.logger.error("Cannot establish listening port", e);
		} catch (final InterruptedException e) {
			this.logger.error("Reader termination was interrupted.", e);
		} finally {
			this.workCompleted();
		}
	}

}
