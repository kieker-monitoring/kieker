/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.sampler.sigar.samplers;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

import kieker.common.record.system.CPUUtilizationRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.signaturePattern.SignatureFactory;
import kieker.monitoring.timer.ITimeSource;

/**
 * Logs detailed utilization statistics for each CPU in the system, retrieved
 * from {@link CpuPerc}, as {@link CPUUtilizationRecord}s via
 * {@link kieker.monitoring.core.controller.IMonitoringController#newMonitoringRecord(kieker.common.record.IMonitoringRecord)} .
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.3
 */
public final class CPUsDetailedPercSampler extends AbstractSigarSampler {

	/**
	 * Constructs a new {@link AbstractSigarSampler} with given {@link SigarProxy} instance used to retrieve the sensor data. Users
	 * should use the factory method {@link kieker.monitoring.sampler.sigar.SigarSamplerFactory#createSensorCPUsDetailedPerc()} to acquire an
	 * instance rather than calling this constructor directly.
	 * 
	 * @param sigar
	 *            The sigar proxy which will be used to retrieve the data.
	 */
	public CPUsDetailedPercSampler(final SigarProxy sigar) {
		super(sigar);
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

		final CpuPerc[] cpus = this.sigar.getCpuPercList();
		final ITimeSource timesource = monitoringController.getTimeSource();
		for (int i = 0; i < cpus.length; i++) {
			if (monitoringController.isProbeActivated(SignatureFactory.createCPUSignature(i))) {
				final CpuPerc curCPU = cpus[i];
				// final double combinedUtilization = curCPU.getCombined();
				final CPUUtilizationRecord r = new CPUUtilizationRecord(timesource.getTime(), monitoringController.getHostname(), Integer.toString(i),
						curCPU.getUser(), curCPU.getSys(), curCPU.getWait(), curCPU.getNice(), curCPU.getIrq(), curCPU.getCombined(), curCPU.getIdle());
				monitoringController.newMonitoringRecord(r);
				// CPUsDetailedPercSampler.log.info("Sigar utilization: " + combinedUtilization + "; " + " Record: " + r);
			}
		}
	}
}
