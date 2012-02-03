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

package kieker.test.tools.junit.traceAnalysis.plugins.flow;

import java.util.HashMap;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;
import kieker.common.record.flow.BeforeOperationEvent;
import kieker.common.record.flow.TraceEvent;
import kieker.test.tools.junit.traceAnalysis.util.SimpleSinkPlugin;
import kieker.tools.traceAnalysis.plugins.flow.TimestampFilter;

import org.junit.Test;

/**
 * 
 * @author Andre van Hoorn
 */
public class TestTimestampFilter extends TestCase { // NOCS

	// private static final Log log = LogFactory.getLog(TestTimestampFilter.class);

	private static final TraceEvent event = new BeforeOperationEvent(34556l, 324440l, 89, "Class.operation"); // NOCS (MagicNumberCheck)

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
		cfg.put(TimestampFilter.CONFIG_IGNORE_EXECUTIONS_BEFORE_TIMESTAMP, Long.toString(ignoreExecutionsBeforeTimestamp));
		cfg.put(TimestampFilter.CONFIG_IGNORE_EXECUTIONS_AFTER_TIMESTAMP, Long.toString(ignoreExecutionsAfterTimestamp));
		return new TimestampFilter(cfg, new HashMap<String, AbstractRepository>());
	}

	/**
	 * Given a {@link TimestampFilter} selecting {@link TraceEvent}s within an interval <i>[a,b]</i>,
	 * assert that a {@link TraceEvent} <i>e</i> with <i>e.timestamp &lt; a</i> does not pass the filter.
	 */
	@Test
	public void testEventBeforeIgnored() {

		final long leftBorder = TestTimestampFilter.event.getTimestamp() + 1;
		final long rightBorder = leftBorder + 1;

		final TimestampFilter filter = TestTimestampFilter.createTimestampFilter(leftBorder, rightBorder);
		final SimpleSinkPlugin sinkPlugin = new SimpleSinkPlugin();

		Assert.assertTrue(sinkPlugin.getList().isEmpty());
		AbstractPlugin.connect(filter, TimestampFilter.OUTPUT_PORT_NAME, sinkPlugin, SimpleSinkPlugin.INPUT_PORT_NAME);
		filter.inputTraceEvent(TestTimestampFilter.event);
		Assert.assertTrue("Filter passed event " + TestTimestampFilter.event + " although timestamp before" + leftBorder
				, sinkPlugin.getList().isEmpty());

	}

	/**
	 * Given a {@link TimestampFilter} selecting {@link TraceEvent}s within an interval <i>[a,b]</i>,
	 * assert that a {@link TraceEvent} <i>e</i> with <i>e.timestamp &gt; b </i> does not
	 * pass the filter.
	 */
	@Test
	public void testEventAfterIgnored() {
		final long rightBorder = TestTimestampFilter.event.getTimestamp() - 1;
		final long leftBorder = rightBorder - 1;

		final TimestampFilter filter = TestTimestampFilter.createTimestampFilter(leftBorder, rightBorder);
		final SimpleSinkPlugin sinkPlugin = new SimpleSinkPlugin();

		Assert.assertTrue(sinkPlugin.getList().isEmpty());
		AbstractPlugin.connect(filter, TimestampFilter.OUTPUT_PORT_NAME, sinkPlugin, SimpleSinkPlugin.INPUT_PORT_NAME);
		filter.inputTraceEvent(TestTimestampFilter.event);
		Assert.assertTrue("Filter passed event " + TestTimestampFilter.event + " although timestamp before" + leftBorder
					, sinkPlugin.getList().isEmpty());
	}

	/**
	 * Given a {@link TimestampFilter} selecting {@link TraceEvent}s within an interval <i>[a,b]</i>,
	 * assert that an event <i>e</i> with <i>e.timestamp == a</i> does pass the filter.
	 */
	@Test
	public void testRecordOnLeftBorderPasses() {
		final long leftBorder = TestTimestampFilter.event.getTimestamp();
		final long rightBorder = leftBorder + 1;

		final TimestampFilter filter = TestTimestampFilter.createTimestampFilter(leftBorder, rightBorder);
		final SimpleSinkPlugin sinkPlugin = new SimpleSinkPlugin();

		Assert.assertTrue(sinkPlugin.getList().isEmpty());
		AbstractPlugin.connect(filter, TimestampFilter.OUTPUT_PORT_NAME, sinkPlugin, SimpleSinkPlugin.INPUT_PORT_NAME);
		filter.inputTraceEvent(TestTimestampFilter.event);

		Assert.assertFalse("Filter ignored event " + TestTimestampFilter.event + " although timestamp on left Border" + leftBorder
						, sinkPlugin.getList().isEmpty());

		Assert.assertTrue(sinkPlugin.getList().size() == 1);
		Assert.assertSame(sinkPlugin.getList().get(0), TestTimestampFilter.event);
	}

	/**
	 * Given a {@link TimestampFilter} selecting {@link TraceEvent}s within an interval <i>[a,b]</i>,
	 * assert that an event <i>e</i> with <i>e.timestamp == b</i> does pass the filter.
	 */
	@Test
	public void testRecordOnRightBorderPasses() {
		final long rightBorder = TestTimestampFilter.event.getTimestamp();
		final long leftBorder = rightBorder - 1;

		final TimestampFilter filter = TestTimestampFilter.createTimestampFilter(leftBorder, rightBorder);
		final SimpleSinkPlugin sinkPlugin = new SimpleSinkPlugin();

		Assert.assertTrue(sinkPlugin.getList().isEmpty());
		AbstractPlugin.connect(filter, TimestampFilter.OUTPUT_PORT_NAME, sinkPlugin, SimpleSinkPlugin.INPUT_PORT_NAME);
		filter.inputTraceEvent(TestTimestampFilter.event);

		Assert.assertFalse("Filter ignored event " + TestTimestampFilter.event + " although timestamp on right Border" + rightBorder
						, sinkPlugin.getList().isEmpty());

		Assert.assertTrue(sinkPlugin.getList().size() == 1);
		Assert.assertSame(sinkPlugin.getList().get(0), TestTimestampFilter.event);
	}

	/**
	 * Given a {@link TimestampFilter} selecting {@link TraceEvent}s within an interval <i>[a,b]</i>,
	 * assert that a {@link TraceEvent} <i>e</i> with <i>e.timestamp &gt; a</i> and <i>r.timestamp
	 * &gt; a </i> does pass the filter.
	 */
	@Test
	public void testRecordTinToutWithinRangePassed() {
		final long leftBorder = TestTimestampFilter.event.getTimestamp() - 1;
		final long rightBorder = TestTimestampFilter.event.getTimestamp() + 1;

		final TimestampFilter filter = TestTimestampFilter.createTimestampFilter(leftBorder, rightBorder);
		final SimpleSinkPlugin sinkPlugin = new SimpleSinkPlugin();

		Assert.assertTrue(sinkPlugin.getList().isEmpty());
		AbstractPlugin.connect(filter, TimestampFilter.OUTPUT_PORT_NAME, sinkPlugin, SimpleSinkPlugin.INPUT_PORT_NAME);
		filter.inputTraceEvent(TestTimestampFilter.event);

		Assert.assertFalse("Filter ignored event " + TestTimestampFilter.event + " although timestamp in interval [" + leftBorder + "," + rightBorder + "]"
					, sinkPlugin.getList().isEmpty());

		Assert.assertTrue(sinkPlugin.getList().size() == 1);
		Assert.assertSame(sinkPlugin.getList().get(0), TestTimestampFilter.event);
	}
}
