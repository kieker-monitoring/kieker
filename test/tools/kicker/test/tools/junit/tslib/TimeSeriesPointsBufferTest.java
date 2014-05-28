/***************************************************************************
 * Copyright 2014 Kicker Project (http://kicker-monitoring.net)
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

package kicker.test.tools.junit.tslib;

import org.junit.Assert;
import org.junit.Test;

import kicker.analysis.exception.AnalysisConfigurationException;
import kicker.test.common.junit.AbstractKickerTest;
import kicker.tools.tslib.TimeSeriesPoint;
import kicker.tools.tslib.TimeSeriesPointsBuffer;

/**
 * 
 * @author Tom Frotscher
 * @since 1.9
 * 
 */
public class TimeSeriesPointsBufferTest extends AbstractKickerTest {

	/**
	 * Creates a new instance of this class.
	 */
	public TimeSeriesPointsBufferTest() {
		// Default Constructor
	}

	/**
	 * Test of the bounded buffer version of the time series point buffer.
	 * 
	 * @throws InterruptedException
	 *             If interrupted
	 * @throws IllegalStateException
	 *             If illegal state is reached
	 * @throws AnalysisConfigurationException
	 *             If analysis configuration exception appears
	 */
	@Test
	public void testBoundedBuffer() throws InterruptedException, IllegalStateException, AnalysisConfigurationException {
		final TimeSeriesPointsBuffer<TimeSeriesPoint<Integer>> bounded = new TimeSeriesPointsBuffer<TimeSeriesPoint<Integer>>(3);
		bounded.add(new TimeSeriesPoint<Integer>(1, 1));
		bounded.add(new TimeSeriesPoint<Integer>(1, 2));
		bounded.add(new TimeSeriesPoint<Integer>(1, 3));

		// Next Value exceed the boundary
		bounded.add(new TimeSeriesPoint<Integer>(1, 4));
		Assert.assertEquals(3, bounded.getSize());

		// Test remove
		bounded.remove();
		Assert.assertEquals(2, bounded.getSize());
	}

	/**
	 * Test of the unbounded buffer version of the time series point buffer.
	 * 
	 * @throws InterruptedException
	 *             If interrupted
	 * @throws IllegalStateException
	 *             If illegal state is reached
	 * @throws AnalysisConfigurationException
	 *             If analysis configuration exception appears
	 */
	@Test
	public void testUnboundedBuffer() throws InterruptedException, IllegalStateException, AnalysisConfigurationException {
		final TimeSeriesPointsBuffer<TimeSeriesPoint<Integer>> unbounded = new TimeSeriesPointsBuffer<TimeSeriesPoint<Integer>>(-1);
		int i = 0;
		while (i < 100) {
			unbounded.add(new TimeSeriesPoint<Integer>(1, i));
			i++;
		}
		Assert.assertEquals(i, unbounded.getSize());
		// Test remove
		unbounded.remove();
		Assert.assertEquals(i - 1, unbounded.getSize());
	}
}
