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

package kieker.tools.logReplayer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.port.InputPort;
import kieker.analysis.reader.AbstractReaderPlugin;
import kieker.analysis.reader.filesystem.FSReader;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IMonitoringRecordReceiver;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.tools.util.LoggingTimestampConverter;

/**
 * Replays a filesystem monitoring log and simply passes each record to a
 * specified {@link IMonitoringRecordReceiver}. The {@link FilesystemLogReplayer} can replay monitoring logs in real-time.
 * 
 * @author Andre van Hoorn
 */
public class FilesystemLogReplayer {

	public static final long MAX_TIMESTAMP = Long.MAX_VALUE;
	public static final long MIN_TIMESTAMP = 0;

	private static final Log LOG = LogFactory.getLog(FilesystemLogReplayer.class);

	private final long ignoreRecordsBeforeTimestamp;
	private final long ignoreRecordsAfterTimestamp;

	/** Each record is delegated to this receiver. */
	private final IMonitoringController recordReceiver;
	private final String[] inputDirs;
	private final boolean realtimeMode;
	private final int numRealtimeWorkerThreads;

	/** Normal replay mode (i.e., non-real-time). */
	public FilesystemLogReplayer(final IMonitoringController monitoringController, final String[] inputDirs) {
		this(monitoringController, inputDirs, false, -1);
	}

	/**
	 * 
	 * @param monitoringController
	 * @param inputDirs
	 * @param realtimeMode
	 * @param numRealtimeWorkerThreads
	 */
	public FilesystemLogReplayer(final IMonitoringController monitoringController, final String[] inputDirs, final boolean realtimeMode,
			final int numRealtimeWorkerThreads) {
		this(monitoringController, inputDirs, realtimeMode, numRealtimeWorkerThreads, FilesystemLogReplayer.MIN_TIMESTAMP, FilesystemLogReplayer.MAX_TIMESTAMP);
	}

	/**
	 * 
	 * 
	 * @param monitoringController
	 * @param inputDirs
	 * @param realtimeMode
	 * @param numRealtimeWorkerThreads
	 * @param ignoreRecordsBeforeTimestamp
	 * @param ignoreRecordsAfterTimestamp
	 */
	public FilesystemLogReplayer(final IMonitoringController monitoringController, final String[] inputDirs, final boolean realtimeMode,
			final int numRealtimeWorkerThreads, final long ignoreRecordsBeforeTimestamp, final long ignoreRecordsAfterTimestamp) {
		this.recordReceiver = monitoringController;
		this.inputDirs = Arrays.copyOf(inputDirs, inputDirs.length);
		this.realtimeMode = realtimeMode;
		this.numRealtimeWorkerThreads = numRealtimeWorkerThreads;
		this.ignoreRecordsBeforeTimestamp = ignoreRecordsBeforeTimestamp;
		this.ignoreRecordsAfterTimestamp = ignoreRecordsAfterTimestamp;
	}

	/**
	 * Replays the monitoring log terminates after the last record was passed to
	 * the configured {@link IMonitoringRecordReceiver}.
	 * 
	 * @return true on success; false otherwise
	 */
	public boolean replay() {
		boolean success = true;

		AbstractReaderPlugin fsReader;
		if (this.realtimeMode) {
			final Configuration configuration = new Configuration();
			configuration.setProperty(FSReaderRealtime.PROP_NAME_INPUTDIRNAMES, Configuration.toProperty(this.inputDirs));
			configuration.setProperty(FSReaderRealtime.PROP_NAME_NUM_WORKERS, Integer.toString(this.numRealtimeWorkerThreads));
			fsReader = new FSReaderRealtime(configuration, new HashMap<String, AbstractRepository>());
		} else {
			final Configuration configuration = new Configuration(null);
			configuration.setProperty(FSReader.CONFIG_INPUTDIRS, Configuration.toProperty(this.inputDirs));
			fsReader = new FSReader(configuration, new HashMap<String, AbstractRepository>());
		}
		final AnalysisController tpanInstance = new AnalysisController();
		tpanInstance.setReader(fsReader);
		final RecordDelegationPlugin delegationPlugin = new RecordDelegationPlugin(this.recordReceiver, this.ignoreRecordsBeforeTimestamp,
				this.ignoreRecordsAfterTimestamp);
		tpanInstance.registerPlugin(delegationPlugin);
		AbstractPlugin.connect(fsReader, FSReader.OUTPUT_PORT_NAME, delegationPlugin, RecordDelegationPlugin.INPUT_PORT_NAME);
		try {
			tpanInstance.run();
			success = true;
		} catch (final Exception ex) { // NOCS (IllegalCatchCheck) // NOPMD
			FilesystemLogReplayer.LOG.error("Exception", ex);
			success = false;
		}
		return success;
	}
}

