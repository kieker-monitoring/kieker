/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.analysisteetime.junit.plugin.filter.select;

import java.nio.BufferOverflowException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysisteetime.plugin.filter.select.timestampfilter.TimestampFilter;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.io.IValueSerializer;

import kieker.test.common.junit.AbstractKiekerTest;

import teetime.framework.AbstractStage;
import teetime.framework.test.StageTester;

/**
 * A test for the class {@link TimestampFilter}.
 *
 * @author Andre van Hoorn, Jan Waller, Lars Bluemke
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
		public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
			throw new UnsupportedOperationException("not used in this test");
		}

		@Override
		public String[] getValueNames() {
			throw new UnsupportedOperationException("not used in this test");
		}

		@Override
		public int getSize() {
			return 8 + 8 + 4;
		}

	};

	/**
	 * Default constructor.
	 */
	public TestTimestampFilter() {
		// empty default constructor
	}

	/**
	 * Given a {@link TimestampFilter} selecting {@link AbstractTraceEvent}s within an interval <i>[a,b]</i>,
	 * assert that a {@link AbstractTraceEvent} <i>e</i> with <i>e.timestamp &lt; a</i> does not pass the filter.
	 */
	@Test
	public void testEventBeforeIgnored() {
		final long leftBorder = TestTimestampFilter.EVENT.getTimestamp() + 1;
		final long rightBorder = leftBorder + 1;
		final TimestampFilter timestampStage = new TimestampFilter(leftBorder, rightBorder);
		final AbstractStage owningStage = timestampStage.getMonitoringRecordsCombinedInputPort().getOwningStage();
		final List<IMonitoringRecord> withinTimestampOutputs = new ArrayList<>();
		final List<IMonitoringRecord> outsideTimestampOutputs = new ArrayList<>();

		StageTester.test(owningStage)
				.and().send(TestTimestampFilter.EVENT).to(timestampStage.getMonitoringRecordsCombinedInputPort())
				.and().receive(withinTimestampOutputs).from(timestampStage.getRecordsWithinTimePeriodOutputPort())
				.and().receive(outsideTimestampOutputs).from(timestampStage.getRecordsOutsideTimePeriodOutputPort())
				.start();

		Assert.assertTrue(withinTimestampOutputs.isEmpty());
		Assert.assertSame(outsideTimestampOutputs.get(0), TestTimestampFilter.EVENT);
	}

	/**
	 * Given a {@link TimestampFilter} selecting {@link AbstractTraceEvent}s within an interval <i>[a,b]</i>,
	 * assert that a {@link AbstractTraceEvent} <i>e</i> with <i>e.timestamp &gt; b </i> does not
	 * pass the filter.
	 */
	@Test
	public void testEventAfterIgnored() {
		final long rightBorder = TestTimestampFilter.EVENT.getTimestamp() - 1;
		final long leftBorder = rightBorder - 1;
		final TimestampFilter timestampStage = new TimestampFilter(leftBorder, rightBorder);
		final AbstractStage owningStage = timestampStage.getMonitoringRecordsCombinedInputPort().getOwningStage();
		final List<IMonitoringRecord> withinTimestampOutputs = new ArrayList<>();
		final List<IMonitoringRecord> outsideTimestampOutputs = new ArrayList<>();

		StageTester.test(owningStage)
				.and().send(TestTimestampFilter.EVENT).to(timestampStage.getMonitoringRecordsCombinedInputPort())
				.and().receive(withinTimestampOutputs).from(timestampStage.getRecordsWithinTimePeriodOutputPort())
				.and().receive(outsideTimestampOutputs).from(timestampStage.getRecordsOutsideTimePeriodOutputPort())
				.start();

		Assert.assertTrue("Filter passed event " + TestTimestampFilter.EVENT + " although timestamp before " + leftBorder, withinTimestampOutputs.isEmpty());
		Assert.assertSame(outsideTimestampOutputs.get(0), TestTimestampFilter.EVENT);
	}

	/**
	 * Given a {@link TimestampFilter} selecting {@link AbstractTraceEvent}s within an interval <i>[a,b]</i>,
	 * assert that an event <i>e</i> with <i>e.timestamp == a</i> does pass the filter.
	 */
	@Test
	public void testRecordOnLeftBorderPasses() {
		final long leftBorder = TestTimestampFilter.EVENT.getTimestamp();
		final long rightBorder = leftBorder + 1;
		final TimestampFilter timestampStage = new TimestampFilter(leftBorder, rightBorder);
		final AbstractStage owningStage = timestampStage.getMonitoringRecordsCombinedInputPort().getOwningStage();
		final List<IMonitoringRecord> withinTimestampOutputs = new ArrayList<>();
		final List<IMonitoringRecord> outsideTimestampOutputs = new ArrayList<>();

		StageTester.test(owningStage)
				.and().send(TestTimestampFilter.EVENT).to(timestampStage.getMonitoringRecordsCombinedInputPort())
				.and().receive(withinTimestampOutputs).from(timestampStage.getRecordsWithinTimePeriodOutputPort())
				.and().receive(outsideTimestampOutputs).from(timestampStage.getRecordsOutsideTimePeriodOutputPort())
				.start();

		Assert.assertFalse("Filter ignored event " + TestTimestampFilter.EVENT + " although timestamp on left Border " + leftBorder,
				withinTimestampOutputs.isEmpty());
		Assert.assertTrue(withinTimestampOutputs.size() == 1);
		Assert.assertSame(withinTimestampOutputs.get(0), TestTimestampFilter.EVENT);
		Assert.assertTrue(outsideTimestampOutputs.isEmpty());

	}

	/**
	 * Given a {@link TimestampFilter} selecting {@link AbstractTraceEvent}s within an interval <i>[a,b]</i>,
	 * assert that an event <i>e</i> with <i>e.timestamp == b</i> does pass the filter.
	 */
	@Test
	public void testRecordOnRightBorderPasses() {
		final long rightBorder = TestTimestampFilter.EVENT.getTimestamp();
		final long leftBorder = rightBorder - 1;
		final TimestampFilter timestampStage = new TimestampFilter(leftBorder, rightBorder);
		final AbstractStage owningStage = timestampStage.getMonitoringRecordsCombinedInputPort().getOwningStage();
		final List<IMonitoringRecord> withinTimestampOutputs = new ArrayList<>();
		final List<IMonitoringRecord> outsideTimestampOutputs = new ArrayList<>();

		StageTester.test(owningStage)
				.and().send(TestTimestampFilter.EVENT).to(timestampStage.getMonitoringRecordsCombinedInputPort())
				.and().receive(withinTimestampOutputs).from(timestampStage.getRecordsWithinTimePeriodOutputPort())
				.and().receive(outsideTimestampOutputs).from(timestampStage.getRecordsOutsideTimePeriodOutputPort())
				.start();

		Assert.assertFalse("Filter ignored event " + TestTimestampFilter.EVENT + " although timestamp on right Border " + rightBorder,
				withinTimestampOutputs.isEmpty());
		Assert.assertTrue(withinTimestampOutputs.size() == 1);
		Assert.assertSame(withinTimestampOutputs.get(0), TestTimestampFilter.EVENT);
		Assert.assertTrue(outsideTimestampOutputs.isEmpty());
	}

	/**
	 * Given a {@link TimestampFilter} selecting {@link AbstractTraceEvent}s within an interval <i>[a,b]</i>,
	 * assert that a {@link AbstractTraceEvent} <i>e</i> with <i>e.timestamp &gt; a</i> and <i>r.timestamp
	 * &gt; a </i> does pass the filter.
	 */
	@Test
	public void testRecordTinToutWithinRangePassed() {
		final long leftBorder = TestTimestampFilter.EVENT.getTimestamp() - 1;
		final long rightBorder = TestTimestampFilter.EVENT.getTimestamp() + 1;
		final TimestampFilter timestampStage = new TimestampFilter(leftBorder, rightBorder);
		final AbstractStage owningStage = timestampStage.getMonitoringRecordsCombinedInputPort().getOwningStage();
		final List<IMonitoringRecord> withinTimestampOutputs = new ArrayList<>();
		final List<IMonitoringRecord> outsideTimestampOutputs = new ArrayList<>();

		StageTester.test(owningStage)
				.and().send(TestTimestampFilter.EVENT).to(timestampStage.getMonitoringRecordsCombinedInputPort())
				.and().receive(withinTimestampOutputs).from(timestampStage.getRecordsWithinTimePeriodOutputPort())
				.and().receive(outsideTimestampOutputs).from(timestampStage.getRecordsOutsideTimePeriodOutputPort())
				.start();

		Assert.assertFalse("Filter ignored event " + TestTimestampFilter.EVENT + " although timestamp in interval [" + leftBorder + "," + rightBorder + "]",
				withinTimestampOutputs.isEmpty());
		Assert.assertTrue(withinTimestampOutputs.size() == 1);
		Assert.assertSame(withinTimestampOutputs.get(0), TestTimestampFilter.EVENT);
		Assert.assertTrue(outsideTimestampOutputs.isEmpty());
	}
}
