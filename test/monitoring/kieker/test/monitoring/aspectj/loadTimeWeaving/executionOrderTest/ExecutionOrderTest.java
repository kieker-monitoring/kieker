/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.monitoring.aspectj.loadTimeWeaving.executionOrderTest;

import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;

/**
 * A simple test to test the execution ordering mechanism monitoring in distributed systems.
 * 
 * @author Matthias Rohr
 *         History:
 * 
 *         2007-03-30: Initial version
 * 
 * @since < 0.9
 */
public final class ExecutionOrderTest {

	private ExecutionOrderTest() {}

	@OperationExecutionMonitoringProbe
	private static void methoda(final boolean b) {
		if (b) {
			ExecutionOrderTest.methoda2(b);
		} else {
			ExecutionOrderTest.methoda2(b);
			ExecutionOrderTest.methodb();
		}
	}

	@OperationExecutionMonitoringProbe
	private static void methoda2(final boolean b) {
		if (b) {
			ExecutionOrderTest.methodb();
		}
	}

	@OperationExecutionMonitoringProbe
	private static void methodb() {
		double d = 12 + (System.currentTimeMillis() / 1000d);
		for (int i = 0; i < 10000; i++) {
			d = d + 5 + i;
		}
	}

	public static void main(final String[] args) {
		ExecutionOrderTest.methoda(true);
		ExecutionOrderTest.methoda(false);
		ExecutionOrderTest.methodd1();
	}

	@OperationExecutionMonitoringProbe
	public static void methodd1() {
		ExecutionOrderTest.methodd2();
	}

	@OperationExecutionMonitoringProbe
	public static void methodd2() {
		ExecutionOrderTest.methodd3();
	}

	@OperationExecutionMonitoringProbe
	public static void methodd3() {
		System.out.println("d3()"); // NOPMD (System.out)
	}

}
