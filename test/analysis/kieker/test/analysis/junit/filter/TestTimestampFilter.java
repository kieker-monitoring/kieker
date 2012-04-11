/***************************************************************************
 * Copyright 2012 by
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

import org.junit.Before;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.select.TimestampFilter;
import kieker.common.configuration.Configuration;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.test.analysis.junit.plugin.SimpleSinkPlugin;

/**
 * 
 * @author Andre van Hoorn, Jan Waller
 */
public final class TestTimestampFilter {

	private static final AbstractTraceEvent EVENT = new AbstractTraceEvent(34556L, 324440L, 0) {
		private static final long serialVersionUID = 1L;

		public Object[] toArray() {
			return new Object[] { this.getTimestamp(), this.getTraceId(), this.getOrderIndex(), };
		}

		public Class<?>[] getValueTypes() {
			return new Class<?>[] { long.class, long.class, int.class, };
		}
	};

	private SimpleSinkPlugin<AbstractTraceEvent> sinkPlugin;
	private final AnalysisController controller = new AnalysisController();

	public TestTimestampFilter() {
		// empty default constructor
	}

	/**
	 * Creates a {@link TimestampFilter} with the given properties
	 * using the constructor {@link TimestampFilter#TimestampFilter(kieker.common.configuration.Configuration, java.util.Map)}
	 * 
	 * @param ignoreExecutionsBeforeTimestamp
	 * @param ignoreExecutionsAfterTimestamp
	 * @return
	 * @throws AnalysisConfigurationException
	 * @throws IllegalStateException
	 */
	private TimestampFilter createTimestampFilter(final long ignoreExecutionsBeforeTimestamp, final long ignoreExecutionsAfterTimestamp)
			throws IllegalStateException, AnalysisConfigurationException {
		final Configuration cfg = new Configuration();
		cfg.setProperty(TimestampFilter.CONFIG_PROPERTY_NAME_IGNORE_BEFORE_TIMESTAMP, Long.toString(ignoreExecutionsBeforeTimestamp));
		cfg.setProperty(TimestampFilter.CONFIG_PROPERTY_NAME_IGNORE_AFTER_TIMESTAMP, Long.toString(ignoreExecutionsAfterTimestamp));
		final TimestampFilter filter = new TimestampFilter(cfg);
		this.controller.registerFilter(filter);
		this.controller.connect(filter, TimestampFilter.OUTPUT_PORT_NAME_WITHIN_PERIOD, this.sinkPlugin, SimpleSinkPlugin.INPUT_PORT_NAME);
		return filter;
	}

	@Before
	public void before() {
		this.sinkPlugin = new SimpleSinkPlugin<AbstractTraceEvent>(new Configuration());
		this.controller.registerFilter(this.sinkPlugin);
	}

	/**
	 * Given a {@link TimestampFilter} selecting {@link AbstractTraceEvent}s within an interval <i>[a,b]</i>,
	 * assert that a {@link AbstractTraceEvent} <i>e</i> with <i>e.timestamp &lt; a</i> does not pass the filter.
	 * 
	 * @throws AnalysisConfigurationException
	 * @throws IllegalStateException
	 */
	@Test
	public void testEventBeforeIgnored() throws IllegalStateException, AnalysisConfigurationException {
		final long leftBorder = TestTimestampFilter.EVENT.getTimestamp() + 1;
		final long rightBorder = leftBorder + 1;
		final TimestampFilter filter = this.createTimestampFilter(leftBorder, rightBorder);
		Assert.assertTrue(this.sinkPlugin.getList().isEmpty());
		filter.inputTraceEvent(TestTimestampFilter.EVENT);
		Assert.assertTrue("Filter passed event " + TestTimestampFilter.EVENT + " although timestamp before" + leftBorder
				, this.sinkPlugin.getList().isEmpty());

	}

	/**
	 * Given a {@link TimestampFilter} selecting {@link AbstractTraceEvent}s within an interval <i>[a,b]</i>,
	 * assert that a {@link AbstractTraceEvent} <i>e</i> with <i>e.timestamp &gt; b </i> does not
	 * pass the filter.
	 * 
	 * @throws AnalysisConfigurationException
	 * @throws IllegalStateException
	 */
	@Test
	public void testEventAfterIgnored() throws IllegalStateException, AnalysisConfigurationException {
		final long rightBorder = TestTimestampFilter.EVENT.getTimestamp() - 1;
		final long leftBorder = rightBorder - 1;
		final TimestampFilter filter = this.createTimestampFilter(leftBorder, rightBorder);
		Assert.assertTrue(this.sinkPlugin.getList().isEmpty());
		filter.inputTraceEvent(TestTimestampFilter.EVENT);
		Assert.assertTrue("Filter passed event " + TestTimestampFilter.EVENT + " although timestamp before" + leftBorder
				, this.sinkPlugin.getList().isEmpty());
	}

