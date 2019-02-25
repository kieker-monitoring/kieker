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

package kieker.monitoring.sampler.oshi.samplers;

import org.hyperic.sigar.SigarException;

import kieker.common.record.system.CPUUtilizationRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.signaturePattern.SignatureFactory;
import kieker.monitoring.timer.ITimeSource;

import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.HardwareAbstractionLayer;

/**
 * Logs detailed utilization statistics for each CPU in the system, retrieved
 * from {@link CentralProcessor}, as {@link CPUUtilizationRecord}s via
 * {@link kieker.monitoring.core.controller.IMonitoringController#newMonitoringRecord(kieker.common.record.IMonitoringRecord)}
 * .
 *
 * @author Matteo Sassano
 *
 */
public final class CPUsDetailedPercSampler extends AbstractOshiSampler {

	/**
	 * Constructs a new {@link AbstractOshiSampler} with given
	 * {@link HardwareAbstractionLayer} instance used to retrieve the sensor data.
	 * Users should use the factory method
	 * {@link kieker.monitoring.sampler.sigar.SigarSamplerFactory#createSensorCPUsDetailedPerc()}
	 * to acquire an instance rather than calling this constructor directly.
	 *
	 * @param hardwareAbstractionLayer
	 *            The {@link HardwareAbstractionLayer} which will be used to
	 *            retrieve the data.
	 */
	public CPUsDetailedPercSampler(final HardwareAbstractionLayer hardwareAbstractionLayer) {
		super(hardwareAbstractionLayer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sample(final IMonitoringController monitoringController) throws SigarException {
		if (!monitoringController.isMonitoringEnabled()) {
			return;
		}
		if (!monitoringController.isProbeActivated(SignatureFactory.createCPUSignature())) {
			return;
		}

		final CentralProcessor centralProcessor = this.hardwareAbstractionLayer.getProcessor();
		final long[][] processorLoadTicks = centralProcessor.getProcessorCpuLoadTicks();

		final ITimeSource timesource = monitoringController.getTimeSource();
		for (int i = 0; i < processorLoadTicks.length; i++) {
			if (monitoringController.isProbeActivated(SignatureFactory.createCPUSignature(i))) {
				final long[] plt = processorLoadTicks[i];
				final long system = plt[TickType.SYSTEM.getIndex()];
				final long wait = plt[TickType.IOWAIT.getIndex()];
				final long nice = plt[TickType.NICE.getIndex()];
				final long idle = plt[TickType.IDLE.getIndex()];
				final long user = plt[TickType.USER.getIndex()];
				final long irq = plt[TickType.IRQ.getIndex()];
				final double combined = centralProcessor.getProcessorCpuLoadBetweenTicks()[i];

				final CPUUtilizationRecord r = new CPUUtilizationRecord(timesource.getTime(),
						monitoringController.getHostname(), Integer.toString(i), user, system, wait, nice, irq,
						combined, idle);
				monitoringController.newMonitoringRecord(r);
			}
		}
	}
}
