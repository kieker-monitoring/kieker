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

package kieker.monitoring.probe.sigar.samplers;

import java.util.List;
import java.util.ListIterator;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

import kieker.common.record.system.CPUUtilizationRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.probe.AbstractProbeInfo;
import kieker.monitoring.probe.IAdaptiveProbe;
import kieker.monitoring.timer.ITimeSource;

/**
 * Logs detailed utilization statistics for each CPU in the system, retrieved
 * from {@link CpuPerc}, as {@link CPUUtilizationRecord}s via
 * {@link kieker.monitoring.core.controller.IMonitoringController#newMonitoringRecord(kieker.common.record.IMonitoringRecord)} .
 * 
 * @author Andre van Hoorn
 * 
 */
public final class CPUsDetailedPercSampler extends AbstractSigarSampler implements IAdaptiveProbe {

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

	public void sample(final IMonitoringController monitoringController) throws SigarException {
		if (!monitoringController.isMonitoringEnabled()) {
			return;
		}

		final CpuPerc[] cpus = this.sigar.getCpuPercList();
		final ITimeSource timesource = monitoringController.getTimeSource();
		for (int i = 0; i < cpus.length; i++) {
			final CPUInfo cpuInfo = new CPUInfo("CPUsDetailedPercSampler", monitoringController.getHostname(), i);
			if (monitoringController.isProbeActivated(cpuInfo, this)) {
				final CpuPerc curCPU = cpus[i];
				// final double combinedUtilization = curCPU.getCombined();
				final CPUUtilizationRecord r = new CPUUtilizationRecord(timesource.getTime(), monitoringController.getHostname(), Integer.toString(i),
						curCPU.getUser(),
						curCPU.getSys(), curCPU.getWait(), curCPU.getNice(), curCPU.getIrq(), curCPU.getCombined(), curCPU.getIdle());
				monitoringController.newMonitoringRecord(r);
				// CPUsDetailedPercSampler.log.info("Sigar utilization: " +
				// combinedUtilization + "; " + " Record: " + r);
			}
		}
	}

	public boolean isProbeActivated(final List<String> probePatterns, final AbstractProbeInfo abstractProbeInfo) {
		final ListIterator<String> iterator = probePatterns.listIterator(probePatterns.size());
		final String cpuSignature = ((CPUInfo) abstractProbeInfo).getCpuSignature();
		while (iterator.hasPrevious()) {
			final String patternWithPrefix = iterator.previous();
			final char prefix = patternWithPrefix.charAt(0);
			final String pattern = patternWithPrefix.substring(1);
			if (pattern.equals(cpuSignature)) {
				switch (prefix) {
				case '+':
					return true;
				case '-':
					return false;
				}
			}
		}
		return true;
	}
}

class CPUInfo extends AbstractProbeInfo {
	private final String hostname;
	private final int cpuId;
	private final String cpuSignature;

	/**
	 * @param hostname
	 * @param cpuId
	 */
	public CPUInfo(final String probeId, final String hostname, final int cpuId) {
		super(probeId);
		this.hostname = hostname;
		this.cpuId = cpuId;
		this.cpuSignature = hostname + "-" + cpuId;
	}

	/**
	 * @return the hostname
	 */
	public String getHostname() {
		return this.hostname;
	}

	/**
	 * @return the cpuId
	 */
	public int getCpuId() {
		return this.cpuId;
	}

	/**
	 * @return the cpuSignature
	 */
	public String getCpuSignature() {
		return this.cpuSignature;
	}
}
