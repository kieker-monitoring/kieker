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

package kieker.test.analysis.junit.filter;

import junit.framework.Assert;
import kieker.analysis.filter.TimestampFilter;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.test.analysis.junit.plugin.SimpleSinkPlugin;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Andre van Hoorn, Jan Waller
 */
public final class TestTimestampFilter {

	private static final AbstractTraceEvent event = new AbstractTraceEvent(34556L, 324440L, 0) {
		private static final long serialVersionUID = 1L;

		@Override
		public Object[] toArray() {
			return new Object[] { this.getTimestamp(), this.getTraceId(), this.getOrderIndex(), };
		}

		@Override
		public Class<?>[] getValueTypes() {
			return new Class<?>[] { long.class, long.class, int.class, };
		}
	};

	private SimpleSinkPlugin sinkPlugin;

	/**
	 * Creates a {@link TimestampFilter} with the given properties
	 * using the constructor {@link TimestampFilter#TimestampFilter(kieker.common.configuration.Configuration, java.util.Map)}
	 * 
	 * @param ignoreExecutionsBeforeTimestamp
	 * @param ignoreExecutionsAfterTimestamp
	 * @return
	 */
	private TimestampFilter createTimestampFilter(final long ignoreExecutionsBeforeTimestamp, final long ignoreExecutionsAfterTimestamp) {
		final Configuration cfg = new Configuration();
		cfg.setProperty(TimestampFilter.CONFIG_IGNORE_EXECUTIONS_BEFORE_TIMESTAMP, Long.toString(ignoreExecutionsBeforeTimestamp));
		cfg.setProperty(TimestampFilter.CONFIG_IGNORE_EXECUTIONS_AFTER_TIMESTAMP, Long.toString(ignoreExecutionsAfterTimestamp));
		final TimestampFilter filter = new TimestampFilter(cfg);
		AbstractPlugin.connect(filter, TimestampFilter.OUTPUT_PORT_NAME, this.sinkPlugin, SimpleSinkPlugin.INPUT_PORT_NAME);
		return filter;
	}

	@Before
	public void before() {
		this.sinkPlugin = new SimpleSinkPlugin(new Configuration());
	}

	/**
	 * Given a {@link TimestampFilter} selecting {@link AbstractTraceEvent}s within an interval <i>[a,b]</i>,
	 * assert that a {@link AbstractTraceEvent} <i>e</i> with <i>e.timestamp &lt; a</i> does not pass the filter.
	 */
	@Test
	public void testEventBeforeIgnored() {
		final long leftBorder = TestTimestampFilter.event.getTimestamp() + 1;
		final long rightBorder = leftBorder + 1;
		final TimestampFilter filter = this.createTimestampFilter(leftBorder, rightBorder);
		Assert.assertTrue(this.sinkPlugin.getList().isEmpty());
		filter.inputTraceEvent(TestTimestampFilter.event);
		Assert.assertTrue("Filter passed event " + TestTimestampFilter.event + " although timestamp before" + leftBorder
				, this.sinkPlugin.getList().isEmpty());

	}

	/**
	 * Given a {@link TimestampFilter} selecting {@link AbstractTraceEvent}s within an interval <i>[a,b]</i>,
	 * assert that a {@link AbstractTraceEvent} <i>e</i> with <i>e.timestamp &gt; b </i> does not
	 * pass the filter.
	 */
	@Test
	public void testEventAfterIgnored() {
		final long rightBorder = TestTimestampFilter.event.getTimestamp() - 1;
		final long leftBorder = rightBorder - 1;
		final TimestampFilter filter = this.createTimestampFilter(leftBorder, rightBorder);
		Assert.assertTrue(this.sinkPlugin.getList().isEmpty());
		filter.inputTraceEvent(TestTimestampFilter.event);
		Assert.assertTrue("Filter passed event " + TestTimestampFilter.event + " although timestamp before" + leftBorder
				, this.sinkPlugin.getList().isEmpty());
	}

	/**
	 * Given a {@link TimestampFilter} selecting {@link AbstractTraceEvent}s within an interval <i>[a,b]</i>,
	 * assert that an event <i>e</i> with <i>e.timestamp == a</i> does pass the filter.
	 */
	@Test
	public void testRecordOnLeftBorderPasses() {
		final long leftBorder = TestTimestampFilter.event.getTimestamp();
		final long rightBorder = leftBorder + 1;
		final TimestampFilter filter = this.createTimestampFilter(leftBorder, rightBorder);

		Assert.assertTrue(this.sinkPlugin.getList().isEmpty());
		filter.inputTraceEvent(TestTimestampFilter.event);
		Assert.assertFalse("Filter ignored event " + TestTimestampFilter.event + " although timestamp on left Border" + leftBorder
				, this.sinkPlugin.getList().isEmpty());
		Assert.assertTrue(this.sinkPlugin.getList().size() == 1);
		Assert.assertSame(this.sinkPlugin.getList().get(0), TestTimestampFilter.event);
	}

	/**
	 * Given a {@link TimestampFilter} selecting {@link AbstractTraceEvent}s within an interval <i>[a,b]</i>,
	 * assert that an event <i>e</i> with <i>e.timestamp == b</i> does pass the filter.
	 */
	@Test
	public void testRecordOnRightBorderPasses() {
		final long rightBorder = TestTimestampFilter.event.getTimestamp();
		final long leftBorder = rightBorder - 1;
		final TimestampFilter filter = this.createTimestampFilter(leftBorder, rightBorder);
		Assert.assertTrue(this.sinkPlugin.getList().isEmpty());
		filter.inputTraceEvent(TestTimestampFilter.event);
		Assert.assertFalse("Filter ignored event " + TestTimestampFilter.event + " although timestamp on right Border" + rightBorder
				, this.sinkPlugin.getList().isEmpty());
		Assert.assertTrue(this.sinkPlugin.getList().size() == 1);
		Assert.assertSame(this.sinkPlugin.getList().get(0), TestTimestampFilter.event);
	}

	/**
	 * Given a {@link TimestampFilter} selecting {@link AbstractTraceEvent}s within an interval <i>[a,b]</i>,
	 * assert that a {@link AbstractTraceEvent} <i>e</i> with <i>e.timestamp &gt; a</i> and <i>r.timestamp
	 * &gt; a </i> does pass the filter.
	 */
	@Test
	public void testRecordTinToutWithinRangePassed() {
		final long leftBorder = TestTimestampFilter.event.getTimestamp() - 1;
		final long rightBorder = TestTimestampFilter.event.getTimestamp() + 1;
		final TimestampFilter filter = this.createTimestampFilter(leftBorder, rightBorder);
		Assert.assertTrue(this.sinkPlugin.getList().isEmpty());
		filter.inputTraceEvent(TestTimestampFilter.event);
		Assert.assertFalse("Filter ignored event " + TestTimestampFilter.event + " although timestamp in interval [" + leftBorder + "," + rightBorder + "]"
				, this.sinkPlugin.getList().isEmpty());
		Assert.assertTrue(this.sinkPlugin.getList().size() == 1);
		Assert.assertSame(this.sinkPlugin.getList().get(0), TestTimestampFilter.event);
	}
}
