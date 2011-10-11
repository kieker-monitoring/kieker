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
import java.util.StringTokenizer;
import java.util.concurrent.CountDownLatch;

import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.IMonitoringRecordConsumerPlugin;
import kieker.analysis.reader.AbstractMonitoringReader;
import kieker.analysis.reader.filesystem.FSReader;
import kieker.analysis.util.PropertyMap;
import kieker.common.record.IMonitoringRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Andre van Hoorn
 */
public class FSReaderRealtime extends AbstractMonitoringReader {
	private static final Log LOG = LogFactory.getLog(FSReaderRealtime.class);

	private static final String PROP_NAME_NUM_WORKERS = "numWorkers";
	private static final String PROP_NAME_INPUTDIRNAMES = "inputDirs";

	/* manages the life-cycle of the reader and consumers */
	private final AnalysisController analysis = new AnalysisController();
	private RealtimeReplayDistributor rtDistributor = null;
	/** Reader will wait for this latch before read() returns */
	private final CountDownLatch terminationLatch = new CountDownLatch(1);

	/**
	 * Constructor for FSReaderRealtime. Requires a subsequent call to the init
	 * method in order to specify the input directory and number of workers
	 * using the parameter @a inputDirName.
	 */
	public FSReaderRealtime() {
		// nothing to do
	}

	public FSReaderRealtime(final String[] inputDirNames, final int numWorkers) {
		this.initInstanceFromArgs(inputDirNames, numWorkers);
	}

	/**
	 * Valid key/value pair: inputDirNames=INPUTDIRECTORY1;...;INPUTDIRECTORYN |
	 * numWorkers=XX
	 */
	@Override
	public boolean init(final String initString) {
		try {
			final PropertyMap propertyMap = new PropertyMap(initString, "|", "="); // throws IllegalArgumentException
			final String numWorkersString = propertyMap.getProperty(FSReaderRealtime.PROP_NAME_NUM_WORKERS);
			int numWorkers = -1;
			if (numWorkersString == null) {
				throw new IllegalArgumentException("Missing init parameter '" + FSReaderRealtime.PROP_NAME_NUM_WORKERS + "'");
			}
			try {
				numWorkers = Integer.parseInt(numWorkersString);
			} catch (final NumberFormatException ex) { // NOPMD (value of numWorkers remains -1)
			}

			this.initInstanceFromArgs(this.inputDirNameListToArray(propertyMap.getProperty(FSReaderRealtime.PROP_NAME_INPUTDIRNAMES)), numWorkers);
		} catch (final IllegalArgumentException exc) {
			FSReaderRealtime.LOG.error("Failed to initString '" + initString + "': " + exc.getMessage());
			return false;
		}
		return true;
	}

	private String[] inputDirNameListToArray(final String inputDirNameList) throws IllegalArgumentException {
		String[] dirNameArray;

		// parse inputDir property value
		if ((inputDirNameList == null) || (inputDirNameList.trim().length() == 0)) { // NOPMD (inefficient empty check)
			final String errorMsg = "Invalid argument value for inputDirNameList:" + inputDirNameList;
			FSReaderRealtime.LOG.error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}
		try {
			final StringTokenizer dirNameTokenizer = new StringTokenizer(inputDirNameList, ";");
			dirNameArray = new String[dirNameTokenizer.countTokens()];
			for (int i = 0; dirNameTokenizer.hasMoreTokens(); i++) {
				dirNameArray[i] = dirNameTokenizer.nextToken().trim();
			}
		} catch (final Exception exc) { // NOCS
			throw new IllegalArgumentException("Error parsing list of input directories'" + inputDirNameList + "'", exc);
		}
		return dirNameArray;
	}

	private void initInstanceFromArgs(final String[] inputDirNames, final int numWorkers) throws IllegalArgumentException {
		if ((inputDirNames == null) || (inputDirNames.length <= 0)) {
			throw new IllegalArgumentException("Invalid property value for " + FSReaderRealtime.PROP_NAME_INPUTDIRNAMES + ":" + Arrays.toString(inputDirNames)); // NOCS
																																									// (MultipleStringLiteralsCheck)
		}

		if (numWorkers <= 0) {
			throw new IllegalArgumentException("Invalid property value for " + FSReaderRealtime.PROP_NAME_NUM_WORKERS + ": " + numWorkers); // NOCS
																																			// (MultipleStringLiteralsCheck)
		}

		final AbstractMonitoringReader fsReader = new FSReader(inputDirNames);
		final IMonitoringRecordConsumerPlugin rtCons = new FSReaderRealtimeCons(this);
		this.rtDistributor = new RealtimeReplayDistributor(numWorkers, rtCons, this.terminationLatch);
		this.analysis.setReader(fsReader);
		this.analysis.registerPlugin(this.rtDistributor);
	}

	/**
	 * Replays the monitoring log in real-time and returns after the complete
	 * log was being replayed.
	 */
	@Override
	public boolean read() {
		final boolean success = true;
		try {
			this.analysis.run();
			this.terminationLatch.await();
		} catch (final Exception ex) { // NOCS (catch all)
			FSReaderRealtime.LOG.error("An error occured while reading", ex);
			return false;
		}
		return success;
	}

	@Override
	public void terminate() {
		this.analysis.terminate();
	}

	/**
	 * Acts as a consumer to the rtDistributor and delegates incoming records to
	 * the FSReaderRealtime instance.
	 */
	private static class FSReaderRealtimeCons implements IMonitoringRecordConsumerPlugin {

		private final FSReaderRealtime master;

		public FSReaderRealtimeCons(final FSReaderRealtime master) {
			this.master = master;
		}

		@Override
		public Collection<Class<? extends IMonitoringRecord>> getRecordTypeSubscriptionList() {
			return null;
		}

		@Override
		public boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
			if (!this.master.deliverRecord(monitoringRecord)) {
				FSReaderRealtime.LOG.error("LogReaderExecutionException");
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
			// nothing to do
		}
	}
}
