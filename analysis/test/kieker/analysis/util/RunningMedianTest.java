/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.util;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class RunningMedianTest {

	@Rule
	public ExpectedException exceptions = ExpectedException.none();

	public RunningMedianTest() {
		// empty default constructor
	}

	@Test
	public void testEmptySet() { // NOPMD expect exception
		this.exceptions.expect(IllegalStateException.class);

		final RunningMedian<Long> runningMedian = RunningMedian.forLong();
		runningMedian.getMedian();
	}

	@Test
	public void testOneElement() {
		final RunningMedian<Integer> runningMedian = RunningMedian.forInteger();
		runningMedian.add(5);
		final long median = runningMedian.getMedian();
		Assert.assertEquals(5, median);
	}

	@Test
	public void testNineElements() {
		final RunningMedian<Integer> runningMedian = RunningMedian.forInteger();
		runningMedian.add(15);
		runningMedian.add(5);
		runningMedian.add(100);
		runningMedian.add(5);
		runningMedian.add(4);
		runningMedian.add(10);
		runningMedian.add(1);
		runningMedian.add(20);
		runningMedian.add(1000);
		final long median = runningMedian.getMedian();
		Assert.assertEquals(10, median);
	}

	@Test
	public void testEvenNumberWithDefaultMeanBuilder() {
		final RunningMedian<Integer> runningMedian = new RunningMedian<>();
		runningMedian.add(8);
		runningMedian.add(6);
		runningMedian.add(2);
		runningMedian.add(4);
		final long median = runningMedian.getMedian();
		Assert.assertEquals(4, median);
	}

	@Test
	public void testEvenNumberWithDefaultIntegerMeanBuilder() {
		final RunningMedian<Integer> runningMedian = RunningMedian.forInteger();
		runningMedian.add(8);
		runningMedian.add(6);
		runningMedian.add(2);
		runningMedian.add(4);
		final long median = runningMedian.getMedian();
		Assert.assertEquals(5, median);
	}

	@Test
	public void testEvenNumberWithDefaultDoubleMeanBuilder() {
		final RunningMedian<Double> runningMedian = RunningMedian.forDouble();
		runningMedian.add(4.0);
		runningMedian.add(3.0);
		runningMedian.add(1.0);
		runningMedian.add(2.0);
		final double median = runningMedian.getMedian();
		Assert.assertEquals(2.5, median, 0.0);
	}

}
