/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.junit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import kieker.tools.tslib.ITimeSeries;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.tools.util.RserveControl;

/**
 * Test that ensures that every time a R-dependent test is run, a fresh
 * Rserve instance is started beforehand and terminated afterwards.
 * 
 * @author Thomas Düllmann
 * 
 */
public abstract class AbstractKiekerRTest extends AbstractKiekerTest {

	private RserveControl rServeControl;

	@Before
	public void setUp() throws Exception {

		// // To make sure, that RBridgeControl does not hold a connection
		// // to an old Rserve instance, we have to enforce a new connection
		// this.rServeControl = new RserveControl();
		// Assume.assumeTrue(this.rServeControl.start());
	}

	@After
	public void tearDown() {
		// if ((this.rServeControl != null) && this.rServeControl.isRserveRunning()) {
		// this.rServeControl.terminate();
		// try {
		// Thread.sleep(500);
		// } catch (final InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
	}

	public static boolean tsContainsPoints(final ITimeSeries<Double> timeSeries) {
		return timeSeries.getPoints().size() > 0;
	}

	public static Double getTsPoint(final ITimeSeries<Double> timeSeries) {
		if (AbstractKiekerRTest.tsContainsPoints(timeSeries)) {
			return timeSeries.getPoints().get(0).getValue();
		} else {
			Assert.fail("The timeseries point you tried to read does not exist");
			return 0.0d;
		}
	}
}
