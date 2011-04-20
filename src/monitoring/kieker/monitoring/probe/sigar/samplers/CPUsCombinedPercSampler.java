package kieker.monitoring.probe.sigar.samplers;

import kieker.common.record.ResourceUtilizationRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.WriterController;
import kieker.monitoring.timer.ITimeSource;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.SigarProxy;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2011 Kieker Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================================
 */
/**
 * Logs the combined (i.e., User + Sys + Nice + Wait) cpu utilization for each
 * CPU in the system, retrieved via {@link CpuPerc#getCombined()}, as {@link ResourceUtilizationRecord}s via
 * {@link WriterController#newMonitoringRecord(kieker.common.record.IMonitoringRecord)} .
 * 
 * @author Andre van Hoorn
 * 
 */
public class CPUsCombinedPercSampler extends AbstractSigarSampler {

	public CPUsCombinedPercSampler(final SigarProxy sigar) {
		super(sigar);
	}

	private final static String CPU_RESOURCE_NAME_PREFIX = "cpu-";

	@Override
	public void sample(final IMonitoringController samplingController) throws Exception {
		final CpuPerc[] cpus = this.sigar.getCpuPercList();
		final ITimeSource timesource = samplingController.getTimeSource();
		for (int i = 0; i < cpus.length; i++) {
			final CpuPerc curCPU = cpus[i];
			final double combinedUtilization = curCPU.getCombined();
			final ResourceUtilizationRecord r = new ResourceUtilizationRecord(timesource.getTime(),
					samplingController.getHostName(), CPUsCombinedPercSampler.CPU_RESOURCE_NAME_PREFIX + i, combinedUtilization);
			samplingController.newMonitoringRecord(r);
			// CPUsCombinedPercSampler.log.info("Sigar utilization: " + combinedUtilization + "; " + " Record: " + r);
		}
	}
}