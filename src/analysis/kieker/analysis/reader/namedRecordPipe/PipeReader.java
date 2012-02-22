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

import kieker.analysis.plugin.AbstractReaderPlugin;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.namedRecordPipe.Broker;
import kieker.common.namedRecordPipe.IPipeReader;
import kieker.common.namedRecordPipe.Pipe;
import kieker.common.record.IMonitoringRecord;

/**
 * 
 * @author Andre van Hoorn
 */
@Plugin(outputPorts = @OutputPort(name = PipeReader.OUTPUT_PORT_NAME, eventTypes = { IMonitoringRecord.class }, description = "Output Port of the PipeReader"))
public final class PipeReader extends AbstractReaderPlugin implements IPipeReader {

	public static final String OUTPUT_PORT_NAME = "defaultOutput";
	public static final String CONFIG_PIPENAME = PipeReader.class.getName() + ".pipeName";

	private static final Log LOG = LogFactory.getLog(PipeReader.class);

	private volatile Pipe pipe;
	private final String pipeName;
	private final CountDownLatch terminationLatch = new CountDownLatch(1);

	/**
	 * Creates a new instance of this class using the given parameter.
	 * 
	 * @param configuration
	 *            The configuration used to load the pipe name. It <b>must</b> contain the property {@link CONFIG_PIPENAME}.
	 * @throws IllegalArgumentException
	 *             If the pipe name was invalid.
	 */
	public PipeReader(final Configuration configuration) throws IllegalArgumentException {
		super(configuration);
		final String pipeName = this.configuration.getStringProperty(PipeReader.CONFIG_PIPENAME);

		this.pipeName = pipeName;
		this.initialize(pipeName);
	}

	// TODO Remove the constructor
	public PipeReader(final String pipeName) {
		super(new Configuration(null));

		this.pipeName = pipeName;
		this.initialize(pipeName);
	}

	private void initialize(final String pipeName) throws IllegalArgumentException {
		this.pipe = Broker.INSTANCE.acquirePipe(pipeName);
		if (this.pipe == null) {
			throw new IllegalArgumentException("Failed to get Pipe with name " + pipeName);
		} else {
			if (PipeReader.LOG.isDebugEnabled()) {
				PipeReader.LOG.debug("Connected to named pipe '" + this.pipe.getName() + "'"); // NOCS (MultipleStringLiteralsCheck)
			}
		}
		// TODO: escaping this in constructor! very bad practice!
		this.pipe.setPipeReader(this);
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration defaultConfiguration = new Configuration();
		defaultConfiguration.setProperty(PipeReader.CONFIG_PIPENAME, "kieker-pipe");
		return defaultConfiguration;
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
	public boolean newMonitoringRecord(final IMonitoringRecord rec) {
		return super.deliver(PipeReader.OUTPUT_PORT_NAME, rec);
	}

	@Override
	public void notifyPipeClosed() {
		/* Notify main thread */
		this.terminationLatch.countDown();
	}

	@Override
	public void terminate(final boolean error) {
		// will lead to notifyPipeClosed() and the subsequent termination of read()
		if (this.pipe != null) {
			this.pipe.close();
		}
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration(null);

		configuration.setProperty(PipeReader.CONFIG_PIPENAME, this.pipeName);

		return configuration;
	}
}
