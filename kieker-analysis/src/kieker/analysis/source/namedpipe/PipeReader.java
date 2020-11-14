/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.source.namedpipe;

import java.util.concurrent.CountDownLatch;

import kieker.common.namedRecordPipe.Broker;
import kieker.common.namedRecordPipe.IPipeReader;
import kieker.common.namedRecordPipe.Pipe;
import kieker.common.record.IMonitoringRecord;

import teetime.framework.AbstractProducerStage;

/**
 * This reader can be used to read records via an in-memory pipe.
 *
 * @author Andre van Hoorn, Lars Bluemke
 *
 * @since 1.3
 */
public class PipeReader extends AbstractProducerStage<IMonitoringRecord> implements IPipeReader {

	private volatile Pipe pipe;
	private final CountDownLatch terminationLatch = new CountDownLatch(1);

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public PipeReader(final String pipeName) {

		this.pipe = Broker.INSTANCE.acquirePipe(pipeName);
		if (this.pipe == null) {
			throw new IllegalArgumentException("Failed to get Pipe with name " + pipeName);
		} else {
			if (this.logger.isDebugEnabled()) {
				this.logger.debug("Connected to named pipe '" + this.pipe.getName() + "'");
			}
		}
		// escaping this in constructor! very bad practice!
		this.pipe.setPipeReader(this);
	}

	/**
	 * Blocks until the associated pipe is being closed.
	 */
	@Override
	protected void execute() {
		// No need to initialize since we receive asynchronously
		try {
			this.terminationLatch.await();
			this.logger.info("Pipe closed. Will terminate.");
		} catch (final InterruptedException ex) {
			this.logger.error("Received InterruptedException", ex);
		}
	}

	/**
	 * This method sends a given records directly to the output port.
	 *
	 * @param rec
	 *            The new record object.
	 * @return true if and only if the record has been delivered.
	 */
	@Override
	public boolean newMonitoringRecord(final IMonitoringRecord rec) {
		this.outputPort.send(rec);
		// after migration to teetime a return actually isn't needed
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notifyPipeClosed() {
		// Notify main thread
		this.terminationLatch.countDown();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void terminateStage() {
		// will lead to notifyPipeClosed() and the subsequent termination of read()
		if (this.pipe != null) {
			this.pipe.close();
		}
		super.terminateStage();
	}

}
