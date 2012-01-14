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
import java.util.StringTokenizer;
import java.util.concurrent.CountDownLatch;

import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.port.InputPort;
import kieker.analysis.plugin.port.OutputPort;
import kieker.analysis.plugin.port.Plugin;
import kieker.analysis.reader.AbstractReaderPlugin;
import kieker.analysis.reader.filesystem.FSReader;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.tools.traceAnalysis.systemModel.repository.AbstractRepository;

/**
 * @author Andre van Hoorn
 */
@Plugin(outputPorts = {
	@OutputPort(name = FSReaderRealtime.OUTPUT_PORT_NAME, eventTypes = { IMonitoringRecord.class }, description = "Output Port of the FSReaderRealtime")
})
public class FSReaderRealtime extends AbstractReaderPlugin {

	public static final String OUTPUT_PORT_NAME = "defaultOutput";
	private static final Log LOG = LogFactory.getLog(FSReaderRealtime.class);

	private static final String PROP_NAME_NUM_WORKERS = FSReaderRealtime.class + ".numWorkers";
	private static final String PROP_NAME_INPUTDIRNAMES = FSReaderRealtime.class + ".inputDirs";

	/* manages the life-cycle of the reader and consumers */
	private final AnalysisController analysis = new AnalysisController();
	private RealtimeReplayDistributor rtDistributor = null;
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
	public FSReaderRealtime(final Configuration configuration, final AbstractRepository[] repositories) {
		super(configuration, repositories);

		this.init(configuration);
	}

	// TODO: Remove this constructor
	public FSReaderRealtime(final String[] inputDirNames, final int numWorkers) {
		this(new Configuration(null), new AbstractRepository[0]);
		this.initInstanceFromArgs(inputDirNames, numWorkers);
	}

	public boolean init(final Configuration configuration) {
		try {
			final String numWorkersString = configuration.getProperty(FSReaderRealtime.PROP_NAME_NUM_WORKERS);
			this.numWorkers = -1;
			if (numWorkersString == null) {
				throw new IllegalArgumentException("Missing init parameter '" + FSReaderRealtime.PROP_NAME_NUM_WORKERS + "'");
			}
			try {
				this.numWorkers = Integer.parseInt(numWorkersString);
			} catch (final NumberFormatException ex) { // NOPMD (value of numWorkers remains -1)
			}
			this.inputDirs = this.inputDirNameListToArray(configuration.getProperty(FSReaderRealtime.PROP_NAME_INPUTDIRNAMES));
			this.initInstanceFromArgs(this.inputDirs, this.numWorkers);
		} catch (final IllegalArgumentException exc) {
			FSReaderRealtime.LOG.error("Failed to load configuration: " + exc.getMessage());
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
		} catch (final Exception exc) { // NOCS // NOPMD
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

		final Configuration configuration = new Configuration(null);
		configuration.setProperty(FSReader.CONFIG_INPUTDIRS,
				Configuration.toProperty(inputDirNames));
		final AbstractReaderPlugin fsReader = new FSReader(configuration, new AbstractRepository[0]);
		final AbstractAnalysisPlugin rtCons = new FSReaderRealtimeCons(this);
		this.rtDistributor = new RealtimeReplayDistributor(numWorkers, rtCons, this.terminationLatch, FSReaderRealtimeCons.INPUT_PORT);
		this.analysis.setReader(fsReader);
		AbstractPlugin.connect(fsReader, FSReader.OUTPUT_PORT_NAME, this.rtDistributor, FSReaderRealtimeCons.INPUT_PORT);
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
		} catch (final Exception ex) { // NOCS (catch all) // NOPMD
			FSReaderRealtime.LOG.error("An error occured while reading", ex);
			return false;
		}
		return success;
	}

	@Override
	public void terminate() {
		this.analysis.terminate();
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration defaultConfiguration = new Configuration();

		// TODO: Provide better default properties.
		defaultConfiguration.setProperty(FSReaderRealtime.PROP_NAME_NUM_WORKERS, "1");
		defaultConfiguration.setProperty(FSReaderRealtime.PROP_NAME_INPUTDIRNAMES, "");

		return defaultConfiguration;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration(null);

		configuration.setProperty(FSReaderRealtime.PROP_NAME_NUM_WORKERS, Integer.toString(this.numWorkers));
		configuration.setProperty(FSReaderRealtime.PROP_NAME_INPUTDIRNAMES, Configuration.toProperty(this.inputDirs));

		return configuration;
	}

	@Override
	protected AbstractRepository[] getDefaultRepositories() {
		return new AbstractRepository[0];
	}

	@Override
	public AbstractRepository[] getCurrentRepositories() {
		return new AbstractRepository[0];
	}

	/**
	 * Acts as a consumer to the rtDistributor and delegates incoming records to
	 * the FSReaderRealtime instance.<br>
	 * 
	 * Do <b>not</b> use this as an outer class. It does not have the necessary
	 * constructors and method-implementations in order to be used as an outer
	 * class.
	 */
	@Plugin(
			outputPorts = { @OutputPort(name = FSReaderRealtimeCons.OUTPUT_PORT_NAME, description = "Output port", eventTypes = { IMonitoringRecord.class })
			})
	private static class FSReaderRealtimeCons extends AbstractAnalysisPlugin {

		public static final String OUTPUT_PORT_NAME = "defaultOutput";
		public static final String INPUT_PORT = "newMonitoringRecord";
		private final FSReaderRealtime master;

		public FSReaderRealtimeCons(final FSReaderRealtime master) {
			super(new Configuration(null), new AbstractRepository[0]);
			this.master = master;
		}

		/**
		 * The supress-warning-tag is only necessary because the method is being used via reflection...
		 * 
		 * @param data
		 */
		@SuppressWarnings("unused")
		@InputPort(eventTypes = { IMonitoringRecord.class })
		public void newMonitoringRecord(final Object data) {
			final IMonitoringRecord record = (IMonitoringRecord) data;
			if (!this.master.deliver(FSReaderRealtime.OUTPUT_PORT_NAME, record)) {
				FSReaderRealtime.LOG.error("LogReaderExecutionException");
			}
			super.deliver(FSReaderRealtimeCons.OUTPUT_PORT_NAME, data);
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

		@Override
		protected Configuration getDefaultConfiguration() {
			return new Configuration();
		}

		@Override
		public Configuration getCurrentConfiguration() {
			return new Configuration();
		}

		@Override
		protected AbstractRepository[] getDefaultRepositories() {
			return new AbstractRepository[0];
		}

		@Override
		public AbstractRepository[] getCurrentRepositories() {
			return new AbstractRepository[0];
		}
	}
}
