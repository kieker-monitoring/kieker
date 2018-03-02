/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.analysis.junit.plugin.filter.select;

import java.nio.BufferOverflowException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.filter.select.TimestampFilter;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.io.IValueSerializer;
import kieker.common.util.registry.IRegistry;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * A test for the class {@link TimestampFilter}.
 *
 * @author Andre van Hoorn, Jan Waller
 *
 * @since 1.5
 */
public final class TestTimestampFilter extends AbstractKiekerTest {

	private static final AbstractTraceEvent EVENT = new AbstractTraceEvent(34556L, 324440L, 0) {
		private static final long serialVersionUID = 1L;

		@Override
		public Object[] toArray() {
			return new Object[] { this.getTimestamp(), this.getTraceId(), this.getOrderIndex(), };
		}

		@Override
		public Class<?>[] getValueTypes() {
			return new Class<?>[] { long.class, long.class, int.class, };
		}

		@Override
		public void registerStrings(final IRegistry<String> stringRegistry) {
			// not used here
		}

		@Override
		public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
			// not used here
		}

		@Override
		public int getSize() {
			return 8 + 8 + 4;
		}

		@Override
		public String[] getValueNames() {
			return new String[] { "timestamp", "traceId", "orderIndex" };
		}

	};

	private ListReader<AbstractTraceEvent> reader;
	private ListCollectionFilter<AbstractTraceEvent> sinkPlugin;
	private IAnalysisController controller;

	/**
	 * Default constructor.
	 */
	public TestTimestampFilter() {
		// empty default constructor
	}

	/**
	 * Creates a {@link TimestampFilter} with the given properties
	 * using the constructor {@link TimestampFilter#TimestampFilter(kieker.common.configuration.Configuration, java.util.Map)}.
	 *
	 * @param ignoreExecutionsBeforeTimestamp
	 * @param ignoreExecutionsAfterTimestamp
	 * @return
	 * @throws AnalysisConfigurationException
	 *             If the internally assembled analysis configuration is somehow invalid.
	 * @throws IllegalStateException
	 *             If the internal analysis is in an invalid state.
	 */
	private void createTimestampFilter(final long ignoreExecutionsBeforeTimestamp, final long ignoreExecutionsAfterTimestamp) throws IllegalStateException,
			AnalysisConfigurationException {
		final Configuration cfg = new Configuration();
		cfg.setProperty(TimestampFilter.CONFIG_PROPERTY_NAME_IGNORE_BEFORE_TIMESTAMP, Long.toString(ignoreExecutionsBeforeTimestamp));
		cfg.setProperty(TimestampFilter.CONFIG_PROPERTY_NAME_IGNORE_AFTER_TIMESTAMP, Long.toString(ignoreExecutionsAfterTimestamp));
		final TimestampFilter filter = new TimestampFilter(cfg, this.controller);
		this.controller.connect(this.reader, ListReader.OUTPUT_PORT_NAME, filter, TimestampFilter.INPUT_PORT_NAME_FLOW);
		this.controller.connect(filter, TimestampFilter.OUTPUT_PORT_NAME_WITHIN_PERIOD, this.sinkPlugin, ListCollectionFilter.INPUT_PORT_NAME);
	}

	/**
	 * This method initializes the setup.
	 */
	@Before
	public void before() {
		this.controller = new AnalysisController();
		this.reader = new ListReader<AbstractTraceEvent>(new Configuration(), this.controller);
		this.sinkPlugin = new ListCollectionFilter<AbstractTraceEvent>(new Configuration(), this.controller);
	}

	/**
	 * Given a {@link TimestampFilter} selecting {@link AbstractTraceEvent}s within an interval <i>[a,b]</i>,
	 * assert that a {@link AbstractTraceEvent} <i>e</i> with <i>e.timestamp &lt; a</i> does not pass the filter.
	 *
	 * @throws AnalysisConfigurationException
	 *             If the internally assembled analysis configuration is somehow invalid.
	 * @throws IllegalStateException
	 *             If the internal analysis is in an invalid state.
	 */
	@Test
	public void testEventBeforeIgnored() throws IllegalStateException, AnalysisConfigurationException {
		final long leftBorder = TestTimestampFilter.EVENT.getTimestamp() + 1;
		final long rightBorder = leftBorder + 1;
		this.createTimestampFilter(leftBorder, rightBorder);
		Assert.assertTrue(this.sinkPlugin.getList().isEmpty());
		this.reader.addObject(TestTimestampFilter.EVENT);
		this.controller.run();
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, this.controller.getState());
		Assert.assertTrue("Filter passed event " + TestTimestampFilter.EVENT + " although timestamp before " + leftBorder, this.sinkPlugin.getList().isEmpty());

	}

	/**
	 * Given a {@link TimestampFilter} selecting {@link AbstractTraceEvent}s within an interval <i>[a,b]</i>,
	 * assert that a {@link AbstractTraceEvent} <i>e</i> with <i>e.timestamp &gt; b </i> does not
	 * pass the filter.
	 *
	 * @throws AnalysisConfigurationException
	 *             If the internally assembled analysis configuration is somehow invalid.
	 * @throws IllegalStateException
	 *             If the internal analysis is in an invalid state.
	 */
	@Test
	public void testEventAfterIgnored() throws IllegalStateException, AnalysisConfigurationException {
		final long rightBorder = TestTimestampFilter.EVENT.getTimestamp() - 1;
		final long leftBorder = rightBorder - 1;
		this.createTimestampFilter(leftBorder, rightBorder);
		Assert.assertTrue(this.sinkPlugin.getList().isEmpty());
		this.reader.addObject(TestTimestampFilter.EVENT);
		this.controller.run();
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, this.controller.getState());
		Assert.assertTrue("Filter passed event " + TestTimestampFilter.EVENT + " although timestamp before " + leftBorder, this.sinkPlugin.getList().isEmpty());
	}

	/**
	 * Given a {@link TimestampFilter} selecting {@link AbstractTraceEvent}s within an interval <i>[a,b]</i>,
	 * assert that an event <i>e</i> with <i>e.timestamp == a</i> does pass the filter.
	 *
	 * @throws AnalysisConfigurationException
	 *             If the internally assembled analysis configuration is somehow invalid.
	 * @throws IllegalStateException
	 *             If the internal analysis is in an invalid state.
	 */
	@Test
	public void testRecordOnLeftBorderPasses() throws IllegalStateException, AnalysisConfigurationException {
		final long leftBorder = TestTimestampFilter.EVENT.getTimestamp();
		final long rightBorder = leftBorder + 1;
		this.createTimestampFilter(leftBorder, rightBorder);
		Assert.assertTrue(this.sinkPlugin.getList().isEmpty());
		this.reader.addObject(TestTimestampFilter.EVENT);
		this.controller.run();
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, this.controller.getState());
		Assert.assertFalse("Filter ignored event " + TestTimestampFilter.EVENT + " although timestamp on left Border " + leftBorder,
				this.sinkPlugin.getList().isEmpty());
		Assert.assertTrue(this.sinkPlugin.getList().size() == 1);
		Assert.assertSame(this.sinkPlugin.getList().get(0), TestTimestampFilter.EVENT);
	}

	/**
	 * Given a {@link TimestampFilter} selecting {@link AbstractTraceEvent}s within an interval <i>[a,b]</i>,
	 * assert that an event <i>e</i> with <i>e.timestamp == b</i> does pass the filter.
	 *
	 * @throws AnalysisConfigurationException
	 *             If the internally assembled analysis configuration is somehow invalid.
	 * @throws IllegalStateException
	 *             If the internal analysis is in an invalid state.
	 */
	@Test
	public void testRecordOnRightBorderPasses() throws IllegalStateException, AnalysisConfigurationException {
		final long rightBorder = TestTimestampFilter.EVENT.getTimestamp();
		final long leftBorder = rightBorder - 1;
		this.createTimestampFilter(leftBorder, rightBorder);
		Assert.assertTrue(this.sinkPlugin.getList().isEmpty());
		this.reader.addObject(TestTimestampFilter.EVENT);
		this.controller.run();
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, this.controller.getState());
		Assert.assertFalse("Filter ignored event " + TestTimestampFilter.EVENT + " although timestamp on right Border " + rightBorder,
				this.sinkPlugin.getList().isEmpty());
		Assert.assertTrue(this.sinkPlugin.getList().size() == 1);
		Assert.assertSame(this.sinkPlugin.getList().get(0), TestTimestampFilter.EVENT);
	}

	/**
	 * Given a {@link TimestampFilter} selecting {@link AbstractTraceEvent}s within an interval <i>[a,b]</i>,
	 * assert that a {@link AbstractTraceEvent} <i>e</i> with <i>e.timestamp &gt; a</i> and <i>r.timestamp
	 * &gt; a </i> does pass the filter.
	 *
	 * @throws AnalysisConfigurationException
	 *             If the internally assembled analysis configuration is somehow invalid.
	 * @throws IllegalStateException
	 *             If the internally assembled analysis is in an invalid state.
	 */
	@Test
	public void testRecordTinToutWithinRangePassed() throws IllegalStateException, AnalysisConfigurationException {
		final long leftBorder = TestTimestampFilter.EVENT.getTimestamp() - 1;
		final long rightBorder = TestTimestampFilter.EVENT.getTimestamp() + 1;
		this.createTimestampFilter(leftBorder, rightBorder);
		Assert.assertTrue(this.sinkPlugin.getList().isEmpty());
		this.reader.addObject(TestTimestampFilter.EVENT);
		this.controller.run();
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, this.controller.getState());
		Assert.assertFalse("Filter ignored event " + TestTimestampFilter.EVENT + " although timestamp in interval [" + leftBorder + "," + rightBorder + "]",
				this.sinkPlugin.getList().isEmpty());
		Assert.assertTrue(this.sinkPlugin.getList().size() == 1);
		Assert.assertSame(this.sinkPlugin.getList().get(0), TestTimestampFilter.EVENT);
	}
}
