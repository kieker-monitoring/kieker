/***************************************************************************
 * Copyright 2012 by
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
import java.util.concurrent.CountDownLatch;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.analysis.plugin.reader.filesystem.FSReader;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;

/**
 * @author Andre van Hoorn
 */
@Plugin(outputPorts = {
	@OutputPort(name = FSReaderRealtime.OUTPUT_PORT_NAME_MONITORING_RECORDS, eventTypes = { IMonitoringRecord.class }, description = "Output Port of the FSReaderRealtime")
})
public final class FSReaderRealtime extends AbstractReaderPlugin {
	public static final String OUTPUT_PORT_NAME_MONITORING_RECORDS = "monitoringRecords";

	public static final String CONFIG_PROPERTY_NAME_NUM_WORKERS = "numWorkers";
	public static final String CONFIG_PROPERTY_NAME_INPUTDIRNAMES = "inputDirs";

	private static final Log LOG = LogFactory.getLog(FSReaderRealtime.class);

	/* manages the life-cycle of the reader and consumers */
	private final AnalysisController analysis = new AnalysisController();
	/** Reader will wait for this latch before read() returns */
	private final CountDownLatch terminationLatch = new CountDownLatch(1);
	private int numWorkers;
	private String[] inputDirs;

	/**
	 * Creates a new instance of this class using the given parameters to
	 * configure the reader.
	 * 
	 * @param configuration
	 *            The configuration used to initialize the whole reader. Keep in
	 *            mind that the configuration should contain the following
	 *            properties:
	 *            <ul>
	 *            <li>The property {@code inputDirNames}, e.g. {@code INPUTDIRECTORY1;...;INPUTDIRECTORYN }
	 *            <li>The property {@code numWorkers}
	 *            </ul>
	 */
	public FSReaderRealtime(final Configuration configuration) {
		super(configuration);
		this.init(configuration);
	}

	private final boolean init(final Configuration configuration) {
		this.numWorkers = configuration.getIntProperty(CONFIG_PROPERTY_NAME_NUM_WORKERS);
		this.inputDirs = configuration.getStringArrayProperty(CONFIG_PROPERTY_NAME_INPUTDIRNAMES, ";");
		// this.inputDirs = this.inputDirNameListToArray(configuration.getStringProperty(PROP_NAME_INPUTDIRNAMES));
		return this.initInstanceFromArgs(this.inputDirs, this.numWorkers);
	}

	// removed and replaced by functions of configuration!
	// private String[] inputDirNameListToArray(final String inputDirNameList) throws IllegalArgumentException {
	// String[] dirNameArray;
	//
	// // parse inputDir property value
	// if ((inputDirNameList == null) || (inputDirNameList.trim().length() == 0)) { // NOPMD (inefficient empty check)
	// // LOG.error(errorMsg); // no log and throw
	// throw new IllegalArgumentException("Invalid argument value for inputDirNameList:" + inputDirNameList);
	// }
	// try {
	// final StringTokenizer dirNameTokenizer = new StringTokenizer(inputDirNameList, ";");
	// dirNameArray = new String[dirNameTokenizer.countTokens()];
	// for (int i = 0; dirNameTokenizer.hasMoreTokens(); i++) {
	// dirNameArray[i] = dirNameTokenizer.nextToken().trim();
	// }
	// } catch (final Exception exc) { // NOPMD NOCS
	// throw new IllegalArgumentException("Error parsing list of input directories'" + inputDirNameList + "'", exc);
	// }
	// return dirNameArray;
	// }

