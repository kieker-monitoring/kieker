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

import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.filter.executionFilter.TraceIdFilter;
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
public class TestTraceIdFilter extends AbstractKiekerTest { // NOCS

	// private static final Log log = LogFactory.getLog(TestTraceIdFilter.class);

	private static final String SESSION_ID = "LKHnuibfV"; // Session ID not relevant here

	private final SystemModelRepository systemEntityFactory = new SystemModelRepository(new Configuration());
	private final ExecutionFactory eFactory = new ExecutionFactory(this.systemEntityFactory);

	/**
	 * Creates a {@link TraceIdFilter} with the given properties
	 * using the constructor {@link TraceIdFilter#TraceIdFilter(Configuration, java.util.Map)}.
	 * 
	 * @param ignoreExecutionsBeforeTimestamp
	 * @param ignoreExecutionsAfterTimestamp
	 * @return
	 */
	private static TraceIdFilter createTraceIdFilter(final Set<Long> selectedTraces) {
		final Configuration cfg = new Configuration();

		if (selectedTraces != null) {
			final String[] selectedTracesArr = new String[selectedTraces.size()];
			final Iterator<Long> iter = selectedTraces.iterator();
			int i = 0;
			while (iter.hasNext()) {
				selectedTracesArr[i++] = iter.next().toString();
			}
			cfg.setProperty(TraceIdFilter.CONFIG_PROPERTY_NAME_SELECT_ALL_TRACES, Boolean.toString(false));
			cfg.setProperty(TraceIdFilter.CONFIG_PROPERTY_NAME_SELECTED_TRACES, Configuration.toProperty(selectedTracesArr));
		} else {
			cfg.setProperty(TraceIdFilter.CONFIG_PROPERTY_NAME_SELECT_ALL_TRACES, Boolean.toString(true));
			cfg.setProperty(TraceIdFilter.CONFIG_PROPERTY_NAME_SELECTED_TRACES, "");
		}

		return new TraceIdFilter(cfg);
	}

	/**
	 * Given a {@link TraceIdFilter} that passes traceIds included in a set <i>idsToPass</i>,
	 * assert that an Execution object <i>exec</i> with traceId not element of
	 * <i>idsToPass</i> is not passed through the filter.
	 * 
	 * @throws AnalysisConfigurationException
	 * @throws IllegalStateException
	 */
	@Test
	public void testAssertIgnoreTraceId() throws IllegalStateException, AnalysisConfigurationException {
		final SortedSet<Long> idsToPass = new TreeSet<Long>();
		idsToPass.add(5L);
		idsToPass.add(7L);

		final AnalysisController controller = new AnalysisController();
		final SimpleListReader<Execution> reader = new SimpleListReader<Execution>(new Configuration());
		final TraceIdFilter filter = TestTraceIdFilter.createTraceIdFilter(idsToPass);
		final SimpleSinkFilter<Execution> sinkPlugin = new SimpleSinkFilter<Execution>(new Configuration());
		final Execution exec = this.eFactory.genExecution(11L, // traceId (must not be element of idsToPass)
				TestTraceIdFilter.SESSION_ID,
				5, // tin (value not important)
				10, // tout (value not important)
				0, 0); // eoi, ess (values not important)
		Assert.assertTrue("Testcase invalid", !idsToPass.contains(exec.getTraceId()));

		Assert.assertTrue(sinkPlugin.getList().isEmpty());

		controller.registerReader(reader);
		controller.registerFilter(filter);
		controller.registerFilter(sinkPlugin);

		controller.connect(reader, SimpleListReader.OUTPUT_PORT_NAME, filter, TraceIdFilter.INPUT_PORT_NAME_EXECUTION);
		controller.connect(filter, TraceIdFilter.OUTPUT_PORT_NAME_MATCH, sinkPlugin, SimpleSinkFilter.INPUT_PORT_NAME);
		reader.addObject(exec);

		controller.run();

		Assert.assertTrue("Filter passed execution " + exec + " although traceId not element of " + idsToPass, sinkPlugin.getList()
				.isEmpty());
	}

