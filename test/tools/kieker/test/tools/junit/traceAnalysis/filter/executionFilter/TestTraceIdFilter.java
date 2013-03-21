/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.tools.util.ExecutionFactory;

/**
 * A test for the {@link TraceIdFilter}.
 * 
 * @author Andre van Hoorn, Nils christian Ehmke
 * 
 * @deprecated to be removed in Kieker 1.8
 */
@Deprecated
@SuppressWarnings("deprecation")
public class TestTraceIdFilter extends AbstractKiekerTest { // NOCS

	// private static final Log log = LogFactory.getLog(TestTraceIdFilter.class);

	private static final String SESSION_ID = "LKHnuibfV"; // Session ID not relevant here

	private final SystemModelRepository systemEntityFactory = new SystemModelRepository(new Configuration(), null);
	private final ExecutionFactory eFactory = new ExecutionFactory(this.systemEntityFactory);

	public TestTraceIdFilter() {
		// empty default constructor
	}

	/**
	 * Creates a {@link TraceIdFilter} with the given properties
	 * using the constructor {@link TraceIdFilter#TraceIdFilter(Configuration, java.util.Map)}.
	 * 
	 * @param ignoreExecutionsBeforeTimestamp
	 * @param ignoreExecutionsAfterTimestamp
	 * @param analysisController
	 *            The analysis controller which will be used to register this component.
	 * @return
	 * 
	 * @deprecated To be removed in Kieker 1.8.
	 */
	@Deprecated
	@SuppressWarnings("deprecation")
	private static kieker.tools.traceAnalysis.filter.executionFilter.TraceIdFilter createTraceIdFilter(final Set<Long> selectedTraces,
			final IAnalysisController analysisController) {
		final Configuration cfg = new Configuration();

		if (selectedTraces != null) {
			final String[] selectedTracesArr = new String[selectedTraces.size()];
			final Iterator<Long> iter = selectedTraces.iterator();
			int i = 0;
			while (iter.hasNext()) {
				selectedTracesArr[i++] = iter.next().toString();
			}
			cfg.setProperty(kieker.tools.traceAnalysis.filter.executionFilter.TraceIdFilter.CONFIG_PROPERTY_NAME_SELECT_ALL_TRACES, Boolean.toString(false));
			cfg.setProperty(kieker.tools.traceAnalysis.filter.executionFilter.TraceIdFilter.CONFIG_PROPERTY_NAME_SELECTED_TRACES,
					Configuration.toProperty(selectedTracesArr));
		} else {
			cfg.setProperty(kieker.tools.traceAnalysis.filter.executionFilter.TraceIdFilter.CONFIG_PROPERTY_NAME_SELECT_ALL_TRACES, Boolean.toString(true));
			cfg.setProperty(kieker.tools.traceAnalysis.filter.executionFilter.TraceIdFilter.CONFIG_PROPERTY_NAME_SELECTED_TRACES, "");
		}

		return new kieker.tools.traceAnalysis.filter.executionFilter.TraceIdFilter(cfg, analysisController);
	}

