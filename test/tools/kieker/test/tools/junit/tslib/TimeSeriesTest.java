/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.tools.tslib.TimeSeries;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * 
 * @author Tillmann Carlos Bielefeld
 * 
 */
public class TimeSeriesTest extends AbstractKiekerTest {

	private TimeSeries<Double> unboundTS;
	private int bounds;
	private TimeSeries<Integer> boundedTS;
	private TimeUnit timeUnit;
	private long startTime;

	/**
	 * Creates a new instance of this class.
	 */
	public TimeSeriesTest() {
		// Default constructor
	}

	/**
	 * Setup of the test case.
	 * 
	 * @throws Exception
	 *             Thrown if exception appears
	 */
	@Before
	public void setUp() throws Exception {
		final long deltaTime = 1000;
		this.timeUnit = TimeUnit.MILLISECONDS;
		this.startTime = System.currentTimeMillis() - (deltaTime * 10);
		this.unboundTS = new TimeSeries<Double>(this.startTime, deltaTime, this.timeUnit);

		this.bounds = 3;
		this.boundedTS = new TimeSeries<Integer>(this.startTime, deltaTime, this.timeUnit, this.bounds);
	}

	/**
	 * Test of the getter and appends methods.
	 */
	@Test
	public void testGettersAndAppendingValues() {
		Assert.assertEquals(this.timeUnit, this.unboundTS.getDeltaTimeUnit());
		Assert.assertEquals(this.startTime, this.unboundTS.getStartTime());

		Assert.assertEquals(0, this.unboundTS.size());
		this.unboundTS.append(666.0);
		this.unboundTS.append(666.0);
		Assert.assertEquals(2, this.unboundTS.size());
	}

	/**
	 * Test if values are collected in the correct order.
	 */
	@Test
	public void testValueSort() {
		final int count = 30;
		for (int i = 0; i < count; i++) {
			this.unboundTS.append(Double.valueOf(i));
		}

		for (int i = 0; i < count; i++) {
			Assert.assertEquals(Double.valueOf(i), this.unboundTS.getPoints().get(i).getValue());
		}

		Assert.assertEquals(count, this.unboundTS.size());
	}

	/**
	 * Test of the buffer capacity.
	 */
	@Test
	public void testCapacityRestriction() {
		Assert.assertEquals(0, this.boundedTS.size());
		Assert.assertEquals(this.bounds, this.boundedTS.getCapacity());
		for (int i = 0; i < (this.bounds + 1); i++) {
			this.boundedTS.append(10 * i);
		}
		Assert.assertEquals(this.bounds, this.boundedTS.size());
	}

	/**
	 * Test if newer values are stored in FIFO order.
	 */
	@Test
	public void testKeepNewerValuesInCapacity() {
		Assert.assertEquals(0, this.boundedTS.size());
		int i;
		final int lastNumber = this.bounds * 2;
		for (i = 0; i <= lastNumber; i++) {
			this.boundedTS.append(i);
		}

		Assert.assertEquals(Integer.valueOf(lastNumber), this.boundedTS.getPoints().get(this.bounds - 1).getValue());
	}

}
