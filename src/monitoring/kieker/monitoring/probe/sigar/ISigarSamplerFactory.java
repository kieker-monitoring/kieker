/***************************************************************************
 * Copyright 2012 by
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

/**
 * Defines the list of methods to be provided by a factory for {@link org.hyperic.sigar.Sigar}-based
 * {@link kieker.monitoring.probe.sigar.samplers.AbstractSigarSampler}s.
 * 
 * @author Andre van Hoorn
 * 
 */
public interface ISigarSamplerFactory {

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
}
