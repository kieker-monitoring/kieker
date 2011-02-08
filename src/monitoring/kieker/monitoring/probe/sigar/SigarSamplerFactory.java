package kieker.monitoring.probe.sigar;

import kieker.monitoring.probe.sigar.samplers.AbstractSigarSampler;
import kieker.monitoring.probe.sigar.samplers.CPUsCombinedPercSampler;
import kieker.monitoring.probe.sigar.samplers.CPUsDetailedPercSampler;
import kieker.monitoring.probe.sigar.samplers.MemSwapUsageSampler;

import org.hyperic.sigar.Humidor;
import org.hyperic.sigar.Sigar;
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
 * Provides factory methods for {@link AbstractSigarSampler}s.
 * 
 * @author Andre van Hoorn
 */
public final class SigarSamplerFactory implements ISigarSamplerFactory {
	//private static final Log log = LogFactory.getLog(SigarSamplerFactory.class);

	/**
	 * Returns the singleton instance of the {@link SigarSamplerFactory} which
	 * uses the singleton instance of the {@link MonitoringController retrieved via
	 * {@link MonitoringController#getInstance()}.
	 * 
	 * The size of the internal thread pool used to serve the sensing and
	 * logging jobs is set to {@link SigarSamplerFactory#DEFAULT_EXECUTOR_THREAD_POOL_SIZE}.
	 */
	public final static SigarSamplerFactory getInstance() {
		return LazyHolder.INSTANCE;
	}

	/**
	 * {@link SigarProxy} instance used to retrieve the data to be logged.
	 */
	private final SigarProxy sigar;

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
	private final static class LazyHolder {
		private static final SigarSamplerFactory INSTANCE = new SigarSamplerFactory();
	}
}