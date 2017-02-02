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

package kieker.test.tools.junit.writeRead;

import java.lang.Thread.State;
import java.lang.reflect.Constructor;
import java.util.List;

import kieker.Await;
import kieker.analysis.AnalysisController;
import kieker.analysis.IProjectContext;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class TestAnalysis {

	private final AnalysisController analysisController;
	private final ListCollectionFilter<IMonitoringRecord> sinkPlugin;
	private final Thread thread;

	public TestAnalysis(final Configuration configuration, final Class<? extends AbstractReaderPlugin> readerClass) throws Exception {
		this.analysisController = new AnalysisController();

		final AbstractReaderPlugin reader = this.newReader(readerClass, configuration, this.analysisController);
		final String outputPortName = reader.getAllOutputPortNames()[0];
		this.sinkPlugin = new ListCollectionFilter<IMonitoringRecord>(new Configuration(), this.analysisController);

		this.analysisController.connect(reader, outputPortName, this.sinkPlugin, ListCollectionFilter.INPUT_PORT_NAME);

		this.thread = new Thread() {
			@Override
			public void run() {
				try {
					TestAnalysis.this.getAnalysisController().run();
				} catch (IllegalStateException | AnalysisConfigurationException e) {
					throw new IllegalStateException("Should never happen", e);
				}
			}
		};
	}

	AnalysisController getAnalysisController() { // NOPMD
		return this.analysisController;
	}

	private AbstractReaderPlugin newReader(final Class<? extends AbstractReaderPlugin> readerClass, final Object... args) throws Exception {
		final Constructor<? extends AbstractReaderPlugin> constructor = readerClass.getConstructor(Configuration.class, IProjectContext.class);
		return constructor.newInstance(args);
	}

	/**
	 * Consider to also call {@link #waitUntilReaderIsWaitingForInput(int)} afterwards.
	 */
	public void startInNewThread() {
		this.thread.start();
	}

	/**
	 * Waits for the reader thread to block.
	 */
	public void waitUntilReaderIsWaitingForInput(final int timoutInMs) throws InterruptedException {
		Await.awaitThreadState(this.thread, State.WAITING, timoutInMs);
	}

	public void startAndWaitForTermination() throws IllegalStateException, AnalysisConfigurationException {
		this.analysisController.run();
	}

	public void waitForTermination(final long timeoutInMs) throws InterruptedException {
		this.thread.join(timeoutInMs);
	}

	public List<IMonitoringRecord> getList() {
		return this.sinkPlugin.getList();
	}

}
