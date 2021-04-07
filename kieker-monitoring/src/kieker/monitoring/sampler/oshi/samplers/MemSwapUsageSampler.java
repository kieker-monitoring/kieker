/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.sampler.oshi.samplers;

import kieker.common.record.system.MemSwapUsageRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.signaturePattern.SignatureFactory;

import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

/**
 * Logs memory and swap statistics retrieved from {@link GlobalMemory}, as
 * {@link MemSwapUsageRecord}s via
 * {@link kieker.monitoring.core.controller.IMonitoringController#newMonitoringRecord(kieker.common.record.IMonitoringRecord)}
 * .
 *
 * @author Matteo Sassano
 *
 * @since 1.3
 */
public class MemSwapUsageSampler extends AbstractOshiSampler {

	/**
	 * Constructs a new {@link AbstractOshiSampler} with given
	 * {@link HardwareAbstractionLayer} instance used to retrieve the sensor data.
	 * Users should use the factory method
	 * {@link kieker.monitoring.sampler.oshi.OshiSamplerFactory#createSensorMemSwapUsage()}
	 * to acquire an instance rather than calling this constructor directly.
	 *
	 * @param hardwareAbstractionLayer
	 *            The {@link HardwareAbstractionLayer} which will be used to
	 *            retrieve the data.
	 */
	public MemSwapUsageSampler(final HardwareAbstractionLayer hardwareAbstractionLayer) {
		super(hardwareAbstractionLayer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sample(final IMonitoringController monitoringCtr) {
		if (!monitoringCtr.isMonitoringEnabled() || !monitoringCtr.isProbeActivated(SignatureFactory.createMemSwapSignature())) {
			return;
		}
		final GlobalMemory globalMemory = this.hardwareAbstractionLayer.getMemory();

		final long memoryTotal = globalMemory.getTotal();
		final long memoryAvailable = globalMemory.getAvailable();
		final long memoryUsed = memoryTotal - memoryAvailable;
		final long swapTotal = globalMemory.getSwapTotal();
		final long swapUsed = globalMemory.getSwapUsed();
		final long swapFree = swapTotal - swapUsed;

		final MemSwapUsageRecord r = new MemSwapUsageRecord(monitoringCtr.getTimeSource().getTime(),
				monitoringCtr.getHostname(), memoryTotal, memoryUsed, memoryAvailable, swapTotal, swapUsed, swapFree);
		monitoringCtr.newMonitoringRecord(r);
	}
}
