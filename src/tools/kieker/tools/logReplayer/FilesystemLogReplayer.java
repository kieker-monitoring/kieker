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
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import kieker.analysis.AnalysisController;
import kieker.analysis.configuration.Configuration;
import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.configuration.OutputPort;
import kieker.analysis.reader.AbstractMonitoringReader;
import kieker.analysis.reader.filesystem.FSReader;
import kieker.common.configuration.AbstractConfiguration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IMonitoringRecordReceiver;
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
	private final AbstractAnalysisPlugin recordReceiver;
	private final String[] inputDirs;
	private final boolean realtimeMode;
	private final int numRealtimeWorkerThreads;

	/** Normal replay mode (i.e., non-real-time). */
	public FilesystemLogReplayer(final AbstractAnalysisPlugin monitoringController, final String[] inputDirs) {
		this(monitoringController, inputDirs, false, -1);
	}

	/**
	 * 
	 * @param monitoringController
	 * @param inputDirs
	 * @param realtimeMode
	 * @param numRealtimeWorkerThreads
	 */
	public FilesystemLogReplayer(final AbstractAnalysisPlugin monitoringController, final String[] inputDirs, final boolean realtimeMode,
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
	public FilesystemLogReplayer(final AbstractAnalysisPlugin monitoringController, final String[] inputDirs, final boolean realtimeMode,
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

		AbstractMonitoringReader fsReader;
		if (this.realtimeMode) {
			fsReader = new FSReaderRealtime(this.inputDirs, this.numRealtimeWorkerThreads);
		} else {
			final Configuration configuration = new Configuration(null);
			configuration.setProperty(FSReader.CONFIG_INPUTDIRS, AbstractConfiguration.toProperty(this.inputDirs));
			fsReader = new FSReader(configuration);
		}
		final AnalysisController tpanInstance = new AnalysisController();
		tpanInstance.setReader(fsReader);
		tpanInstance.registerPlugin(new RecordDelegationPlugin(this.recordReceiver, this.ignoreRecordsBeforeTimestamp, this.ignoreRecordsAfterTimestamp));
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
 * Kieker analysis plugin that delegates each record to the configured {@link IMonitoringRecordReceiver}.
 * 
 * TODO: We need to extract this class and merge it with that of {@link JMSLogReplayer} See ticket http://samoa.informatik.uni-kiel.de:8000/kieker/ticket/172
 * 
 * @author Andre van Hoorn
 * 
 */
class RecordDelegationPlugin extends AbstractAnalysisPlugin {

	private static final Log LOG = LogFactory.getLog(RecordDelegationPlugin.class);

	private final AbstractAnalysisPlugin rec;

	private final long ignoreRecordsBeforeTimestamp;
	private final long ignoreRecordsAfterTimestamp;

	private static final Collection<Class<?>> OUT_CLASSES = Collections.unmodifiableCollection(new CopyOnWriteArrayList<Class<?>>(
			new Class<?>[] { IMonitoringRecord.class }));
	private static final Collection<Class<?>> IN_CLASSES = Collections.unmodifiableCollection(new CopyOnWriteArrayList<Class<?>>(
			new Class<?>[] { IMonitoringRecord.class }));

	private final AbstractInputPort input = new AbstractInputPort("in", RecordDelegationPlugin.IN_CLASSES) {
		@Override
		public void newEvent(final Object event) {
			RecordDelegationPlugin.this.newMonitoringRecord((IMonitoringRecord) event);
		}
	};

	private final OutputPort output = new OutputPort("out", RecordDelegationPlugin.OUT_CLASSES);

	public RecordDelegationPlugin(final Configuration configuration) {
		super(configuration);

		// TODO: Load from configuration.
		this.rec = null;
		this.ignoreRecordsBeforeTimestamp = 0;
		this.ignoreRecordsAfterTimestamp = 0;

		super.registerInputPort("in", input);
		super.registerOutputPort("out", output);
	}

	/**
	 * Data is only sent via the first input port of the given plugin.
	 * 
	 * @param rec
	 * @param ignoreRecordsBeforeTimestamp
	 * @param ignoreRecordsAfterTimestamp
	 */
	public RecordDelegationPlugin(final AbstractAnalysisPlugin rec, final long ignoreRecordsBeforeTimestamp, final long ignoreRecordsAfterTimestamp) {
		super(new Configuration(null));

		this.rec = rec;
		this.output.subscribe(this.rec.getAllInputPorts()[0]);
		this.ignoreRecordsBeforeTimestamp = ignoreRecordsBeforeTimestamp;
		this.ignoreRecordsAfterTimestamp = ignoreRecordsAfterTimestamp;

		super.registerInputPort("in", input);
		super.registerOutputPort("out", output);
	}

	private void newMonitoringRecord(final IMonitoringRecord record) {
		if ((record.getLoggingTimestamp() >= this.ignoreRecordsBeforeTimestamp) && (record.getLoggingTimestamp() <= this.ignoreRecordsAfterTimestamp)) {
			this.output.deliver(record);
		}
	}

	/*
	 * {@inheritdoc}
	 */
	@Override
	public boolean execute() {
		RecordDelegationPlugin.LOG.debug(RecordDelegationPlugin.class.getName() + " starting ...");
		RecordDelegationPlugin.LOG
				.info("Ignoring records before " + LoggingTimestampConverter.convertLoggingTimestampToUTCString(this.ignoreRecordsBeforeTimestamp));
		RecordDelegationPlugin.LOG.info("Ignoring records after " + LoggingTimestampConverter.convertLoggingTimestampToUTCString(this.ignoreRecordsAfterTimestamp));
		return true;
	}

	/*
	 * {@inheritdoc}
	 */
	@Override
	public void terminate(final boolean error) {
		// nothing to do
	}

	@Override
	protected Properties getDefaultProperties() {
		return new Properties();
	}
}