	/**
	 * Given a {@link TimestampFilter} selecting {@link AbstractTraceEvent}s within an interval <i>[a,b]</i>,
	 * assert that an event <i>e</i> with <i>e.timestamp == a</i> does pass the filter.
	 * 
	 * @throws AnalysisConfigurationException
	 * @throws IllegalStateException
	 */
	@Test
	public void testRecordOnLeftBorderPasses() throws IllegalStateException, AnalysisConfigurationException {
		final long leftBorder = TestTimestampFilter.EVENT.getTimestamp();
		final long rightBorder = leftBorder + 1;
		final TimestampFilter filter = this.createTimestampFilter(leftBorder, rightBorder);

		Assert.assertTrue(this.sinkPlugin.getList().isEmpty());
		filter.inputTraceEvent(TestTimestampFilter.EVENT);
		Assert.assertFalse("Filter ignored event " + TestTimestampFilter.EVENT + " although timestamp on left Border" + leftBorder
				, this.sinkPlugin.getList().isEmpty());
		Assert.assertTrue(this.sinkPlugin.getList().size() == 1);
		Assert.assertSame(this.sinkPlugin.getList().get(0), TestTimestampFilter.EVENT);
	}

	/**
	 * Given a {@link TimestampFilter} selecting {@link AbstractTraceEvent}s within an interval <i>[a,b]</i>,
	 * assert that an event <i>e</i> with <i>e.timestamp == b</i> does pass the filter.
	 * 
	 * @throws AnalysisConfigurationException
	 * @throws IllegalStateException
	 */
	@Test
	public void testRecordOnRightBorderPasses() throws IllegalStateException, AnalysisConfigurationException {
		final long rightBorder = TestTimestampFilter.EVENT.getTimestamp();
		final long leftBorder = rightBorder - 1;
		final TimestampFilter filter = this.createTimestampFilter(leftBorder, rightBorder);
		Assert.assertTrue(this.sinkPlugin.getList().isEmpty());
		filter.inputTraceEvent(TestTimestampFilter.EVENT);
		Assert.assertFalse("Filter ignored event " + TestTimestampFilter.EVENT + " although timestamp on right Border" + rightBorder
				, this.sinkPlugin.getList().isEmpty());
		Assert.assertTrue(this.sinkPlugin.getList().size() == 1);
		Assert.assertSame(this.sinkPlugin.getList().get(0), TestTimestampFilter.EVENT);
	}

	/**
	 * Given a {@link TimestampFilter} selecting {@link AbstractTraceEvent}s within an interval <i>[a,b]</i>,
	 * assert that a {@link AbstractTraceEvent} <i>e</i> with <i>e.timestamp &gt; a</i> and <i>r.timestamp
	 * &gt; a </i> does pass the filter.
	 * 
	 * @throws AnalysisConfigurationException
	 * @throws IllegalStateException
	 */
	@Test
	public void testRecordTinToutWithinRangePassed() throws IllegalStateException, AnalysisConfigurationException {
		final long leftBorder = TestTimestampFilter.EVENT.getTimestamp() - 1;
		final long rightBorder = TestTimestampFilter.EVENT.getTimestamp() + 1;
		final TimestampFilter filter = this.createTimestampFilter(leftBorder, rightBorder);
		Assert.assertTrue(this.sinkPlugin.getList().isEmpty());
		filter.inputTraceEvent(TestTimestampFilter.EVENT);
		Assert.assertFalse("Filter ignored event " + TestTimestampFilter.EVENT + " although timestamp in interval [" + leftBorder + "," + rightBorder + "]"
				, this.sinkPlugin.getList().isEmpty());
		Assert.assertTrue(this.sinkPlugin.getList().size() == 1);
		Assert.assertSame(this.sinkPlugin.getList().get(0), TestTimestampFilter.EVENT);
	}
}
