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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Andre van Hoorn
 */
public class FilesystemLogReplayer {

	private static final Log log = LogFactory
			.getLog(FilesystemLogReplayer.class);
	private final IMonitoringRecordReceiver recordReceiver;
	private final String[] inputDirs;
	private final boolean realtimeMode;
	private final int numRealtimeWorkerThreads;

	@SuppressWarnings("unused")
	private FilesystemLogReplayer() {
		this.recordReceiver = null;
		this.inputDirs = null;
		this.realtimeMode = false;
		this.numRealtimeWorkerThreads = -1;
	}

	/** Normal replay mode (i.e., non-real-time). */
	public FilesystemLogReplayer(
			final IMonitoringRecordReceiver monitoringController,
			final String[] inputDirs) {
		this.recordReceiver = monitoringController;
		this.inputDirs = inputDirs;
		this.realtimeMode = false;
		this.numRealtimeWorkerThreads = -1;
	}

	public FilesystemLogReplayer(
			final IMonitoringRecordReceiver monitoringController,
			final String[] inputDirs,
			final boolean realtimeMode, final int numRealtimeWorkerThreads) {
		this.recordReceiver = monitoringController;
		this.inputDirs = inputDirs;
		this.realtimeMode = realtimeMode;
		this.numRealtimeWorkerThreads = numRealtimeWorkerThreads;
	}

	/**
	 * @return true on success; false otherwise
	 */
	public boolean replay() {
		boolean success = true;


		final IMonitoringRecordConsumerPlugin logCons = new IMonitoringRecordConsumerPlugin() {

			/**
			 * Anonymous consumer class that simply passes all records to the
			 * controller
			 */
			@Override
			public Collection<Class<? extends IMonitoringRecord>> getRecordTypeSubscriptionList() {
				return null; // consume all types
			}

			@Override
			public boolean newMonitoringRecord(
					final IMonitoringRecord monitoringRecord) {
				return FilesystemLogReplayer.this.recordReceiver
						.newMonitoringRecord(monitoringRecord);
			}

			@Override
			public boolean execute() {
				return true; // no need to do anything
			}

			@Override
			public void terminate(final boolean error) {
				// no need to do anything
			}
		};
		AbstractMonitoringLogReader fsReader;
		if (this.realtimeMode) {
			fsReader = new FSReaderRealtime(this.inputDirs,
					this.numRealtimeWorkerThreads);
		} else {
			fsReader = new FSReader(this.inputDirs);
		}
		final AnalysisController tpanInstance = new AnalysisController();
		tpanInstance.setLogReader(fsReader);
		tpanInstance.registerPlugin(logCons);
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
