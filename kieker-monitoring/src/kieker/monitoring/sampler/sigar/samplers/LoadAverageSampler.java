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

package kieker.monitoring.sampler.sigar.samplers;

import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

import kieker.common.record.system.LoadAverageRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.signaturePattern.SignatureFactory;
import kieker.monitoring.timer.ITimeSource;

/**
 * Logs load average of the system, retrieved as {@link LoadAverageRecord} via
 * {@link kieker.monitoring.core.controller.IMonitoringController#newMonitoringRecord(kieker.common.record.IMonitoringRecord)} .
 *
 * @author Teerat Pitakrat
 *
 * @since 1.12
 */
public final class LoadAverageSampler extends AbstractSigarSampler {

	/**
	 * Constructs a new {@link AbstractSigarSampler} with given {@link SigarProxy} instance used to retrieve the sensor data. Users
	 * should use the factory method {@link kieker.monitoring.sampler.sigar.SigarSamplerFactory#createSensorLoadAverage()} to acquire an
	 * instance rather than calling this constructor directly.
	 *
	 * @param sigar
	 *            The sigar proxy which will be used to retrieve the data.
	 */
	public LoadAverageSampler(final SigarProxy sigar) {
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
		if (!monitoringController.isProbeActivated(SignatureFactory.createLoadAverageSignature())) {
			return;
		}

		final double[] loadAverage = this.sigar.getLoadAverage();
		if (loadAverage.length != 3) {
			return;
		}
		final ITimeSource timesource = monitoringController.getTimeSource();
		final LoadAverageRecord r = new LoadAverageRecord(timesource.getTime(), monitoringController.getHostname(), loadAverage[0], loadAverage[1],
				loadAverage[2]);
		monitoringController.newMonitoringRecord(r);
	}
}
