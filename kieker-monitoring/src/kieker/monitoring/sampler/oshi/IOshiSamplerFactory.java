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

package kieker.monitoring.sampler.oshi;

import kieker.monitoring.sampler.oshi.samplers.CPUsCombinedPercSampler;
import kieker.monitoring.sampler.oshi.samplers.CPUsDetailedPercSampler;
import kieker.monitoring.sampler.oshi.samplers.DiskUsageSampler;
import kieker.monitoring.sampler.oshi.samplers.LoadAverageSampler;
import kieker.monitoring.sampler.oshi.samplers.MemSwapUsageSampler;
import kieker.monitoring.sampler.oshi.samplers.NetworkUtilizationSampler;

/**
 * Defines the list of methods to be provided by a factory for
 * {@link com.github.oshi}-based
 * {@link kieker.monitoring.sampler.oshi.samplers.AbstractOshiSampler}s.
 *
 * @author Matteo Sassano
 *
 */
public interface IOshiSamplerFactory {

	/**
	 * Creates an instance of {@link MemSwapUsageSampler}.
	 *
	 * @return the created instance.
	 */
	public MemSwapUsageSampler createSensorMemSwapUsage();

	/**
	 * Creates an instance of {@link CPUsDetailedPercSampler}.
	 *
	 * @return the created instance.
	 */
	public CPUsDetailedPercSampler createSensorCPUsDetailedPerc();

	/**
	 * Creates an instance of {@link CPUsCombinedPercSampler}.
	 *
	 * @return the created instance.
	 */
	public CPUsCombinedPercSampler createSensorCPUsCombinedPerc();

	/**
	 * Creates an instance of {@link LoadAverageSampler}.
	 *
	 * @return the created instance.
	 */
	public LoadAverageSampler createSensorLoadAverage();

	/**
	 * Creates an instance of {@link NetworkUtilizationSampler}.
	 *
	 * @return the created instance.
	 */
	public NetworkUtilizationSampler createSensorNetworkUtilization();

	/**
	 * Creates an instance of {@link DiskUsageSampler}.
	 *
	 * @return the created instance.
	 */
	public DiskUsageSampler createSensorDiskUsage();
}
