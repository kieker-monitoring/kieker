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

package kieker.test.tools.junit.rbridge;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.tools.util.InvalidREvaluationResultException;
import kieker.tools.util.RBridgeControl;

import kieker.test.tools.junit.opad.util.AbstractKiekerRTest;

/**
 *
 * @author Tillmann Carlos Bielefeld
 * @since 1.10
 *
 */
public class RBridgeTest extends AbstractKiekerRTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(RBridgeTest.class);

	/**
	 * Creates a new instance of this class.
	 */
	public RBridgeTest() {
		// Default constructor
	}

	/**
	 * Test of the RBridge, connects to Rserve.
	 *
	 * @throws Exception
	 *             If exception is thrown
	 */
	@Test
	public void test() throws Exception {
		final RBridgeControl r = RBridgeControl.getInstance();

		r.evalWithR("measures <- c(NA,NA,NA,NA,NA,NA,NA,NA,NA,NA,31.0,41.0,95.0,77.0,29.0,62.0,49.0,NA)");
		r.evalWithR("forecasts <- c(NA,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,2.8181818181818183,"
				+ "6.0,12.846153846153847,17.428571428571427,18.2,20.9375,22.58823529411765)");
		r.evalWithR("anomalies <- c(NA,NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN,0.8333333333333333,0.7446808510638298,"
				+ "0.761768901569187,0.6308623298033282,0.2288135593220339,0.49510173323285606,0.3689400164338537,NA)");
		final Object result = r.evalWithR("combined <- cbind(measures, forecasts, anomalies)");

		if (RBridgeTest.LOGGER.isInfoEnabled()) {
			RBridgeTest.LOGGER.info(result.toString());
		}

		Assert.assertTrue(result != null);
		Assert.assertTrue(result instanceof org.rosuda.REngine.REXPDouble);
	}

	/**
	 * Test to make sure that InvalidREvaluationResultException within the evalWithR() method are caught correctly.
	 *
	 * @throws InvalidREvaluationResultException
	 *             if exceptions are handled correctly.
	 */
	@Test(expected = InvalidREvaluationResultException.class)
	public void testNullPointerEvaluationEvalWithR() {
		final RBridgeControl r = RBridgeControl.getInstance();
		r.evalWithR("accuracy(NULL)[6]");
	}
}
