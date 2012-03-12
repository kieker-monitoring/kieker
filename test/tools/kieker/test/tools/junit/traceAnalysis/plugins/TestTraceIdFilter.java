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

package kieker.test.tools.junit.traceAnalysis.plugins;

import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.analysis.AnalysisController;
import kieker.common.configuration.Configuration;
import kieker.test.analysis.junit.plugin.SimpleSinkPlugin;
import kieker.test.tools.junit.traceAnalysis.util.ExecutionFactory;
import kieker.tools.traceAnalysis.plugins.executionFilter.TraceIdFilter;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

import org.junit.Test;

/**
 * 
 * @author Andre van Hoorn
 */
public class TestTraceIdFilter extends TestCase { // NOCS

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
			final String selectedTracesArr[] = new String[selectedTraces.size()];
			final Iterator<Long> iter = selectedTraces.iterator();
			int i = 0;
			while (iter.hasNext()) {
				selectedTracesArr[i++] = iter.next().toString();
			}
			cfg.setProperty(TraceIdFilter.CONFIG_SELECT_ALL_TRACES, Boolean.toString(false));
			cfg.setProperty(TraceIdFilter.CONFIG_SELECTED_TRACES, Configuration.toProperty(selectedTracesArr));
		} else {
			cfg.setProperty(TraceIdFilter.CONFIG_SELECT_ALL_TRACES, Boolean.toString(true));
			cfg.setProperty(TraceIdFilter.CONFIG_SELECTED_TRACES, "");
		}

		return new TraceIdFilter(cfg);
	}

	/**
	 * Given a {@link TraceIdFilter} that passes traceIds included in a set <i>idsToPass</i>,
	 * assert that an Execution object <i>exec</i> with traceId not element of
	 * <i>idsToPass</i> is not passed through the filter.
	 */
	@Test
	public void testAssertIgnoreTraceId() {
		final NavigableSet<Long> idsToPass = new TreeSet<Long>();
		idsToPass.add(5l); // NOCS (MagicNumberCheck)
		idsToPass.add(7l); // NOCS (MagicNumberCheck)

		final TraceIdFilter filter = TestTraceIdFilter.createTraceIdFilter(idsToPass);
		final SimpleSinkPlugin sinkPlugin = new SimpleSinkPlugin(new Configuration());
		final AnalysisController controller = new AnalysisController();
		final Execution exec = this.eFactory.genExecution(11l, // traceId (must not be element of idsToPass) // NOCS (MagicNumberCheck)
				TestTraceIdFilter.SESSION_ID,
				5, // tin (value not important) // NOCS (MagicNumberCheck)
				10, // tout (value not important) // NOCS (MagicNumberCheck)
				0, 0); // eoi, ess (values not important) // NOCS (MagicNumberCheck)
		Assert.assertTrue("Testcase invalid", !idsToPass.contains(exec.getTraceId()));

		Assert.assertTrue(sinkPlugin.getList().isEmpty());

		controller.registerFilter(filter);
		controller.registerFilter(sinkPlugin);

		controller.connect(filter, TraceIdFilter.OUTPUT_PORT_NAME, sinkPlugin, SimpleSinkPlugin.INPUT_PORT_NAME);
		filter.newExecution(exec);
		Assert.assertTrue("Filter passed execution " + exec + " although traceId not element of " + idsToPass, sinkPlugin.getList()
				.isEmpty());
	}

	/**
	 * Given a {@link TraceIdFilter} that passes traceIds included in a set <i>idsToPass</i>,
	 * assert that an Execution object <i>exec</i> with traceId element of
	 * <i>idsToPass</i> is passed through the filter.
	 */
	@Test
	public void testAssertPassTraceId() {
		final NavigableSet<Long> idsToPass = new TreeSet<Long>();
		idsToPass.add(5l); // NOCS (MagicNumberCheck)
		idsToPass.add(7l); // NOCS (MagicNumberCheck)

		final TraceIdFilter filter = TestTraceIdFilter.createTraceIdFilter(idsToPass);
		final SimpleSinkPlugin sinkPlugin = new SimpleSinkPlugin(new Configuration());
		final AnalysisController controller = new AnalysisController();
		final Execution exec = this.eFactory.genExecution(7l, // traceId (must be element of idsToPass) // NOCS (MagicNumberCheck)
				TestTraceIdFilter.SESSION_ID,
				5, // tin (value not important) // NOCS (MagicNumberCheck)
				10, // tout (value not important) // NOCS (MagicNumberCheck)
				0, 0); // eoi, ess (values not important) // NOCS (MagicNumberCheck)
		Assert.assertTrue("Testcase invalid", idsToPass.contains(exec.getTraceId()));

		Assert.assertTrue(sinkPlugin.getList().isEmpty());

		controller.registerFilter(filter);
		controller.registerFilter(sinkPlugin);

		controller.connect(filter, TraceIdFilter.OUTPUT_PORT_NAME, sinkPlugin, SimpleSinkPlugin.INPUT_PORT_NAME);
		filter.newExecution(exec);
		Assert.assertFalse("Filter didn't pass execution " + exec + " although traceId element of " + idsToPass, sinkPlugin.getList()
				.isEmpty());

		Assert.assertTrue(sinkPlugin.getList().size() == 1);
		Assert.assertSame(sinkPlugin.getList().get(0), exec);
	}

	/**
	 * Given a {@link TraceIdFilter} that passes all traceIds, assert that an Execution
	 * object <i>exec</i> is passed through the filter.
	 */
	@Test
	public void testAssertPassTraceIdWhenPassAll() {
		final NavigableSet<Long> idsToPass = null; // i.e., pass all

		final TraceIdFilter filter = TestTraceIdFilter.createTraceIdFilter(idsToPass);
		final SimpleSinkPlugin sinkPlugin = new SimpleSinkPlugin(new Configuration());
		final AnalysisController controller = new AnalysisController();
		final Execution exec = this.eFactory.genExecution(7l, // traceId (must be element of idsToPass) // NOCS (MagicNumberCheck)
				TestTraceIdFilter.SESSION_ID,
				5, // tin (value not important) // NOCS (MagicNumberCheck)
				10, // tout (value not important) // NOCS (MagicNumberCheck)
				0, 0); // eoi, ess (values not important) // NOCS (MagicNumberCheck)

		Assert.assertTrue(sinkPlugin.getList().isEmpty());

		controller.registerFilter(filter);
		controller.registerFilter(sinkPlugin);

		controller.connect(filter, TraceIdFilter.OUTPUT_PORT_NAME, sinkPlugin, SimpleSinkPlugin.INPUT_PORT_NAME);
		filter.newExecution(exec);
		Assert.assertFalse("Filter didn't pass execution " + exec + " although traceId element of " + idsToPass, sinkPlugin.getList()
				.isEmpty());

		Assert.assertTrue(sinkPlugin.getList().size() == 1);
		Assert.assertSame(sinkPlugin.getList().get(0), exec);
	}
}
