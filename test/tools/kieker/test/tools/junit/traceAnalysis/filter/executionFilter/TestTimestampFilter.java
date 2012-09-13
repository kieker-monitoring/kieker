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
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.filter.executionFilter.TimestampFilter;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

import kieker.test.analysis.util.plugin.filter.SimpleSinkFilter;
import kieker.test.analysis.util.plugin.reader.SimpleListReader;
import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.tools.util.ExecutionFactory;

/**
 * 
 * @author Andre van Hoorn
 */
public class TestTimestampFilter extends AbstractKiekerTest { // NOCS

	// private static final Log LOG = LogFactory.getLog(TestTimestampFilter.class);

	private static final long IGNORE_EXECUTIONS_BEFORE_TIMESTAMP = 50;
	private static final long IGNORE_EXECUTIONS_AFTER_TIMESTAMP = 100;

	private static final String SESSION_ID = "j8tVhvDPYL"; // Session ID not relevant here

	private final SystemModelRepository systemEntityFactory = new SystemModelRepository(new Configuration());
	private final ExecutionFactory eFactory = new ExecutionFactory(this.systemEntityFactory);

	/**
	 * Creates a {@link TimestampFilter} with the given properties
	 * using the constructor {@link TimestampFilter#TimestampFilter(kieker.common.configuration.Configuration, java.util.Map)}
	 * 
	 * @param ignoreExecutionsBeforeTimestamp
	 * @param ignoreExecutionsAfterTimestamp
	 * @return
	 */
	private static TimestampFilter createTimestampFilter(final long ignoreExecutionsBeforeTimestamp, final long ignoreExecutionsAfterTimestamp) {
		final Configuration cfg = new Configuration();
		cfg.setProperty(TimestampFilter.CONFIG_PROPERTY_NAME_IGNORE_BEFORE_TIMESTAMP, Long.toString(ignoreExecutionsBeforeTimestamp));
		cfg.setProperty(TimestampFilter.CONFIG_PROPERTY_NAME_IGNORE_AFTER_TIMESTAMP, Long.toString(ignoreExecutionsAfterTimestamp));
		return new TimestampFilter(cfg);
	}

