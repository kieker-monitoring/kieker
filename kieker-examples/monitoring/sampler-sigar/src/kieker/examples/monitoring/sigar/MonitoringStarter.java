/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.examples.monitoring.sigar;

import java.util.concurrent.TimeUnit;

import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.sampler.sigar.ISigarSamplerFactory;
import kieker.monitoring.sampler.sigar.SigarSamplerFactory;
import kieker.monitoring.sampler.sigar.samplers.CPUsDetailedPercSampler;
import kieker.monitoring.sampler.sigar.samplers.MemSwapUsageSampler;

public final class MonitoringStarter {

	private MonitoringStarter() {}

	public static void main(final String[] args) throws InterruptedException {
		System.out.println("Monitoring CPU and Memory/Swap for thirty seconds in five seconds steps with an offset of two seconds");

		final ISigarSamplerFactory sigarFactory = SigarSamplerFactory.INSTANCE;

		final CPUsDetailedPercSampler cpuSampler = sigarFactory.createSensorCPUsDetailedPerc();
		final MemSwapUsageSampler memSwapSampler = sigarFactory.createSensorMemSwapUsage();

		final long sleepTimeInMilliseconds = 30 * 1000;
		final long offsetInSeconds = 2;
		final long periodInSeconds = 5;

		final IMonitoringController monitoringController = MonitoringController.getInstance();
		monitoringController.schedulePeriodicSampler(cpuSampler, offsetInSeconds, periodInSeconds, TimeUnit.SECONDS);
		monitoringController.schedulePeriodicSampler(memSwapSampler, offsetInSeconds, periodInSeconds, TimeUnit.SECONDS);

		Thread.sleep(sleepTimeInMilliseconds);

		System.out.println("Terminating monitoring");
		monitoringController.terminateMonitoring();
	}

}
