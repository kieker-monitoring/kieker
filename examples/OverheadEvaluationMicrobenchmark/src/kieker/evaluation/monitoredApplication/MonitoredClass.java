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

package kieker.evaluation.monitoredApplication;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;

/**
 * @author Jan Waller
 */
public final class MonitoredClass {
	private final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

	public MonitoredClass() {
		// empty default constructor
	}

	@OperationExecutionMonitoringProbe
	public final long monitoredMethod(final long methodTime, final int recDepth) {
		if (recDepth > 1) {
			return this.monitoredMethod(methodTime, recDepth - 1);
		} else {
			final long exitTime = this.threadMXBean.getCurrentThreadUserTime() + methodTime;
			long currentTime = this.threadMXBean.getCurrentThreadUserTime();
			while (currentTime < exitTime) {
				currentTime = this.threadMXBean.getCurrentThreadUserTime();
			}
			return currentTime;
		}
	}
}
