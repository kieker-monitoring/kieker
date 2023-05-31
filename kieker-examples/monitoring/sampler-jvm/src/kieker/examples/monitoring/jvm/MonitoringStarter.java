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

package kieker.examples.monitoring.jvm;

import java.util.concurrent.TimeUnit;

import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.sampler.mxbean.GCSampler;
import kieker.monitoring.sampler.mxbean.MemorySampler;

public final class MonitoringStarter {

	private MonitoringStarter() {}

	public static void main(final String[] args) throws InterruptedException {
		System.out.println("Monitoring JVM memory and garbage collectors for thirty seconds in five seconds steps with an offset of two seconds");

		final MemorySampler memorySampler = new MemorySampler();
		final GCSampler gcSampler = new GCSampler();

		final long sleepTimeInMilliseconds = 30 * 1000;
		final long offsetInSeconds = 2;
		final long periodInSeconds = 5;

		final IMonitoringController monitoringController = MonitoringController.getInstance();
		monitoringController.schedulePeriodicSampler(memorySampler, offsetInSeconds, periodInSeconds, TimeUnit.SECONDS);
		monitoringController.schedulePeriodicSampler(gcSampler, offsetInSeconds, periodInSeconds, TimeUnit.SECONDS);

		Thread.sleep(sleepTimeInMilliseconds);

		System.out.println("Terminating monitoring");
		monitoringController.terminateMonitoring();
	}

}
