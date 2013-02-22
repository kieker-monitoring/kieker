/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.junit.traceAnalysis.filter.executionFilter;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.filter.executionFilter.TimestampFilter;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.tools.util.ExecutionFactory;

/**
 * A test for the {@link TimestampFilter}.
 * 
 * @author Andre van Hoorn, Nils Christian Ehmke
 */
@SuppressWarnings("deprecation")
public class TestTimestampFilter extends AbstractKiekerTest { // NOCS

	// private static final Log LOG = LogFactory.getLog(TestTimestampFilter.class);

	private static final long IGNORE_EXECUTIONS_BEFORE_TIMESTAMP = 50;
	private static final long IGNORE_EXECUTIONS_AFTER_TIMESTAMP = 100;

	private static final String SESSION_ID = "j8tVhvDPYL"; // Session ID not relevant here

	private final SystemModelRepository systemEntityFactory = new SystemModelRepository(new Configuration(), null);
	private final ExecutionFactory eFactory = new ExecutionFactory(this.systemEntityFactory);

	public TestTimestampFilter() {
		// empty default constructor
	}

	/**
	 * Creates a {@link TimestampFilter} with the given properties using the constructor
	 * {@link TimestampFilter#TimestampFilter(kieker.common.configuration.Configuration, java.util.Map)}.
	 * 
	 * @param ignoreExecutionsBeforeTimestamp
	 *            The lower limit for the timestamps.
	 * @param ignoreExecutionsAfterTimestamp
	 *            The upper limit for the timestamps.
	 * @param analysisController
	 *            The analysis controller which will be used to register this component.
	 * @return A suitable instance of {@link TimestampFilter}.
	 */
	private static TimestampFilter createTimestampFilter(final long ignoreExecutionsBeforeTimestamp, final long ignoreExecutionsAfterTimestamp,
			final IAnalysisController analysisController) {
		final Configuration cfg = new Configuration();
		cfg.setProperty(TimestampFilter.CONFIG_PROPERTY_NAME_IGNORE_BEFORE_TIMESTAMP, Long.toString(ignoreExecutionsBeforeTimestamp));
		cfg.setProperty(TimestampFilter.CONFIG_PROPERTY_NAME_IGNORE_AFTER_TIMESTAMP, Long.toString(ignoreExecutionsAfterTimestamp));
		return new TimestampFilter(cfg, analysisController);
	}

	/**
	 * Given a TimestampFilter selecting records within an interval <i>[a,b]</i>,
	 * assert that a record <i>r</i> with <i>r.tin &lt; a</i> and <i>r.tout
	 * &gt; a </i>, <i>r.tout &lt; b</i> does not pass the filter.
	 * 
	 * @throws AnalysisConfigurationException
	 *             If the internally assembled analysis configuration is somehow invalid.
	 * @throws IllegalStateException
	 *             If the internal analysis is in an invalid state.
	 */
	@Test
	public void testRecordTinBeforeToutWithinIgnored() throws IllegalStateException, AnalysisConfigurationException {
		final AnalysisController controller = new AnalysisController();

		final ListReader<Execution> reader = new ListReader<Execution>(new Configuration(), controller);
		final TimestampFilter filter = TestTimestampFilter.createTimestampFilter(TestTimestampFilter.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP,
				TestTimestampFilter.IGNORE_EXECUTIONS_AFTER_TIMESTAMP, controller);
		final ListCollectionFilter<Execution> sinkPlugin = new ListCollectionFilter<Execution>(new Configuration(), controller);
		final Execution exec = this.eFactory.genExecution(77, // traceId (value not important)
				TestTimestampFilter.SESSION_ID,
				TestTimestampFilter.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP - 1, // tin
				TestTimestampFilter.IGNORE_EXECUTIONS_AFTER_TIMESTAMP - 1, // tout
				0, 0); // eoi, ess
		reader.addObject(exec);

		Assert.assertTrue(sinkPlugin.getList().isEmpty());

		controller.connect(reader, ListReader.OUTPUT_PORT_NAME, filter, TimestampFilter.INPUT_PORT_NAME_EXECUTION);
		controller.connect(filter, TimestampFilter.OUTPUT_PORT_NAME_WITHIN_PERIOD, sinkPlugin, ListCollectionFilter.INPUT_PORT_NAME);

		controller.run();

		Assert.assertTrue("Filter passed execution " + exec + " although tin timestamp before" + TestTimestampFilter.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP
				, sinkPlugin.getList().isEmpty());

	}

	/**
	 * Given a TimestampFilter selecting records within an interval <i>[a,b]</i>,
	 * assert that a record <i>r</i> with <i>r.tin &gt; a</i>, <i>r.tin
	 * &lt; b</i> and <i>r.tout &gt; b </i> does not pass the filter.
	 * 
	 * @throws AnalysisConfigurationException
	 *             If the internally assembled analysis configuration is somehow invalid.
	 * @throws IllegalStateException
	 *             If the internal analysis is in an invalid state.
	 */
	@Test
	public void testRecordTinWithinToutAfterIgnored() throws IllegalStateException, AnalysisConfigurationException {
		final AnalysisController controller = new AnalysisController();

		final ListReader<Execution> reader = new ListReader<Execution>(new Configuration(), controller);
		final TimestampFilter filter = TestTimestampFilter.createTimestampFilter(TestTimestampFilter.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP,
				TestTimestampFilter.IGNORE_EXECUTIONS_AFTER_TIMESTAMP, controller);
		final ListCollectionFilter<Execution> sinkPlugin = new ListCollectionFilter<Execution>(new Configuration(), controller);

		final Execution exec = this.eFactory.genExecution(15, // traceId (value not important)
				TestTimestampFilter.SESSION_ID,
				TestTimestampFilter.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP + 1, // tin
				TestTimestampFilter.IGNORE_EXECUTIONS_AFTER_TIMESTAMP + 1, // tout
				0, 0); // eoi, ess
		reader.addObject(exec);

		Assert.assertTrue(sinkPlugin.getList().isEmpty());

		controller.connect(reader, ListReader.OUTPUT_PORT_NAME, filter, TimestampFilter.INPUT_PORT_NAME_EXECUTION);
		controller.connect(filter, TimestampFilter.OUTPUT_PORT_NAME_WITHIN_PERIOD, sinkPlugin, ListCollectionFilter.INPUT_PORT_NAME);

		controller.run();

		Assert.assertTrue("Filter passed execution " + exec + " although tin timestamp before" + TestTimestampFilter.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP
				, sinkPlugin.getList().isEmpty());
	}

