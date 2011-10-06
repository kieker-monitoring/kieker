/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.analysis.reader.namedRecordPipe;

import java.util.concurrent.CountDownLatch;

import kieker.analysis.reader.AbstractMonitoringReader;
import kieker.analysis.util.PropertyMap;
import kieker.common.namedRecordPipe.Broker;
import kieker.common.namedRecordPipe.IPipeReader;
import kieker.common.namedRecordPipe.Pipe;
import kieker.common.record.IMonitoringRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Andre van Hoorn
 */
public final class PipeReader extends AbstractMonitoringReader implements IPipeReader {
	public static final String PROPERTY_PIPE_NAME = "pipeName";
	private static final Log LOG = LogFactory.getLog(PipeReader.class);

	private volatile Pipe pipe;

	public PipeReader() {}

	public PipeReader(final String pipeName) {
		initPipe(pipeName);
	}

	private final CountDownLatch terminationLatch = new CountDownLatch(1);

	private void initPipe(final String pipeName) throws IllegalArgumentException {
		this.pipe = Broker.getInstance().acquirePipe(pipeName);
		if (this.pipe == null) {
			PipeReader.LOG.error("Failed to get Pipe with name " + pipeName);
			throw new IllegalArgumentException("Failed to get Pipe with name " + pipeName);
		} else {
			PipeReader.LOG.debug("Connectod to named pipe '" + this.pipe.getName() + "'");
		}
		this.pipe.setPipeReader(this);
	}

	/**
	 * Blocks until the associated pipe is being closed.
	 */
	@Override
	public boolean read() {
		// No need to initialize since we receive asynchronously
		try {
			this.terminationLatch.await();
			PipeReader.LOG.info("Pipe closed. Will terminate.");
		} catch (final InterruptedException e) {
			PipeReader.LOG.error("Received InterruptedException", e);
			return false;
		}
		return true;
	}

	@Override
	public boolean init(final String initString) {
		try {
			final PropertyMap propertyMap = new PropertyMap(initString, "|", "="); // throws
			// IllegalArgumentException
			initPipe(propertyMap.getProperty(PipeReader.PROPERTY_PIPE_NAME));
			PipeReader.LOG.debug("Connected to pipe '" + this.pipe.getName() + "'" + " (" + this.pipe + ")");
		} catch (final Exception exc) {
			PipeReader.LOG.error("Failed to parse initString '" + initString + "': " + exc.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public boolean newMonitoringRecord(final IMonitoringRecord rec) {
		return super.deliverRecord(rec);
	}

	@Override
	public void notifyPipeClosed() {
		/* Notify main thread */
		this.terminationLatch.countDown();
	}

	@Override
	public void terminate() {
		// will lead to notifyPipeClosed() and the subsequent termination of read()
		this.pipe.close();
	}
}
