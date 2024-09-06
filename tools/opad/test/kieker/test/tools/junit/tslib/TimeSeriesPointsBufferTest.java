/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.junit.tslib;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.tools.opad.timeseries.TimeSeriesPointsBuffer;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 *
 * @author Tom Frotscher
 * @since 1.10
 *
 */
public class TimeSeriesPointsBufferTest extends AbstractKiekerTest {

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
		final TimeSeriesPointsBuffer<Integer> bounded = new TimeSeriesPointsBuffer<>(3);
		bounded.add(1);
		bounded.add(2);
		bounded.add(3);
		// Next Value exceed the boundary
		bounded.add(4);
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
		final TimeSeriesPointsBuffer<Integer> unbounded = new TimeSeriesPointsBuffer<>(-1);
		int i = 0;
		while (i < 100) {
			unbounded.add(i);
			i++;
		}
		Assert.assertEquals(i, unbounded.getSize());
		// Test remove
		unbounded.remove();
		Assert.assertEquals(i - 1, unbounded.getSize());
	}
}
