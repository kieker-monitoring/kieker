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
import java.util.StringTokenizer;
import java.util.concurrent.CountDownLatch;

import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.IMonitoringRecordConsumerPlugin;
import kieker.analysis.reader.AbstractMonitoringLogReader;
import kieker.analysis.reader.filesystem.FSReader;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.PropertyMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Andre van Hoorn
 */
public class FSReaderRealtime extends AbstractMonitoringLogReader {

	private static final Log log = LogFactory.getLog(FSReaderRealtime.class);

	/* manages the life-cycle of the reader and consumers */
	private final AnalysisController tpanInstance = new AnalysisController();
	private RealtimeReplayDistributor rtDistributor = null;
	private static final String PROP_NAME_NUM_WORKERS = "numWorkers";
	private static final String PROP_NAME_INPUTDIRNAMES = "inputDirs";
	/** Reader will wait for this latch before read() returns */
	private final CountDownLatch terminationLatch = new CountDownLatch(1);

	/**
	 * Acts as a consumer to the rtDistributor and delegates incoming records to
	 * the FSReaderRealtime instance.
	 */
	private class FSReaderRealtimeCons implements
			IMonitoringRecordConsumerPlugin {

		private final FSReaderRealtime master;

		public FSReaderRealtimeCons(final FSReaderRealtime master) {
			this.master = master;
		}

		@Override
		public Collection<Class<? extends IMonitoringRecord>> getRecordTypeSubscriptionList() {
			return null;
		}

		@Override
		public boolean newMonitoringRecord(
				final IMonitoringRecord monitoringRecord) {
			if (!this.master.deliverRecord(monitoringRecord)) {
				FSReaderRealtime.log.error("LogReaderExecutionException");
				return false;
			}
			return true;
		}

		@Override
		public boolean execute() {
			/* do nothing */
			return true;
		}

		@Override
		public void terminate(final boolean error) {
		}
	}

	/**
	 * Constructor for FSReaderRealtime. Requires a subsequent call to the init
	 * method in order to specify the input directory and number of workers
	 * using the parameter @a inputDirName.
	 */
	public FSReaderRealtime() {
	}

	/**
	 * Valid key/value pair: inputDirNames=INPUTDIRECTORY1;...;INPUTDIRECTORYN |
	 * numWorkers=XX
	 */
	@Override
	public boolean init(final String initString) {
		try {
			final PropertyMap propertyMap = new PropertyMap(initString, "|",
					"="); // throws IllegalArgumentException
			final String numWorkersString = propertyMap
					.getProperty(FSReaderRealtime.PROP_NAME_NUM_WORKERS);
			int numWorkers = -1;
			if (numWorkersString == null) {
				throw new IllegalArgumentException("Missing init parameter '"
						+ FSReaderRealtime.PROP_NAME_NUM_WORKERS + "'");
			}
			try {
				numWorkers = Integer.parseInt(numWorkersString);
			} catch (final NumberFormatException ex) { /*
														 * value of numWorkers
														 * remains -1
														 */
			}

			this.initInstanceFromArgs(this.inputDirNameListToArray(propertyMap
					.getProperty(FSReaderRealtime.PROP_NAME_INPUTDIRNAMES)),
					numWorkers);
		} catch (final Exception exc) {
			FSReaderRealtime.log.error("Failed to initString '"
					+ initString + "': " + exc.getMessage());
			return false;
		}
		return true;
	}

	public FSReaderRealtime(final String[] inputDirNames, final int numWorkers) {
		this.initInstanceFromArgs(inputDirNames, numWorkers);
	}

	private String[] inputDirNameListToArray(final String inputDirNameList)
			throws IllegalArgumentException {
		String[] dirNameArray;

		// parse inputDir property value
		if ((inputDirNameList == null)
				|| (inputDirNameList.trim().length() == 0)) {
			FSReaderRealtime.log
					.error("Invalid argument value for inputDirNameList:"
							+ inputDirNameList);
			throw new IllegalArgumentException(
					"Invalid argument value for inputDirNameList:"
							+ inputDirNameList);
		}
		try {
			final StringTokenizer dirNameTokenizer = new StringTokenizer(
					inputDirNameList, ";");
			dirNameArray = new String[dirNameTokenizer.countTokens()];
			for (int i = 0; dirNameTokenizer.hasMoreTokens(); i++) {
				dirNameArray[i] = dirNameTokenizer.nextToken().trim();
			}
		} catch (final Exception exc) {
			throw new IllegalArgumentException(
					"Error parsing list of input directories'"
							+ inputDirNameList + "'", exc);
		}
		return dirNameArray;
	}

	private void initInstanceFromArgs(final String[] inputDirNames,
			final int numWorkers) throws IllegalArgumentException {
		if ((inputDirNames == null) || (inputDirNames.length <= 0)) {
			throw new IllegalArgumentException("Invalid property value for "
					+ FSReaderRealtime.PROP_NAME_INPUTDIRNAMES + ":"
					+ inputDirNames);
		}

		if (numWorkers <= 0) {
			throw new IllegalArgumentException("Invalid property value for "
					+ FSReaderRealtime.PROP_NAME_NUM_WORKERS + ": "
					+ numWorkers);
		}

		final AbstractMonitoringLogReader fsReader = new FSReader(inputDirNames);
		final IMonitoringRecordConsumerPlugin rtCons = new FSReaderRealtimeCons(
				this);
		this.rtDistributor = new RealtimeReplayDistributor(numWorkers, rtCons,
				this.terminationLatch);
		// fsReader.addConsumer(rtDistributor, null);
		this.tpanInstance.setLogReader(fsReader);
		this.tpanInstance.registerPlugin(this.rtDistributor);
	}

	/**
	 * Replays the monitoring log in real-time and returns after the complete
	 * log was being replayed.
	 */
	@Override
	public boolean read() {
		final boolean success = true;
		try {
			this.tpanInstance.run();
			this.terminationLatch.await();
		} catch (final Exception ex) {
			FSReaderRealtime.log.error("An error occured while reading", ex);
			return false;
		}
		return success;
	}
}
