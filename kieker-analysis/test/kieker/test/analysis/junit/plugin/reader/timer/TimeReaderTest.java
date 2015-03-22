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

package kieker.test.analysis.junit.plugin.reader.timer;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.AnalysisController.STATE;
import kieker.analysis.AnalysisControllerThread;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.CountingFilter;
import kieker.analysis.plugin.reader.timer.TimeReader;
import kieker.common.configuration.Configuration;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * A JUnit test for the {@link TimeReader}.<br>
 * </br>
 * 
 * Note: I know that some of the tests can result in race conditions (although this is not very likely). This is acceptable, as those are just tests (and not for
 * productive deployment). However, if the tests tend to fail, they have to be reworked.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.8
 */
public class TimeReaderTest extends AbstractKiekerTest {

	/**
	 * Default constructor.
	 */
	public TimeReaderTest() {
		// empty default constructor
	}

	/**
	 * Tests the "non blocking" mode of the reader.
	 * 
	 * @throws InterruptedException
	 *             If the thread is interrupted. This should not happen.
	 */
	@SuppressWarnings("unused")
	@Test
	public void testNonBlockingMode() throws InterruptedException {
		final AnalysisController ac = new AnalysisController();
		final AnalysisControllerThread thread = new AnalysisControllerThread(ac);

		final Configuration configuration = new Configuration();
		configuration.setProperty(TimeReader.CONFIG_PROPERTY_NAME_NUMBER_IMPULSES, "1");
		configuration.setProperty(TimeReader.CONFIG_PROPERTY_NAME_DELAY_NS, "0");
		configuration.setProperty(TimeReader.CONFIG_PROPERTY_NAME_UPDATE_INTERVAL_NS, "1000000000");

		new TimeReader(configuration, ac);

		// We expect the reader to return very fast - in this case we expect the AC to return within five seconds
		thread.start();
		Thread.sleep(6000);

		Assert.assertEquals(STATE.TERMINATED, ac.getState());
	}

	/**
	 * Tests the "blocking" mode of the reader.
	 * 
	 * @throws InterruptedException
	 *             If the thread is interrupted. This should not happen.
	 */
	@SuppressWarnings("unused")
	@Test
	public void testBlockingMode() throws InterruptedException {
		final AnalysisController ac = new AnalysisController();
		final AnalysisControllerThread thread = new AnalysisControllerThread(ac);

		final Configuration configuration = new Configuration();
		configuration.setProperty(TimeReader.CONFIG_PROPERTY_NAME_NUMBER_IMPULSES, Long.toString(TimeReader.INFINITE_EMITS));

		new TimeReader(configuration, ac);

		// We expect that the reader doesn't return immediately - in this case we expect the AC to run at least five seconds
		thread.start();
		Thread.sleep(5000);

		Assert.assertEquals(STATE.RUNNING, ac.getState());
		ac.terminate();
	}

	/**
	 * This test makes sure that the reader stores its configuration.
	 */
	@Test
	public void testConfigurationConservation() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(TimeReader.CONFIG_PROPERTY_NAME_NUMBER_IMPULSES, "50");
		configuration.setProperty(TimeReader.CONFIG_PROPERTY_NAME_DELAY_NS, "42");
		configuration.setProperty(TimeReader.CONFIG_PROPERTY_NAME_UPDATE_INTERVAL_NS, "21");
		final TimeReader tr = new TimeReader(configuration, new AnalysisController());

		Assert.assertEquals(42, tr.getCurrentConfiguration().getLongProperty(TimeReader.CONFIG_PROPERTY_NAME_DELAY_NS));
		Assert.assertEquals(21, tr.getCurrentConfiguration().getLongProperty(TimeReader.CONFIG_PROPERTY_NAME_UPDATE_INTERVAL_NS));
		Assert.assertEquals(50, tr.getCurrentConfiguration().getLongProperty(TimeReader.CONFIG_PROPERTY_NAME_NUMBER_IMPULSES));
	}

	/**
	 * This test should make sure that the timer delivers a correct amount of records within a given limit.
	 * 
	 * @throws InterruptedException
	 *             If the test thread is interrupted.
	 * @throws IllegalStateException
	 *             If the analysis is in the wrong state.
	 * @throws AnalysisConfigurationException
	 *             If the analysis is somehow invalid configured.
	 */
	@Test
	public void testIntervalTimer() throws InterruptedException, IllegalStateException, AnalysisConfigurationException {
		// Running 5 seconds, firing one event per 100 ms, we expect to receive approx. 50 events.
		final AnalysisController ac = new AnalysisController();
		final AnalysisControllerThread thread = new AnalysisControllerThread(ac);

		final Configuration configuration = new Configuration();
		configuration.setProperty(TimeReader.CONFIG_PROPERTY_NAME_UPDATE_INTERVAL_NS, "100000000");
		final TimeReader tr = new TimeReader(configuration, ac);
		final CountingFilter cf = new CountingFilter(new Configuration(), ac);

		ac.connect(tr, TimeReader.OUTPUT_PORT_NAME_TIMESTAMP_RECORDS, cf, CountingFilter.INPUT_PORT_NAME_EVENTS);

		thread.start();
		Thread.sleep(5000);
		ac.terminate();

		Assert.assertTrue(cf.getMessageCount() > 40);
		Assert.assertTrue(cf.getMessageCount() < 60);
	}

	/**
	 * This test should make sure that the timer delivers the correct amount of records.
	 * 
	 * @throws InterruptedException
	 *             If the test thread is interrupted.
	 * @throws IllegalStateException
	 *             If the analysis is in the wrong state.
	 * @throws AnalysisConfigurationException
	 *             If the analysis is somehow invalid configured.
	 */
	@Test
	public void testNumberOfEmittedSignals() throws InterruptedException, IllegalStateException, AnalysisConfigurationException {
		// Delivering 10 records with an interval time of 1 second, the analysis should run not more than 12 seconds.
		final AnalysisController ac = new AnalysisController();
		final AnalysisControllerThread thread = new AnalysisControllerThread(ac);

		final Configuration configuration = new Configuration();
		configuration.setProperty(TimeReader.CONFIG_PROPERTY_NAME_UPDATE_INTERVAL_NS, "100000000");
		configuration.setProperty(TimeReader.CONFIG_PROPERTY_NAME_NUMBER_IMPULSES, "10");
		final TimeReader tr = new TimeReader(configuration, ac);
		final CountingFilter cf = new CountingFilter(new Configuration(), ac);

		ac.connect(tr, TimeReader.OUTPUT_PORT_NAME_TIMESTAMP_RECORDS, cf, CountingFilter.INPUT_PORT_NAME_EVENTS);

		thread.start();
		Thread.sleep(12000);
		ac.terminate();

		Assert.assertEquals(10, cf.getMessageCount());
	}
}
