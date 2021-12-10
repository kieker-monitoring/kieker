/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.plugin.reader.namedRecordPipe;

import java.util.concurrent.CountDownLatch;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.namedRecordPipe.Broker;
import kieker.common.namedRecordPipe.IPipeReader;
import kieker.common.namedRecordPipe.Pipe;
import kieker.common.record.IMonitoringRecord;

/**
 * This reader can be used to read records via an in-memory pipe.
 *
 * @author Andre van Hoorn
 *
 * @since 1.3
 * @deprecated 1.15 ported to teetime
 */
@Deprecated
@Plugin(description = "A reader which reads records via an in-memory pipe", outputPorts = {
	@OutputPort(name = PipeReader.OUTPUT_PORT_NAME_RECORDS, eventTypes = IMonitoringRecord.class,
			description = "Output Port of the PipeReader")
}, configuration = {
	@Property(name = PipeReader.CONFIG_PROPERTY_NAME_PIPENAME,
			defaultValue = PipeReader.CONFIG_PROPERTY_VALUE_PIPENAME_DEFAULT,
			description = "The name of the pipe used to read data.")
})
public final class PipeReader extends AbstractReaderPlugin implements IPipeReader {

	/** This is the name of the default output port. */
	public static final String OUTPUT_PORT_NAME_RECORDS = "monitoringRecords";

	/** This is the configuration-parameter for the name of the pipe to be used. */
	public static final String CONFIG_PROPERTY_NAME_PIPENAME = "pipeName";
	/** The default used pipe name. */
	public static final String CONFIG_PROPERTY_VALUE_PIPENAME_DEFAULT = "kieker-pipe";

	private volatile Pipe pipe;
	private final String pipeName;
	private final CountDownLatch terminationLatch = new CountDownLatch(1);

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public PipeReader(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		final String pipeNameConfig = this.configuration.getStringProperty(CONFIG_PROPERTY_NAME_PIPENAME);

		this.pipeName = pipeNameConfig;
		this.pipe = Broker.INSTANCE.acquirePipe(pipeNameConfig);
		if (this.pipe == null) {
			throw new IllegalArgumentException("Failed to get Pipe with name " + pipeNameConfig);
		} else {
			this.logger.debug("Connected to named pipe '{}'", this.pipe.getName());
		}
		// escaping this in constructor! very bad practice!
		this.pipe.setPipeReader(this);
	}

	/**
	 * Blocks until the associated pipe is being closed.
	 *
	 * @return true if the reading terminated in a "normal" way. If an interrupt terminates the wait-method too early, false will be returned.
	 */
	@Override
	public boolean read() {
		// No need to initialize since we receive asynchronously
		try {
			this.terminationLatch.await();
			this.logger.info("Pipe closed. Will terminate.");
		} catch (final InterruptedException ex) {
			this.logger.error("Received InterruptedException", ex);
			return false;
		}
		return true;
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
		return super.deliver(OUTPUT_PORT_NAME_RECORDS, rec);
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
	public void terminate(final boolean error) {
		// will lead to notifyPipeClosed() and the subsequent termination of read()
		if (this.pipe != null) {
			this.pipe.close();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration(null);

		configuration.setProperty(CONFIG_PROPERTY_NAME_PIPENAME, this.pipeName);

		return configuration;
	}
}
