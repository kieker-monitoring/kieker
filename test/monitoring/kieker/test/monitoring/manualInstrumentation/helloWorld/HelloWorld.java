package kieker.test.monitoring.manualInstrumentation.helloWorld;

import kieker.common.record.OperationExecutionRecord;
import kieker.monitoring.core.controller.MonitoringController;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2011 Kieker Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================================
 */
/**
 * @author Andre van Hoorn
 */
public class HelloWorld {

	public static void main(String args[]) {
		System.out.println("Hello");

		/* recording of the start time of doSomething */
		long startTime = System.nanoTime();

		doSomething();

		long endTime = System.nanoTime();
		MonitoringController.getInstance().newMonitoringRecord(
				new OperationExecutionRecord("kieker.component", "method", 1, startTime, endTime));
	}

	static void doSomething() {
		System.out.println("doing something");
		/* .. some application logic does something meaningful .. */
	}
}
