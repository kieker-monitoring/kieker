/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.core.controller;

import java.util.concurrent.atomic.AtomicLong;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.RegistryRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.writer.IMonitoringWriter;

/**
 * @author Andre van Hoorn, Matthias Rohr, Jan Waller, Robert von Massow
 * 
 * @since 1.3
 */
public final class WriterController extends AbstractController implements IWriterController {
	private static final Log LOG = LogFactory.getLog(WriterController.class);

	/** the total number of monitoring records received. */
	private final AtomicLong numberOfInserts = new AtomicLong(0);
	/** Monitoring Writer. */
	private final IMonitoringWriter monitoringWriter;
	/** Whether or not the {@link IMonitoringRecord#setLoggingTimestamp(long)} is automatically set. */
	private final boolean autoSetLoggingTimestamp;
	/** Whether or not to automatically log the metadata record. */
	private final boolean logMetadataRecord;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for the controller.
	 */
	public WriterController(final Configuration configuration) {
		super(configuration);
		this.logMetadataRecord = configuration.getBooleanProperty(ConfigurationFactory.METADATA);
		this.autoSetLoggingTimestamp = configuration.getBooleanProperty(ConfigurationFactory.AUTO_SET_LOGGINGTSTAMP);
		this.monitoringWriter = AbstractController.createAndInitialize(IMonitoringWriter.class,
				configuration.getStringProperty(ConfigurationFactory.WRITER_CLASSNAME),
				configuration);
		if (this.monitoringWriter == null) {
			this.terminate();
			return;
		}
	}

	@Override
	protected final void init() {
		if (this.monitoringWriter != null) {
			try {
				this.monitoringWriter.setController(super.monitoringController);
			} catch (final Exception e) { // NOPMD NOCS (IllegalCatchCheck)
				LOG.error("Error initializing writer", e);
				this.terminate();
			}
		}
	}

	@Override
	protected final void cleanup() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Shutting down Writer Controller");
		}
		if (this.monitoringWriter != null) {
			this.monitoringWriter.terminate();
		}
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder(256);
		sb.append("WriterController:\n\tNumber of Inserts: '");
		sb.append(this.getNumberOfInserts());
		sb.append("'\n\tAutomatic assignment of logging timestamps: '");
		sb.append(this.autoSetLoggingTimestamp);
		sb.append("'\n");
		if (this.monitoringWriter != null) {
			sb.append(this.monitoringWriter.toString());
		} else {
			sb.append("\tNo Monitoring Writer available");
		}
		sb.append('\n');
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean newMonitoringRecord(final IMonitoringRecord record) {
		try {
			// fast lane for RegistryRecords (these must always be delivered without blocking!)
			if (!(record instanceof RegistryRecord)) {
				final IMonitoringController monitoringController = super.monitoringController;
				if (!monitoringController.isMonitoringEnabled()) { // enabled and not terminated
					return false;
				}
				if (this.autoSetLoggingTimestamp) {
					record.setLoggingTimestamp(monitoringController.getTimeSource().getTime());
				}
				if ((0L == this.numberOfInserts.getAndIncrement()) && this.logMetadataRecord) {
					this.monitoringController.sendMetadataAsRecord();
				}
				if (!this.monitoringWriter.newMonitoringRecord(record)) {
					LOG.error("Error writing the monitoring data. Will terminate monitoring!");
					this.terminate();
					return false;
				}
			} else { // registry record
				if (!this.monitoringWriter.newMonitoringRecordNonBlocking(record)) {
					LOG.error("Error writing the monitoring data. Will terminate monitoring!");
					this.terminate();
					return false;
				}
			}
			return true;
		} catch (final Exception ex) { // NOPMD NOCS (IllegalCatchCheck)
			LOG.error("Exception detected. Will terminate monitoring", ex);
			this.terminate();
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final long getNumberOfInserts() {
		return this.numberOfInserts.longValue();
	}
}