	/**
	 * Given a TimestampFilter selecting records within an interval <i>[a,b]</i>,
	 * assert that a record <i>r</i> with <i>r.tin &lt; a</i> and <i>r.tout
	 * &gt; a </i>, <i>r.tout &lt; b</i> does not pass the filter.
	 * 
	 * @throws AnalysisConfigurationException
	 * @throws IllegalStateException
	 */
	@Test
	public void testRecordTinBeforeToutWithinIgnored() throws IllegalStateException, AnalysisConfigurationException {
		final SimpleListReader<Execution> reader = new SimpleListReader<Execution>(new Configuration());
		final TimestampFilter filter = TestTimestampFilter.createTimestampFilter(TestTimestampFilter.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP,
				TestTimestampFilter.IGNORE_EXECUTIONS_AFTER_TIMESTAMP);
		final AnalysisController controller = new AnalysisController();
		final SimpleSinkFilter<Execution> sinkPlugin = new SimpleSinkFilter<Execution>(new Configuration());
		final Execution exec = this.eFactory.genExecution(77, // traceId (value not important)
				TestTimestampFilter.SESSION_ID,
				TestTimestampFilter.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP - 1, // tin
				TestTimestampFilter.IGNORE_EXECUTIONS_AFTER_TIMESTAMP - 1, // tout
				0, 0); // eoi, ess
		reader.addObject(exec);

		Assert.assertTrue(sinkPlugin.getList().isEmpty());

		controller.registerReader(reader);
		controller.registerFilter(filter);
		controller.registerFilter(sinkPlugin);

		controller.connect(reader, SimpleListReader.OUTPUT_PORT_NAME, filter, TimestampFilter.INPUT_PORT_NAME_EXECUTION);
		controller.connect(filter, TimestampFilter.OUTPUT_PORT_NAME_WITHIN_PERIOD, sinkPlugin, SimpleSinkFilter.INPUT_PORT_NAME);

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
	 * @throws IllegalStateException
	 */
	@Test
	public void testRecordTinWithinToutAfterIgnored() throws IllegalStateException, AnalysisConfigurationException {
		final SimpleListReader<Execution> reader = new SimpleListReader<Execution>(new Configuration());
		final TimestampFilter filter = TestTimestampFilter.createTimestampFilter(TestTimestampFilter.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP,
				TestTimestampFilter.IGNORE_EXECUTIONS_AFTER_TIMESTAMP);
		final SimpleSinkFilter<Execution> sinkPlugin = new SimpleSinkFilter<Execution>(new Configuration());
		final AnalysisController controller = new AnalysisController();
		final Execution exec = this.eFactory.genExecution(15, // traceId (value not important)
				TestTimestampFilter.SESSION_ID,
				TestTimestampFilter.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP + 1, // tin
				TestTimestampFilter.IGNORE_EXECUTIONS_AFTER_TIMESTAMP + 1, // tout
				0, 0); // eoi, ess
		reader.addObject(exec);

		Assert.assertTrue(sinkPlugin.getList().isEmpty());

		controller.registerReader(reader);
		controller.registerFilter(filter);
		controller.registerFilter(sinkPlugin);

		controller.connect(reader, SimpleListReader.OUTPUT_PORT_NAME, filter, TimestampFilter.INPUT_PORT_NAME_EXECUTION);
		controller.connect(filter, TimestampFilter.OUTPUT_PORT_NAME_WITHIN_PERIOD, sinkPlugin, SimpleSinkFilter.INPUT_PORT_NAME);

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
	 * @throws IllegalStateException
	 */
	@Test
	public void testRecordTinToutOnBordersPassed() throws IllegalStateException, AnalysisConfigurationException {
		final SimpleListReader<Execution> reader = new SimpleListReader<Execution>(new Configuration());
		final TimestampFilter filter = TestTimestampFilter.createTimestampFilter(TestTimestampFilter.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP,
				TestTimestampFilter.IGNORE_EXECUTIONS_AFTER_TIMESTAMP);
		final SimpleSinkFilter<Execution> sinkPlugin = new SimpleSinkFilter<Execution>(new Configuration());
		final AnalysisController controller = new AnalysisController();
		final Execution exec = this.eFactory.genExecution(159, // traceId (value not important)
				TestTimestampFilter.SESSION_ID,
				TestTimestampFilter.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP, // tin
				TestTimestampFilter.IGNORE_EXECUTIONS_AFTER_TIMESTAMP, // tout
				0, 0); // eoi, ess
		reader.addObject(exec);

		Assert.assertTrue(sinkPlugin.getList().isEmpty());

		controller.registerReader(reader);
		controller.registerFilter(filter);
		controller.registerFilter(sinkPlugin);

		controller.connect(reader, SimpleListReader.OUTPUT_PORT_NAME, filter, TimestampFilter.INPUT_PORT_NAME_EXECUTION);
		controller.connect(filter, TimestampFilter.OUTPUT_PORT_NAME_WITHIN_PERIOD, sinkPlugin, SimpleSinkFilter.INPUT_PORT_NAME);

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
	 * @throws IllegalStateException
	 */
	@Test
	public void testRecordTinToutWithinRangePassed() throws IllegalStateException, AnalysisConfigurationException {
		final SimpleListReader<Execution> reader = new SimpleListReader<Execution>(new Configuration());
		final TimestampFilter filter = TestTimestampFilter.createTimestampFilter(TestTimestampFilter.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP,
				TestTimestampFilter.IGNORE_EXECUTIONS_AFTER_TIMESTAMP);
		final SimpleSinkFilter<Execution> sinkPlugin = new SimpleSinkFilter<Execution>(new Configuration());
		final AnalysisController controller = new AnalysisController();
		final Execution exec = this.eFactory.genExecution(159, // traceId (value not important)
				TestTimestampFilter.SESSION_ID,
				TestTimestampFilter.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP + 1, // tin
				TestTimestampFilter.IGNORE_EXECUTIONS_AFTER_TIMESTAMP - 1, // tout
				0, 0); // eoi, ess
		reader.addObject(exec);

		Assert.assertTrue(sinkPlugin.getList().isEmpty());

		controller.registerReader(reader);
		controller.registerFilter(filter);
		controller.registerFilter(sinkPlugin);

		controller.connect(reader, SimpleListReader.OUTPUT_PORT_NAME, filter, TimestampFilter.INPUT_PORT_NAME_EXECUTION);
		controller.connect(filter, TimestampFilter.OUTPUT_PORT_NAME_WITHIN_PERIOD, sinkPlugin, SimpleSinkFilter.INPUT_PORT_NAME);

		controller.run();

		Assert.assertFalse("Filter didn't pass execution " + exec + " although timestamps within range [" + TestTimestampFilter.IGNORE_EXECUTIONS_BEFORE_TIMESTAMP
				+ "," + TestTimestampFilter.IGNORE_EXECUTIONS_AFTER_TIMESTAMP + "]", sinkPlugin.getList().isEmpty());

		Assert.assertTrue(sinkPlugin.getList().size() == 1);
		Assert.assertSame(sinkPlugin.getList().get(0), exec);
	}
}