/**
 * Kieker analysis plugin that delegates each record to the configured {@link IMonitoringRecordReceiver}.<br>
 * 
 * <b>Don't</b> change the visibility modificator to public. The class does not have the necessary <i>Configuration</i>-Constructor in order to be used by the
 * analysis meta model. <br>
 * 
 * TODO: We need to extract this class and merge it with that of {@link JMSLogReplayer} See ticket http://samoa.informatik.uni-kiel.de:8000/kieker/ticket/172
 * 
 * @author Andre van Hoorn
 * 
 */
class RecordDelegationPlugin extends AbstractAnalysisPlugin {

	public static final String INPUT_PORT_NAME = "newMonitoringRecord";
	private static final Log LOG = LogFactory.getLog(RecordDelegationPlugin.class);

	private final IMonitoringController rec;

	private final long ignoreRecordsBeforeTimestamp;
	private final long ignoreRecordsAfterTimestamp;

	/**
	 * Data is only sent via the first input port of the given plugin.
	 * 
	 * @param rec
	 * @param ignoreRecordsBeforeTimestamp
	 * @param ignoreRecordsAfterTimestamp
	 */
	public RecordDelegationPlugin(final IMonitoringController rec, final long ignoreRecordsBeforeTimestamp, final long ignoreRecordsAfterTimestamp) {
		super(new Configuration(null), new HashMap<String, AbstractRepository>());

		this.rec = rec;
		this.ignoreRecordsBeforeTimestamp = ignoreRecordsBeforeTimestamp;
		this.ignoreRecordsAfterTimestamp = ignoreRecordsAfterTimestamp;
	}

	@InputPort(
			name = RecordDelegationPlugin.INPUT_PORT_NAME,
			eventTypes = { IMonitoringRecord.class }, description = RecordDelegationPlugin.INPUT_PORT_NAME)
	public void newMonitoringRecord(final Object data) {
		final IMonitoringRecord record = (IMonitoringRecord) data;
		if ((record.getLoggingTimestamp() >= this.ignoreRecordsBeforeTimestamp) && (record.getLoggingTimestamp() <= this.ignoreRecordsAfterTimestamp)) {
			/* Delegate the record to the monitoring controller. */
			this.rec.newMonitoringRecord(record);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean execute() {
		if (RecordDelegationPlugin.LOG.isDebugEnabled()) {
			RecordDelegationPlugin.LOG.debug(RecordDelegationPlugin.class.getName() + " starting ...");
		}
		RecordDelegationPlugin.LOG
				.info("Ignoring records before " + LoggingTimestampConverter.convertLoggingTimestampToUTCString(this.ignoreRecordsBeforeTimestamp));
		RecordDelegationPlugin.LOG.info("Ignoring records after " + LoggingTimestampConverter.convertLoggingTimestampToUTCString(this.ignoreRecordsAfterTimestamp));
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void terminate(final boolean error) {
		// nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configuration getCurrentConfiguration() {
		/* No configuration possible. */
		return new Configuration(null);
	}

	@Override
	public Map<String, AbstractRepository> getCurrentRepositories() {
		return new HashMap<String, AbstractRepository>();
	}
}
