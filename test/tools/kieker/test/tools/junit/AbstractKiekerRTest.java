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

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import kieker.tools.tslib.ITimeSeries;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * Test that ensures that every time a R-dependent test is run, a fresh
 * Rserve instance is started beforehand and terminated afterwards.
 *
 * @since 1.10
 * @author Thomas Duellmann
 *
 */
public abstract class AbstractKiekerRTest extends AbstractKiekerTest {

	private static final String PROPERTY_NAME_KIEKER_R_TEST = "TestKiekerRTests";

	/**
	 * Check whether the SystemProperty is set that states, that Kieker R-related tests
	 * should be executed and Check whether a connection to the Rserve server can be established.
	 */
	@Before
	public void preCheckForRSysPropertyAndConnection() {

		final String messageWhenPropertyNotSet =
				"Skipping " + this.getClass() + " because system property " + PROPERTY_NAME_KIEKER_R_TEST + " not true";
		Assume.assumeTrue(messageWhenPropertyNotSet, this.isTestKiekerRTestsSet());

		try {
			final RConnection rConnection = new RConnection();
			Assume.assumeTrue(rConnection.isConnected());
			rConnection.close();
		} catch (final RserveException e) { // thrown on new RConnection();
			if (this.isTestKiekerRTestsSet()) {
				Assert.fail("You chose to execute KiekerRTests, but no connection to Rserve can be established.");
			}
		}
	}

	/**
	 * Checks whether the given TimeSeries contains items.
	 *
	 * @param timeSeries
	 *            TimeSeries
	 * @return true if TimeSeries contains one or more items, else false
	 */
	public static boolean tsContainsPoints(final ITimeSeries<Double> timeSeries) {
		return timeSeries.getPoints().size() > 0;
	}

	/**
	 * Gets the first (index = 0) TimeSeries point from the given TimeSeries.
	 *
	 * @param timeSeries
	 *            TimeSeries
	 * @return First (index = 0) TimeSeries item if there are any, else 0d (and Test fails)
	 */
	public static Double getTsPoint(final ITimeSeries<Double> timeSeries) {
		if (AbstractKiekerRTest.tsContainsPoints(timeSeries)) {
			return timeSeries.getPoints().get(0).getValue();
		} else {
			Assert.fail("The timeseries point you tried to read does not exist");
			return 0.0d;
		}
	}

	/**
	 * Checks whether the system property {@value #PROPERTY_NAME_KIEKER_R_TEST} is set to true.
	 *
	 * @return true if set correctly (to true), else false
	 */
	private boolean isTestKiekerRTestsSet() {
		return "true".equals(System.getProperty(PROPERTY_NAME_KIEKER_R_TEST));
	}
}
