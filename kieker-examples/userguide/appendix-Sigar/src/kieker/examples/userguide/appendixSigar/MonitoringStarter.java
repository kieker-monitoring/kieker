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

package kieker.examples.userguide.appendixSigar;

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
		System.out.println("Monitoring CPU and Mem/Swap for 30 seconds in 5 seconds steps with an offset of two seconds");

		final IMonitoringController monitoringController =
				MonitoringController.getInstance();

		final ISigarSamplerFactory sigarFactory = SigarSamplerFactory.INSTANCE;

		final CPUsDetailedPercSampler cpuSampler =
				sigarFactory.createSensorCPUsDetailedPerc();
		final MemSwapUsageSampler memSwapSampler =
				sigarFactory.createSensorMemSwapUsage();

		final long offset = 2; // start after 2 seconds
		final long period = 5; // monitor every 5 seconds

		monitoringController.schedulePeriodicSampler(
				cpuSampler, offset, period, TimeUnit.SECONDS);
		monitoringController.schedulePeriodicSampler(
				memSwapSampler, offset, period, TimeUnit.SECONDS);

		Thread.sleep(30000);

		System.out.println("Terminating");
		monitoringController.terminateMonitoring();
	}
}
