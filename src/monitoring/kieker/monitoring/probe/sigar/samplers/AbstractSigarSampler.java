package kieker.monitoring.probe.sigar.samplers;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.sampler.ISampler;

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
 * Eases the implementation of {@link ISampler}s which collect
 * system-level sensor data via the {@link Sigar} API and store this data as {@link IMonitoringRecord}s via
 * {@link MonitoringController#newMonitoringRecord(kieker.common.record.IMonitoringRecord)} .
 * 
 * @author Andre van Hoorn
 * 
 */
public abstract class AbstractSigarSampler implements ISampler {
	protected final SigarProxy sigar;

	/**
	 * Constructs a new {@link AbstractSigarSampler} with given {@link SigarProxy} instance used to retrieve the sensor
	 * data.
	 * 
	 * @param sigar
	 */
	public AbstractSigarSampler(final SigarProxy sigar) {
		this.sigar = sigar;
	}
}