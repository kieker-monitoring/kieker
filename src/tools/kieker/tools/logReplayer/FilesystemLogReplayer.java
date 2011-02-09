package kieker.tools.logReplayer;

/*
* ==================LICENCE=========================
* Copyright 2006-2009 Kieker Project
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
* ==================================================
*
*/

import java.util.Collection;

import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.IMonitoringRecordConsumerPlugin;
import kieker.analysis.reader.AbstractMonitoringLogReader;
import kieker.analysis.reader.filesystem.FSReader;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IMonitoringRecordReceiver;
import kieker.tools.util.LoggingTimestampConverter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Replays a filesystem monitoring log and simply passes each record to a
 * specified {@link IMonitoringRecordReceiver}. The
 * {@link FilesystemLogReplayer} can replay monitoring logs in real-time.
 * 
 * @author Andre van Hoorn
 */
public class FilesystemLogReplayer {

	public static final long MAX_TIMESTAMP = Long.MAX_VALUE;
	public static final long MIN_TIMESTAMP = 0;

	private final long ignoreRecordsBeforeTimestamp;
	private final long ignoreRecordsAfterTimestamp;

	private static final Log log = LogFactory
			.getLog(FilesystemLogReplayer.class);
	/** Each record is delegated to this receiver. */
	private final IMonitoringRecordReceiver recordReceiver;
	private final String[] inputDirs;
	private final boolean realtimeMode;
	private final int numRealtimeWorkerThreads;

	/** Must not be used for construction */
	@SuppressWarnings("unused")
	private FilesystemLogReplayer() {
		this(null, null);
	}

	/** Normal replay mode (i.e., non-real-time). */
	public FilesystemLogReplayer(
			final IMonitoringRecordReceiver monitoringController,
			final String[] inputDirs) {
		this(monitoringController, inputDirs, false, -1);
	}

	/**
	 * 
	 * @param monitoringController
	 * @param inputDirs
	 * @param realtimeMode
	 * @param numRealtimeWorkerThreads
	 */
	public FilesystemLogReplayer(
			final IMonitoringRecordReceiver monitoringController,
			final String[] inputDirs, final boolean realtimeMode,
			final int numRealtimeWorkerThreads) {
		this(monitoringController, inputDirs, realtimeMode,
				numRealtimeWorkerThreads, FilesystemLogReplayer.MIN_TIMESTAMP,
				FilesystemLogReplayer.MAX_TIMESTAMP);
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
	public FilesystemLogReplayer(
			final IMonitoringRecordReceiver monitoringController,
			final String[] inputDirs, final boolean realtimeMode,
			final int numRealtimeWorkerThreads,
			final long ignoreRecordsBeforeTimestamp,
			final long ignoreRecordsAfterTimestamp) {
		this.recordReceiver = monitoringController;
		this.inputDirs = inputDirs;
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

		AbstractMonitoringLogReader fsReader;
		if (this.realtimeMode) {
			fsReader =
					new FSReaderRealtime(this.inputDirs,
							this.numRealtimeWorkerThreads);
		} else {
			fsReader = new FSReader(this.inputDirs);
		}
		final AnalysisController tpanInstance = new AnalysisController();
		tpanInstance.setLogReader(fsReader);
		tpanInstance.registerPlugin(new RecordDelegationPlugin(
				this.recordReceiver, this.ignoreRecordsBeforeTimestamp,
				this.ignoreRecordsAfterTimestamp));
		try {
			tpanInstance.run();
			success = true;
		} catch (final Exception ex) {
			FilesystemLogReplayer.log.error("Exception", ex);
			success = false;
		}
		return success;
	}
}

/**
 * Kieker analysis plugin that delegates each record to the configured
 * {@link IMonitoringRecordReceiver}.
 * 
 * TODO: We need to extract this class and merge it with that of
 * {@link JMSLogReplayer}
 * 
 * @author Andre van Hoorn
 * 
 */
class RecordDelegationPlugin implements IMonitoringRecordConsumerPlugin {

	private static final Log log = LogFactory
			.getLog(RecordDelegationPlugin.class);

	private final IMonitoringRecordReceiver rec;

	private final long ignoreRecordsBeforeTimestamp;
	private final long ignoreRecordsAfterTimestamp;

	/**
	 * Must not be used for construction.
	 */
	@SuppressWarnings("unused")
	private RecordDelegationPlugin() {
		this(null, FilesystemLogReplayer.MIN_TIMESTAMP,
				FilesystemLogReplayer.MAX_TIMESTAMP);
	}

	public RecordDelegationPlugin(final IMonitoringRecordReceiver rec,
			final long ignoreRecordsBeforeTimestamp,
			final long ignoreRecordsAfterTimestamp) {
		this.rec = rec;
		this.ignoreRecordsBeforeTimestamp = ignoreRecordsBeforeTimestamp;
		this.ignoreRecordsAfterTimestamp = ignoreRecordsAfterTimestamp;
	}

	/*
	 * {@inheritdoc}
	 */
	@Override
	public boolean newMonitoringRecord(final IMonitoringRecord record) {
		if ((record.getLoggingTimestamp() < this.ignoreRecordsBeforeTimestamp)
				|| (record.getLoggingTimestamp() > this.ignoreRecordsAfterTimestamp)) {
			return true;
		}
		return this.rec.newMonitoringRecord(record);
	}

	/*
	 * {@inheritdoc}
	 */
	@Override
	public boolean execute() {
		RecordDelegationPlugin.log.info(RecordDelegationPlugin.class.getName()
				+ " starting ...");
		RecordDelegationPlugin.log
				.info("Ignoring records before "
						+ LoggingTimestampConverter
								.convertLoggingTimestampToUTCString(this.ignoreRecordsBeforeTimestamp));
		RecordDelegationPlugin.log
				.info("Ignoring records after "
						+ LoggingTimestampConverter
								.convertLoggingTimestampToUTCString(this.ignoreRecordsAfterTimestamp));
		return true;
	}

	/*
	 * {@inheritdoc}
	 */
	@Override
	public void terminate(final boolean error) {
		return;
	}

	/*
	 * {@inheritdoc}
	 */
	@Override
	public Collection<Class<? extends IMonitoringRecord>> getRecordTypeSubscriptionList() {
		return null; // receive records of any type
	}
}