	/**
	 * Given a {@link TraceIdFilter} that passes traceIds included in a set <i>idsToPass</i>,
	 * assert that an Execution object <i>exec</i> with traceId element of
	 * <i>idsToPass</i> is passed through the filter.
	 * 
	 * @throws AnalysisConfigurationException
	 * @throws IllegalStateException
	 */
	@Test
	public void testAssertPassTraceId() throws IllegalStateException, AnalysisConfigurationException {
		final SortedSet<Long> idsToPass = new TreeSet<Long>();
		idsToPass.add(5L);
		idsToPass.add(7L);
		final AnalysisController controller = new AnalysisController();
		final SimpleListReader<Execution> reader = new SimpleListReader<Execution>(new Configuration());
		final TraceIdFilter filter = TestTraceIdFilter.createTraceIdFilter(idsToPass);
		final SimpleSinkFilter<Execution> sinkPlugin = new SimpleSinkFilter<Execution>(new Configuration());
		final Execution exec = this.eFactory.genExecution(7L, // traceId (must be element of idsToPass)
				TestTraceIdFilter.SESSION_ID,
				5, // tin (value not important)
				10, // tout (value not important)
				0, 0); // eoi, ess (values not important)
		Assert.assertTrue("Testcase invalid", idsToPass.contains(exec.getTraceId()));

		Assert.assertTrue(sinkPlugin.getList().isEmpty());

		controller.registerReader(reader);
		controller.registerFilter(filter);
		controller.registerFilter(sinkPlugin);

		controller.connect(reader, SimpleListReader.OUTPUT_PORT_NAME, filter, TraceIdFilter.INPUT_PORT_NAME_EXECUTION);
		controller.connect(filter, TraceIdFilter.OUTPUT_PORT_NAME_MATCH, sinkPlugin, SimpleSinkFilter.INPUT_PORT_NAME);
		reader.addObject(exec);

		controller.run();

		Assert.assertFalse("Filter didn't pass execution " + exec + " although traceId element of " + idsToPass, sinkPlugin.getList()
				.isEmpty());

		Assert.assertTrue(sinkPlugin.getList().size() == 1);
		Assert.assertSame(sinkPlugin.getList().get(0), exec);
	}

	/**
	 * Given a {@link TraceIdFilter} that passes all traceIds, assert that an Execution
	 * object <i>exec</i> is passed through the filter.
	 * 
	 * @throws AnalysisConfigurationException
	 * @throws IllegalStateException
	 */
	@Test
	public void testAssertPassTraceIdWhenPassAll() throws IllegalStateException, AnalysisConfigurationException {
		final AnalysisController controller = new AnalysisController();
		final SimpleListReader<Execution> reader = new SimpleListReader<Execution>(new Configuration());
		final TraceIdFilter filter = TestTraceIdFilter.createTraceIdFilter(null); // i.e., pass all
		final SimpleSinkFilter<Execution> sinkPlugin = new SimpleSinkFilter<Execution>(new Configuration());
		final Execution exec = this.eFactory.genExecution(7L, // traceId (must be element of idsToPass)
				TestTraceIdFilter.SESSION_ID,
				5, // tin (value not important)
				10, // tout (value not important)
				0, 0); // eoi, ess (values not important)

		Assert.assertTrue(sinkPlugin.getList().isEmpty());

		controller.registerReader(reader);
		controller.registerFilter(filter);
		controller.registerFilter(sinkPlugin);

		controller.connect(reader, SimpleListReader.OUTPUT_PORT_NAME, filter, TraceIdFilter.INPUT_PORT_NAME_EXECUTION);
		controller.connect(filter, TraceIdFilter.OUTPUT_PORT_NAME_MATCH, sinkPlugin, SimpleSinkFilter.INPUT_PORT_NAME);
		reader.addObject(exec);

		controller.run();
		Assert.assertFalse("Filter didn't pass execution " + exec + " although all should pass.", sinkPlugin.getList().isEmpty());

		Assert.assertTrue(sinkPlugin.getList().size() == 1);
		Assert.assertSame(sinkPlugin.getList().get(0), exec);
	}
}