	/**
	 * Given a TimestampFilter selecting records within an interval <i>[a,b]</i>,
	 * assert that a record <i>r</i> with <i>r.tin == a</i> and <i>r.tout == b </i>
	 * does pass the filter.
	 * 
	 * @throws AnalysisConfigurationException
	 *             If the internally assembled analysis configuration is somehow invalid.
	 * @throws IllegalStateException
	 *             If the internal analysis is in an invalid state.
	 */
	@Test
	public void testRecordTinToutOnBordersPassed() throws IllegalStateException, AnalysisConfigurationException {
		final AnalysisController controller = new AnalysisController();

		final ListReader<Execution> reader = new ListReader<Execution>(new Configuration(), controller);
		final TimestampFilter filter = TestTimestampFilter.createTimestampFilter(TestTimestampFilter.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP,
				TestTimestampFilter.IGNORE_EXECUTIONS_AFTER_TIMESTAMP, controller);
		final ListCollectionFilter<Execution> sinkPlugin = new ListCollectionFilter<Execution>(new Configuration(), controller);

		final Execution exec = this.eFactory.genExecution(159, // traceId (value not important)
				TestTimestampFilter.SESSION_ID,
				TestTimestampFilter.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP, // tin
				TestTimestampFilter.IGNORE_EXECUTIONS_AFTER_TIMESTAMP, // tout
				0, 0); // eoi, ess
		reader.addObject(exec);

		Assert.assertTrue(sinkPlugin.getList().isEmpty());

		controller.connect(reader, ListReader.OUTPUT_PORT_NAME, filter, TimestampFilter.INPUT_PORT_NAME_EXECUTION);
		controller.connect(filter, TimestampFilter.OUTPUT_PORT_NAME_WITHIN_PERIOD, sinkPlugin, ListCollectionFilter.INPUT_PORT_NAME);

		controller.run();

		Assert.assertFalse("Filter didn't pass execution " + exec + " although timestamps within range [" + TestTimestampFilter.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP
				+ "," + TestTimestampFilter.IGNORE_EXECUTIONS_AFTER_TIMESTAMP + "]", sinkPlugin.getList().isEmpty());

		Assert.assertTrue(sinkPlugin.getList().size() == 1);
		Assert.assertSame(sinkPlugin.getList().get(0), exec);
	}

	/**
	 * Given a TimestampFilter selecting records within an interval <i>[a,b]</i>,
	 * assert that a record <i>r</i> with <i>r.tin &gt; a</i>, <i>r.tin &lt; b</i>
	 * and <i>r.tout &lt; b </i>, <i>r.tout &gt; a </i> does pass the filter.
	 * 
	 * @throws AnalysisConfigurationException
	 *             If the internally assembled analysis configuration is somehow invalid.
	 * @throws IllegalStateException
	 *             If the internally assembled analysis is in an invalid state.
	 */
	@Test
	public void testRecordTinToutWithinRangePassed() throws IllegalStateException, AnalysisConfigurationException {
		final AnalysisController controller = new AnalysisController();

		final ListReader<Execution> reader = new ListReader<Execution>(new Configuration(), controller);
		final TimestampFilter filter = TestTimestampFilter.createTimestampFilter(TestTimestampFilter.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP,
				TestTimestampFilter.IGNORE_EXECUTIONS_AFTER_TIMESTAMP, controller);
		final ListCollectionFilter<Execution> sinkPlugin = new ListCollectionFilter<Execution>(new Configuration(), controller);

		final Execution exec = this.eFactory.genExecution(159, // traceId (value not important)
				TestTimestampFilter.SESSION_ID,
				TestTimestampFilter.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP + 1, // tin
				TestTimestampFilter.IGNORE_EXECUTIONS_AFTER_TIMESTAMP - 1, // tout
				0, 0); // eoi, ess
		reader.addObject(exec);

		Assert.assertTrue(sinkPlugin.getList().isEmpty());

		controller.connect(reader, ListReader.OUTPUT_PORT_NAME, filter, TimestampFilter.INPUT_PORT_NAME_EXECUTION);
		controller.connect(filter, TimestampFilter.OUTPUT_PORT_NAME_WITHIN_PERIOD, sinkPlugin, ListCollectionFilter.INPUT_PORT_NAME);

		controller.run();

		Assert.assertFalse("Filter didn't pass execution " + exec + " although timestamps within range [" + TestTimestampFilter.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP
				+ "," + TestTimestampFilter.IGNORE_EXECUTIONS_AFTER_TIMESTAMP + "]", sinkPlugin.getList().isEmpty());

		Assert.assertTrue(sinkPlugin.getList().size() == 1);
		Assert.assertSame(sinkPlugin.getList().get(0), exec);
	}
}
