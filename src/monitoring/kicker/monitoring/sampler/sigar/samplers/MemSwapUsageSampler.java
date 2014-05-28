/***************************************************************************
 * Copyright 2014 Kicker Project (http://kicker-monitoring.net)
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

package kicker.monitoring.sampler.sigar.samplers;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.Swap;

import kicker.common.record.system.MemSwapUsageRecord;
import kicker.monitoring.core.controller.IMonitoringController;
import kicker.monitoring.core.signaturePattern.SignatureFactory;

/**
 * Logs memory and swap statistics retrieved from {@link Mem} and {@link Swap},
 * as {@link MemSwapUsageRecord}s via {@link kicker.monitoring.core.controller.IMonitoringController#newMonitoringRecord(kicker.common.record.IMonitoringRecord)} .
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.3
 */
public class MemSwapUsageSampler extends AbstractSigarSampler {

	/**
	 * Constructs a new {@link AbstractSigarSampler} with given {@link SigarProxy} instance used to retrieve the sensor data. Users
	 * should use the factory method {@link kicker.monitoring.sampler.sigar.SigarSamplerFactory#createSensorMemSwapUsage()} to acquire an
	 * instance rather than calling this constructor directly.
	 * 
	 * @param sigar
	 *            The sigar proxy which will be used to retrieve the data.
	 */
	public MemSwapUsageSampler(final SigarProxy sigar) {
		super(sigar);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sample(final IMonitoringController monitoringCtr) throws SigarException {
		if (!monitoringCtr.isMonitoringEnabled()) {
			return;
		}
		if (!monitoringCtr.isProbeActivated(SignatureFactory.createMemSwapSignature())) {
			return;
		}

		final Mem mem = this.sigar.getMem();
		final Swap swap = this.sigar.getSwap();
		final MemSwapUsageRecord r = new MemSwapUsageRecord(
				monitoringCtr.getTimeSource().getTime(), monitoringCtr.getHostname(),
				mem.getTotal(), mem.getActualUsed(), mem.getActualFree(),
				swap.getTotal(), swap.getUsed(), swap.getFree());
		monitoringCtr.newMonitoringRecord(r);
	}
}
