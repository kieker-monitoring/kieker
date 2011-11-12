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
import java.util.StringTokenizer;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

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
	private final OutputPort outputPort;
	/**
	 * This field determines which classes are transported through the output port.
	 */
	private static final Collection<Class<?>> OUT_CLASSES = Collections
			.unmodifiableCollection(new CopyOnWriteArrayList<Class<?>>(new Class<?>[] { IMonitoringRecord.class }));

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

		/* Register the output port. */
		this.outputPort = new OutputPort("Output Port of the JMXReader", FSReaderRealtime.OUT_CLASSES);
		super.registerOutputPort("out", this.outputPort);

		this.init(configuration);
	}

	public FSReaderRealtime(final String[] inputDirNames, final int numWorkers) {
		this(new Configuration(null));
		this.initInstanceFromArgs(inputDirNames, numWorkers);
	}

	public boolean init(final Configuration configuration) {
		try {
			final String numWorkersString = configuration.getProperty(FSReaderRealtime.PROP_NAME_NUM_WORKERS);
			int numWorkers = -1;
			if (numWorkersString == null) {
				throw new IllegalArgumentException("Missing init parameter '" + FSReaderRealtime.PROP_NAME_NUM_WORKERS + "'");
			}
			try {
				numWorkers = Integer.parseInt(numWorkersString);
			} catch (final NumberFormatException ex) { // NOPMD (value of numWorkers remains -1)
			}

			this.initInstanceFromArgs(this.inputDirNameListToArray(configuration.getProperty(FSReaderRealtime.PROP_NAME_INPUTDIRNAMES)), numWorkers);
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
				AbstractConfiguration.toProperty(inputDirNames));
		final AbstractMonitoringReader fsReader = new FSReader(configuration);
		final AbstractAnalysisPlugin rtCons = new FSReaderRealtimeCons(this);
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

	/**
	 * Acts as a consumer to the rtDistributor and delegates incoming records to
	 * the FSReaderRealtime instance.
	 */
	private static class FSReaderRealtimeCons extends AbstractAnalysisPlugin {

		private final FSReaderRealtime master;
		private final OutputPort output = new OutputPort("out", Collections.unmodifiableCollection(new CopyOnWriteArrayList<Class<?>>(
				new Class<?>[] { IMonitoringRecord.class })));
		private final AbstractInputPort input = new AbstractInputPort("in", Collections.unmodifiableCollection(new CopyOnWriteArrayList<Class<?>>(
				new Class<?>[] { IMonitoringRecord.class }))) {
			@Override
			public void newEvent(final Object event) {
				FSReaderRealtimeCons.this.newMonitoringRecord((IMonitoringRecord) event);

				output.deliver(event);
			}
		};

		public FSReaderRealtimeCons(Configuration configuration) {
			super(configuration);
			master = null;

			// TODO: Load from configuration.

			registerInputPort("in", input);
			registerOutputPort("out", output);
		}

		public FSReaderRealtimeCons(final FSReaderRealtime master) {
			super(new Configuration(null));
			this.master = master;

			registerInputPort("in", input);
			registerOutputPort("out", output);
		}

		public boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
			if (!this.master.outputPort.deliver(monitoringRecord)) {
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

		@Override
		protected Properties getDefaultProperties() {
			return new Properties();
		}
	}

	@Override
	protected Properties getDefaultProperties() {
		return new Properties();
	}
}
