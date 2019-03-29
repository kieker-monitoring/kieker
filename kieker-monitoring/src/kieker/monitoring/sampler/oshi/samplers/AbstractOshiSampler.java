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

import kieker.monitoring.core.sampler.ISampler;

import oshi.hardware.HardwareAbstractionLayer;

/**
 * Eases the implementation of {@link ISampler}s which collect system-level
 * sensor data via the {@link com.github.oshi} API and store this data as
 * {@link kieker.common.record.IMonitoringRecord}s via
 * {@link kieker.monitoring.core.controller.WriterController#newMonitoringRecord(kieker.common.record.IMonitoringRecord)}
 * .
 *
 * @author Matteo Sassano
 * @since 1.14
 *
 */
public abstract class AbstractOshiSampler implements ISampler {

	/**
	 * The {@link HardwareAbstractionLayer} reference which will be used to retrieve
	 * the sensor data.
	 */
	protected final HardwareAbstractionLayer hardwareAbstractionLayer;

	/**
	 * Constructs a new {@link AbstractOshiSampler} with given
	 * {@link HardwareAbstractionLayer} instance used to retrieve the sensor data.
	 *
	 * @param hardwareAbstractionLayer
	 *            The {@link HardwareAbstractionLayer} reference which will be used
	 *            to retrieve the data.
	 */
	public AbstractOshiSampler(final HardwareAbstractionLayer hardwareAbstractionLayer) {
		this.hardwareAbstractionLayer = hardwareAbstractionLayer;
	}
}
