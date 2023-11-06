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

package kieker.monitoring.sampler.oshi.samplers;

import kieker.common.record.system.ResourceUtilizationRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.signaturePattern.SignatureFactory;
import kieker.monitoring.timer.ITimeSource;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

/**
 * Logs the combined (i.e., User + Sys + Nice + Wait) cpu utilization for each
 * CPU in the system, retrieved via {@link HardwareAbstractionLayer}, as
 * {@link ResourceUtilizationRecord}s via
 * {@link kieker.monitoring.core.controller.IMonitoringController#newMonitoringRecord(kieker.common.record.IMonitoringRecord)}
 * .
 *
 * @author Matteo Sassano
 * @since 1.14
 *
 */
public class CPUsCombinedPercSampler extends AbstractOshiSampler {

	private static final String CPU_RESOURCE_NAME_PREFIX = "cpu-";

	/**
	 * Constructs a new {@link AbstractOshiSampler} with given {@link SystemInfo}
	 * instance used to retrieve the sensor data. Users should use the factory
	 * method
	 * {@link kieker.monitoring.sampler.oshi.OshiSamplerFactory#createSensorCPUsCombinedPerc()}
	 * to acquire an instance rather than calling this constructor directly.
	 *
	 * @param hardwareAbstractionLayer
	 *            The hardwareAbstractionLayer which will be used to retrieve the
	 *            data.
	 */
	public CPUsCombinedPercSampler(final HardwareAbstractionLayer hardwareAbstractionLayer) {
		super(hardwareAbstractionLayer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sample(final IMonitoringController monitoringController) {
		if (!monitoringController.isMonitoringEnabled() || !monitoringController.isProbeActivated(SignatureFactory.createCPUSignature())) {
			return;
		}
		final CentralProcessor centralProcessor = this.hardwareAbstractionLayer.getProcessor();
		final double[] cpuLoads = centralProcessor.getProcessorCpuLoadBetweenTicks();
		final ITimeSource timesource = monitoringController.getTimeSource();

		for (int i = 0; i < cpuLoads.length; i++) {
			if (monitoringController.isProbeActivated(SignatureFactory.createCPUSignature(i))) {

				final double combinedUtilization = cpuLoads[i];
				final ResourceUtilizationRecord r = new ResourceUtilizationRecord(timesource.getTime(),
						monitoringController.getHostname(), CPU_RESOURCE_NAME_PREFIX + i, combinedUtilization);
				monitoringController.newMonitoringRecord(r);
			}
		}
	}
}
