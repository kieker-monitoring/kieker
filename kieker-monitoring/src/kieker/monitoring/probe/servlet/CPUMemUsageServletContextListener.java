/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.probe.servlet;

import kieker.monitoring.core.sampler.ISampler;
import kieker.monitoring.sampler.sigar.ISigarSamplerFactory;
import kieker.monitoring.sampler.sigar.SigarSamplerFactory;

/**
 * <p>
 * Starts and stops the periodic logging of CPU utilization and Memory usage employing the {@link SigarSamplerFactory} as the Servlet is initialized and destroyed
 * respectively. <br/>
 * The initial delay and the sampling period (both given in seconds) can be configured via context-params in the web.xml file, as shown below.
 * </p>
 * 
 * <p>
 * The integration and configuration in a web.xml file works as follows:<br/>
 * 
 * <pre>
 *  {@code
 *  <web-app>
 *  ...
 *  
 *  <context-param>
 *   <param-name>CPUMemUsageServletContextListener.samplingIntervalSeconds</param-name>
 *   <param-value>15</param-value>
 *  </context-param>
 *  
 *  <context-param>
 *   <param-name>CPUMemUsageServletContextListener.initialSamplingDelaySeconds</param-name>
 *   <param-value>0</param-value>
 *  </context-param>
 *  
 *  <listener>
 *    <listener-class>
 *     kieker.monitoring.probe.servlet.CPUMemUsageServletContextListener
 *    </listener-class>
 *  </listener>
 * 
 * ... 
 * </web-app>}
 * }
 * </pre>
 * 
 * </p>
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.3
 */
public class CPUMemUsageServletContextListener extends AbstractRegularSamplingServletContextListener {

	/** Prefix for parameters used in the web.xml file. */
	// NOTE that this declaration must be BEFORE the following public constants!
	private static final String CONTEXT_PARAM_NAME_PREFIX = CPUMemUsageServletContextListener.class.getSimpleName();

	/** Parameter name for the sampling interval to be used in the web.xml file. */
	public static final String CONTEXT_PARAM_NAME_SAMPLING_INTERVAL_SECONDS = CONTEXT_PARAM_NAME_PREFIX // NOCS (decl. order)
			+ ".samplingIntervalSeconds";
	/** Parameter name for the initial delay to be used in the web.xml file. */
	public static final String CONTEXT_PARAM_NAME_INITIAL_SAMPLING_DELAY_SECONDS = CONTEXT_PARAM_NAME_PREFIX // NOCS (decl. order)
			+ ".initialSamplingDelaySeconds";

	/**
	 * Empty constructor.
	 */
	public CPUMemUsageServletContextListener() {
		// nothing to do
	}

	@Override
	protected String getContextParameterNameSamplingIntervalSeconds() {
		return CONTEXT_PARAM_NAME_SAMPLING_INTERVAL_SECONDS;
	}

	@Override
	protected String getContextParameterNameSamplingDelaySeconds() {
		return CONTEXT_PARAM_NAME_INITIAL_SAMPLING_DELAY_SECONDS;
	}

	@Override
	protected ISampler[] createSamplers() {
		final ISigarSamplerFactory sigarFactory = SigarSamplerFactory.INSTANCE;
		return new ISampler[] { sigarFactory.createSensorCPUsDetailedPerc(), sigarFactory.createSensorMemSwapUsage() };
	}

}
