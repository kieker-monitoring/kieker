/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.test.monitoring.aspectJ.loadTimeWeaving.executionOrderTest;

import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;

/**
 * A simple test to test the execution ordering mechanism monitoring in distributed systems
 * 
 * @author Matthias Rohr
 *         History:
 * 
 *         2007-03-30: Initial version
 * 
 */

public class ExecutionOrderTest {
	@OperationExecutionMonitoringProbe
	private static void a(final boolean b) {
		if (b) {
			ExecutionOrderTest.a2(b);
		} else {
			ExecutionOrderTest.a2(b);
			ExecutionOrderTest.b();
		}
	}

	@OperationExecutionMonitoringProbe
	private static void a2(final boolean b) {
		if (b) {
			ExecutionOrderTest.b();
		}
	}

	@OperationExecutionMonitoringProbe
	private static void b() {
		double d = 12 + (System.currentTimeMillis() / 1000d);
		for (int i = 0; i < 10000; i++) {
			d = d + 5 + i;
		}
	}

	public static void main(final String[] args) {
		ExecutionOrderTest.a(true);
		ExecutionOrderTest.a(false);
		ExecutionOrderTest.d1();
	}

	@OperationExecutionMonitoringProbe
	public static void d1() {
		ExecutionOrderTest.d2();
	}

	@OperationExecutionMonitoringProbe
	public static void d2() {
		ExecutionOrderTest.d3();
	}

	@OperationExecutionMonitoringProbe
	public static void d3() {
		System.out.println("d3()");
	}

}
