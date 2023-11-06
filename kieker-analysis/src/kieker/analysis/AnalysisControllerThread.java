/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Allows spawn the execution of an {@link AnalysisController} into a separate {@link Thread}. The thread with the {@link AnalysisController} instance
 * provided in the constructor {@link #AnalysisControllerThread(IAnalysisController)} is started by calling the {@link #start()} method. The analysis can be
 * terminated by calling the {@link #terminate()} method which delegates the call to the {@link kieker.analysis.AnalysisController#terminate()} method.
 * 
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 1.4
 */
public final class AnalysisControllerThread extends Thread {
	private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisControllerThread.class);

	private final IAnalysisController analysisController;
	private final CountDownLatch terminationLatch = new CountDownLatch(1);

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param analysisController
	 *            The analysis controller to be managed by this thread.
	 */
	public AnalysisControllerThread(final IAnalysisController analysisController) {
		super();
		this.analysisController = analysisController;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void start() { // NOPMD (here we net method level synchronization (inheritance))
		super.start();
		// If we have a default AnalysisController, we are able to wait for its initialization
		if (this.analysisController instanceof AnalysisController) {
			((AnalysisController) this.analysisController).awaitInitialization();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		try {
			this.analysisController.run();
			this.terminationLatch.countDown();
		} catch (final Exception ex) { // NOPMD NOCS (Exception)
			LOGGER.error("Error running AnalysisController.", ex);
		}
	}

	/**
	 * Awaits (with timeout) the termination of the contained {@link AnalysisController}.
	 * 
	 * @param timeout
	 *            The maximum time to wait
	 * @param unit
	 *            The time unit of the timeout.
	 * @return see {@link CountDownLatch#await(long, TimeUnit)}
	 * @throws InterruptedException
	 *             If the current thread has been interrupted while waiting.
	 */
	public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
		return this.terminationLatch.await(timeout, unit);
	}

	/**
	 * Awaits the termination of the contained {@link AnalysisController}.
	 * 
	 * @throws InterruptedException
	 *             If the current thread has been interrupted while waiting.
	 */
	public void awaitTermination() throws InterruptedException {
		this.terminationLatch.await();
	}

	/**
	 * Initiates a termination of the executed {@link AnalysisController}.
	 */
	public void terminate() {
		synchronized (this) {
			this.analysisController.terminate();
		}
	}
}
