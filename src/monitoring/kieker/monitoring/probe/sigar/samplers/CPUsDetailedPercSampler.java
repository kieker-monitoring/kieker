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

package kieker.monitoring.probe.sigar.samplers;

import kieker.common.record.system.CPUUtilizationRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.timer.ITimeSource;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

/**
 * Logs detailed utilization statistics for each CPU in the system, retrieved
 * from {@link CpuPerc}, as {@link CPUUtilizationRecord}s via
 * {@link kieker.monitoring.core.controller.IMonitoringController#newMonitoringRecord(kieker.common.record.IMonitoringRecord)} .
 * 
 * @author Andre van Hoorn
 * 
 */
public final class CPUsDetailedPercSampler extends AbstractSigarSampler {

	/**
	 * Constructs a new {@link AbstractSigarSampler} with given {@link SigarProxy} instance used to retrieve the sensor data. Users
	 * should use the factory method {@link kieker.monitoring.probe.sigar.SigarSamplerFactory#createSensorCPUsDetailedPerc()} to acquire an
	 * instance rather than calling this constructor directly.
	 * 
	 * @param sigar
	 */
	public CPUsDetailedPercSampler(final SigarProxy sigar) {
		super(sigar);
	}

	@Override
	public void sample(final IMonitoringController samplingController) throws SigarException {
		final CpuPerc[] cpus = this.sigar.getCpuPercList();
		final ITimeSource timesource = samplingController.getTimeSource();
		for (int i = 0; i < cpus.length; i++) {
			final CpuPerc curCPU = cpus[i];
			// final double combinedUtilization = curCPU.getCombined();
			final CPUUtilizationRecord r = new CPUUtilizationRecord(timesource.getTime(), samplingController.getHostName(), Integer.toString(i), curCPU.getUser(), // NOPMD
					curCPU.getSys(), curCPU.getWait(), curCPU.getNice(), curCPU.getIrq(), curCPU.getCombined(), curCPU.getIdle());
			samplingController.newMonitoringRecord(r);
			// CPUsDetailedPercSampler.log.info("Sigar utilization: " +
			// combinedUtilization + "; " + " Record: " + r);
		}
	}
}
