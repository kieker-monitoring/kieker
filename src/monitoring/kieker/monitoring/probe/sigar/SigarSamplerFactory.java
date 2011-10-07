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

package kieker.monitoring.probe.sigar;

import kieker.monitoring.probe.sigar.samplers.CPUsCombinedPercSampler;
import kieker.monitoring.probe.sigar.samplers.CPUsDetailedPercSampler;
import kieker.monitoring.probe.sigar.samplers.MemSwapUsageSampler;

import org.hyperic.sigar.Humidor;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarProxy;

/**
 * Provides factory methods for {@link kieker.monitoring.probe.sigar.samplers.AbstractSigarSampler}s.
 * 
 * @author Andre van Hoorn
 */
public final class SigarSamplerFactory implements ISigarSamplerFactory {
	// private static final Log log = LogFactory.getLog(SigarSamplerFactory.class);

	/**
	 * Returns the singleton instance of the {@link SigarSamplerFactory}.
	 */
	public final static SigarSamplerFactory getInstance() {
		return LazyHolder.INSTANCE;
	}

	/**
	 * {@link SigarProxy} instance used to retrieve the data to be logged.
	 */
	private final SigarProxy sigar;

	/**
	 * {@link SigarProxy} instance used by this {@link SigarSamplerFactory}.
	 * 
	 * @return the sigar
	 */
	public final SigarProxy getSigar() {
		return this.sigar;
	}

	/**
	 * Used by {@link #getInstance()} to construct the singleton instance.
	 */
	private SigarSamplerFactory() {
		this(new Humidor(new Sigar()));
	}

	/**
	 * Constructs a {@link SigarSamplerFactory} with the given parameters.
	 * 
	 * @param humidor
	 */
	public SigarSamplerFactory(final Humidor humidor) {
		this.sigar = humidor.getSigar();
	}

	@Override
	public CPUsCombinedPercSampler createSensorCPUsCombinedPerc() {
		return new CPUsCombinedPercSampler(this.sigar);
	}

	@Override
	public CPUsDetailedPercSampler createSensorCPUsDetailedPerc() {
		return new CPUsDetailedPercSampler(this.sigar);
	}

	@Override
	public MemSwapUsageSampler createSensorMemSwapUsage() {
		return new MemSwapUsageSampler(this.sigar);
	}

	/**
	 * SINGLETON
	 */
	private final static class LazyHolder { // NOCS (MissingCtorCheck)
		private static final SigarSamplerFactory INSTANCE = new SigarSamplerFactory();
	}
}
