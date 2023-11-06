/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

/**
 * Provides factory methods for
 * {@link kieker.monitoring.sampler.oshi.samplers.AbstractOshiSampler}s.
 *
 * @author Matteo Sassano
 * @since 1.14
 *
 */
public enum OshiSamplerFactory implements IOshiSamplerFactory { // Singleton pattern (Effective Java #3)
	/** The singleton instance. */
	INSTANCE;

	/**
	 * {@link HardwareAbstractionLayer} instance used to retrieve the data to be
	 * logged.
	 */
	private final HardwareAbstractionLayer hardwareAbstractionLayer;

	/**
	 * Used by {@link #getInstance()} to construct the singleton instance.
	 */
	private OshiSamplerFactory() {
		this.hardwareAbstractionLayer = new SystemInfo().getHardware();
	}

	/**
	 * {@link SystemInfo} instance used by this {@link OshiSamplerFactory}.
	 *
	 * @return the systemInfo
	 */
	public final HardwareAbstractionLayer getHardwareAbstractionLayer() {
		return this.hardwareAbstractionLayer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CPUsCombinedPercSampler createSensorCPUsCombinedPerc() {
		return new CPUsCombinedPercSampler(this.hardwareAbstractionLayer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CPUsDetailedPercSampler createSensorCPUsDetailedPerc() {
		return new CPUsDetailedPercSampler(this.hardwareAbstractionLayer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MemSwapUsageSampler createSensorMemSwapUsage() {
		return new MemSwapUsageSampler(this.hardwareAbstractionLayer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LoadAverageSampler createSensorLoadAverage() {
		return new LoadAverageSampler(this.hardwareAbstractionLayer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NetworkUtilizationSampler createSensorNetworkUtilization() {
		return new NetworkUtilizationSampler(this.hardwareAbstractionLayer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DiskUsageSampler createSensorDiskUsage() {
		return new DiskUsageSampler(this.hardwareAbstractionLayer);
	}
}
