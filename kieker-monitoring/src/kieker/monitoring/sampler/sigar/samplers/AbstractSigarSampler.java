/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

import org.hyperic.sigar.SigarProxy;

import kieker.monitoring.core.sampler.ISampler;

/**
 * Eases the implementation of {@link ISampler}s which collect system-level sensor data via the {@link org.hyperic.sigar.Sigar} API and store this data as
 * {@link kieker.common.record.IMonitoringRecord}s via
 * {@link kieker.monitoring.core.controller.WriterController#newMonitoringRecord(kieker.common.record.IMonitoringRecord)} .
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.3
 */
public abstract class AbstractSigarSampler implements ISampler {
	/** The sigar proxy which will be used to retrieve the sensor data. */
	protected final SigarProxy sigar;

	/**
	 * Constructs a new {@link AbstractSigarSampler} with given {@link SigarProxy} instance used to retrieve the sensor data.
	 * 
	 * @param sigar
	 *            The sigar proxy which will be used to retrieve the data.
	 */
	public AbstractSigarSampler(final SigarProxy sigar) {
		this.sigar = sigar;
	}
}