	private boolean initInstanceFromArgs(final String[] inputDirNames, final int nWorkers) throws IllegalArgumentException {
		if ((inputDirNames == null) || (inputDirNames.length <= 0)) {
			throw new IllegalArgumentException("Invalid property value for " + CONFIG_PROPERTY_NAME_INPUTDIRNAMES + ":"
					+ Arrays.toString(inputDirNames)); // NOCS
		}

		if (nWorkers <= 0) {
			throw new IllegalArgumentException("Invalid property value for " + CONFIG_PROPERTY_NAME_NUM_WORKERS + ": " + nWorkers); // NOCS
		}

		final Configuration configuration = new Configuration();
		configuration.setProperty(FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS, Configuration.toProperty(inputDirNames));
		final AbstractReaderPlugin fsReader = new FSReader(configuration);
		final FSReaderRealtimeCons rtCons = new FSReaderRealtimeCons(new Configuration());
		/* Register this instance as the master of the created plugin. */
		rtCons.setMaster(this);
		this.analysis.registerFilter(rtCons);
		final Configuration rtDistributorConfiguration = new Configuration();
		rtDistributorConfiguration.setProperty(RealtimeReplayDistributor.CONFIG_PROPERTY_NAME_NUM_WORKERS, Integer.toString(nWorkers));
		final RealtimeReplayDistributor rtDistributor = new RealtimeReplayDistributor(rtDistributorConfiguration);
		rtDistributor.setCons(rtCons);
		rtDistributor.setConstInputPortName(FSReaderRealtimeCons.INPUT_PORT_MONITORING_RECORDS);
		rtDistributor.setController(this.analysis);
		rtDistributor.setTerminationLatch(this.terminationLatch);

		this.analysis.registerReader(fsReader);
		this.analysis.registerFilter(rtDistributor);

		try {
			this.analysis.connect(fsReader, FSReader.OUTPUT_PORT_NAME_RECORDS, rtDistributor, RealtimeReplayDistributor.INPUT_PORT_NAME_MONITORING_RECORDS);
		} catch (final AnalysisConfigurationException ex) {
			LOG.error("Failed to connect fsReader to rtDistributor.", ex);
			return false;
		}
		return true;
	}

	/**
	 * Replays the monitoring log in real-time and returns after the complete
	 * log was being replayed.
	 */

	public boolean read() {
		final boolean success = true;
		try {
			this.analysis.run();
			this.terminationLatch.await();
		} catch (final Exception ex) { // NOPMD NOCS (catch all)
			LOG.error("An error occured while reading", ex);
			return false;
		}
		return success;
	}

	public void terminate(final boolean error) {
		this.analysis.terminate(error); // forward to analysis controller
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration defaultConfiguration = new Configuration();
		defaultConfiguration.setProperty(CONFIG_PROPERTY_NAME_NUM_WORKERS, "1");
		defaultConfiguration.setProperty(CONFIG_PROPERTY_NAME_INPUTDIRNAMES, "."); // the current folder as default
		return defaultConfiguration;
	}

	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_NUM_WORKERS, Integer.toString(this.numWorkers));
		configuration.setProperty(CONFIG_PROPERTY_NAME_INPUTDIRNAMES, Configuration.toProperty(this.inputDirs));
		return configuration;
	}

	/**
	 * Acts as a consumer to the rtDistributor and delegates incoming records to the FSReaderRealtime instance.
	 * 
	 * Do <b>not</b> use this as an outer class. It does not have the necessary constructors and
	 * method-implementations in order to be used as an outer class.
	 */
	@Plugin
	private static class FSReaderRealtimeCons extends AbstractFilterPlugin {

		/**
		 * This is the name of the default input port this plugin.
		 */
		public static final String INPUT_PORT_MONITORING_RECORDS = "monitoring-records";

		private FSReaderRealtime master;

		/**
		 * Creates a new instance of this class using the given configuration object.
		 * 
		 * @param configuration
		 *            The configuration object. Currently no information from this object are being used.
		 */
		public FSReaderRealtimeCons(final Configuration configuration) {
			super(configuration);
		}

		/**
		 * Sets the "master" of this object to a new value. The master is the instance of <code>FSReaderRealtime</code> which will receive the data. This method
		 * should be called directly after creation.
		 * 
		 * @param master
		 *            The new master of this plugin.
		 */
		public void setMaster(final FSReaderRealtime master) {
			this.master = master;
		}

		/**
		 * The suppress-warning-tag is only necessary because the method is being used via reflection...
		 * 
		 * @param data
		 */
		@SuppressWarnings("unused")
		@InputPort(name = FSReaderRealtimeCons.INPUT_PORT_MONITORING_RECORDS, eventTypes = { IMonitoringRecord.class })
		public void inputMonitoringRecords(final IMonitoringRecord record) {
			/* Make sure that the master exists. This is necessary due to the changed constructor. */
			if (this.master == null) {
				LOG.warn("Plugin doesn't have a valid master-object.");
				return;
			}
			if (!this.master.deliver(OUTPUT_PORT_NAME_MONITORING_RECORDS, record)) {
				LOG.error("LogReaderExecutionException");
			}
		}

		@Override
		protected Configuration getDefaultConfiguration() {
			return new Configuration();
		}

		public Configuration getCurrentConfiguration() {
			return new Configuration();
		}
	}
}
