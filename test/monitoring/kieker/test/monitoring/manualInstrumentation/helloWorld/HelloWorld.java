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

package kieker.test.monitoring.manualInstrumentation.helloWorld;

import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.timer.ITimeSource;

/**
 * @author Andre van Hoorn
 */
public final class HelloWorld {

	private HelloWorld() {}

	public static void main(final String[] args) {
		System.out.println("Hello");

		final IMonitoringController monitoringController = MonitoringController.getInstance();
		final ITimeSource timeSource = monitoringController.getTimeSource();

		/* recording of the start time of doSomething */
		final long startTime = timeSource.getTime();
		HelloWorld.doSomething();
		final long endTime = timeSource.getTime();
		monitoringController.newMonitoringRecord(new OperationExecutionRecord("kieker.component", "method", 1, startTime, endTime));
	}

	private static void doSomething() {
		System.out.println("doing something");
		/* .. some application logic does something meaningful .. */
	}
}