	/**
	 * Given a {@link TraceIdFilter} that passes traceIds included in a set <i>idsToPass</i>,
	 * assert that an Execution object <i>exec</i> with traceId not element of
	 * <i>idsToPass</i> is not passed through the filter.
	 * 
	 * @throws AnalysisConfigurationException
	 *             If the internally assembled analysis configuration is somehow invalid.
	 * @throws IllegalStateException
	 *             If the internal analysis is in an invalid state.
	 */
	@Test
	public void testAssertIgnoreTraceId() throws IllegalStateException, AnalysisConfigurationException {
		final SortedSet<Long> idsToPass = new TreeSet<Long>();
		idsToPass.add(5L);
		idsToPass.add(7L);

		final IAnalysisController controller = new AnalysisController();
		final ListReader<Execution> reader = new ListReader<Execution>(new Configuration(), controller);
		final kieker.tools.traceAnalysis.filter.executionFilter.TraceIdFilter filter = TestTraceIdFilter.createTraceIdFilter(idsToPass, controller);
		final ListCollectionFilter<Execution> sinkPlugin = new ListCollectionFilter<Execution>(new Configuration(), controller);
		final Execution exec = this.eFactory.genExecution(11L, // traceId (must not be element of idsToPass)
				TestTraceIdFilter.SESSION_ID,
				5, // tin (value not important)
				10, // tout (value not important)
				0, 0); // eoi, ess (values not important)
		Assert.assertTrue("Testcase invalid", !idsToPass.contains(exec.getTraceId()));

		Assert.assertTrue(sinkPlugin.getList().isEmpty());

		controller.connect(reader, ListReader.OUTPUT_PORT_NAME, filter, kieker.tools.traceAnalysis.filter.executionFilter.TraceIdFilter.INPUT_PORT_NAME_EXECUTION);
		controller.connect(filter, kieker.tools.traceAnalysis.filter.executionFilter.TraceIdFilter.OUTPUT_PORT_NAME_MATCH, sinkPlugin,
				ListCollectionFilter.INPUT_PORT_NAME);
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
	 *             If the internally assembled analysis configuration is somehow invalid.
	 * @throws IllegalStateException
	 *             If the internal analysis is in an invalid state.
	 */
	@Test
	public void testAssertPassTraceId() throws IllegalStateException, AnalysisConfigurationException {
		final SortedSet<Long> idsToPass = new TreeSet<Long>();
		idsToPass.add(5L);
		idsToPass.add(7L);
		final IAnalysisController controller = new AnalysisController();
		final ListReader<Execution> reader = new ListReader<Execution>(new Configuration(), controller);
		final kieker.tools.traceAnalysis.filter.executionFilter.TraceIdFilter filter = TestTraceIdFilter.createTraceIdFilter(idsToPass, controller);
		final ListCollectionFilter<Execution> sinkPlugin = new ListCollectionFilter<Execution>(new Configuration(), controller);
		final Execution exec = this.eFactory.genExecution(7L, // traceId (must be element of idsToPass)
				TestTraceIdFilter.SESSION_ID,
				5, // tin (value not important)
				10, // tout (value not important)
				0, 0); // eoi, ess (values not important)
		Assert.assertTrue("Testcase invalid", idsToPass.contains(exec.getTraceId()));

		Assert.assertTrue(sinkPlugin.getList().isEmpty());

		controller.connect(reader, ListReader.OUTPUT_PORT_NAME, filter, kieker.tools.traceAnalysis.filter.executionFilter.TraceIdFilter.INPUT_PORT_NAME_EXECUTION);
		controller.connect(filter, kieker.tools.traceAnalysis.filter.executionFilter.TraceIdFilter.OUTPUT_PORT_NAME_MATCH, sinkPlugin,
				ListCollectionFilter.INPUT_PORT_NAME);
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
	 *             If the internally assembled analysis configuration is somehow invalid.
	 * @throws IllegalStateException
	 *             If the internally assembled analysis is in an invalid state.
	 */
	@Test
	public void testAssertPassTraceIdWhenPassAll() throws IllegalStateException, AnalysisConfigurationException {
		final IAnalysisController controller = new AnalysisController();

		final ListReader<Execution> reader = new ListReader<Execution>(new Configuration(), controller);
		final kieker.tools.traceAnalysis.filter.executionFilter.TraceIdFilter filter = TestTraceIdFilter.createTraceIdFilter(null, controller); // i.e., pass all
		final ListCollectionFilter<Execution> sinkPlugin = new ListCollectionFilter<Execution>(new Configuration(), controller);
		final Execution exec = this.eFactory.genExecution(7L, // traceId (must be element of idsToPass)
				TestTraceIdFilter.SESSION_ID,
				5, // tin (value not important)
				10, // tout (value not important)
				0, 0); // eoi, ess (values not important)

		Assert.assertTrue(sinkPlugin.getList().isEmpty());

		controller.connect(reader, ListReader.OUTPUT_PORT_NAME, filter, kieker.tools.traceAnalysis.filter.executionFilter.TraceIdFilter.INPUT_PORT_NAME_EXECUTION);
		controller.connect(filter, kieker.tools.traceAnalysis.filter.executionFilter.TraceIdFilter.OUTPUT_PORT_NAME_MATCH, sinkPlugin,
				ListCollectionFilter.INPUT_PORT_NAME);
		reader.addObject(exec);

		controller.run();
		Assert.assertFalse("Filter didn't pass execution " + exec + " although all should pass.", sinkPlugin.getList().isEmpty());

		Assert.assertTrue(sinkPlugin.getList().size() == 1);
		Assert.assertSame(sinkPlugin.getList().get(0), exec);
	}
}
