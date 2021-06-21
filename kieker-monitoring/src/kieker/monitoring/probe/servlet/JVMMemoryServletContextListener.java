/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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
import kieker.monitoring.sampler.mxbean.MemorySampler;

/**
 * <p>
 * Starts and stops the periodic logging of the JIT compilation. <br/>
 * The initial delay and the sampling period (both given in seconds) can be configured via context-params in the web.xml file, as shown below.
 * </p>
 *
 * <p>
 * The integration and configuration in a web.xml file works as follows:<br/>
 *
 * <pre>
 * {@code
 *  <web-app>
 *  ...
 *
 * <context-param>
 *  <param-name>JVMMemoryServletContextListener.samplingIntervalSeconds</param-name>
 *  <param-value>15</param-value>
 * </context-param>
 *
 *  <context-param>
 *   <param-name>JVMMemoryServletContextListener.initialSamplingDelaySeconds</param-name>
 *  <param-value>0</param-value>
 * </context-param>
 *
 * <listener>
 *   <listener-class>
 *     kieker.monitoring.probe.servlet.JVMMemoryServletContextListener
 *   </listener-class>
 * </listener>
 *
 * ...
 * </web-app>}
 * }
 * </pre>
 *
 * </p>
 *
 * @author Nils Christian Ehmke
 *
 * @since 1.10
 */
public class JVMMemoryServletContextListener extends AbstractRegularSamplingServletContextListener {

	/** Prefix for parameters used in the web.xml file. */
	// NOTE that this declaration must be BEFORE the following public constants!
	private static final String CONTEXT_PARAM_NAME_PREFIX = JVMMemoryServletContextListener.class.getSimpleName();

	/** Parameter name for the sampling interval to be used in the web.xml file. */
	public static final String CONTEXT_PARAM_NAME_SAMPLING_INTERVAL_SECONDS = CONTEXT_PARAM_NAME_PREFIX // NOCS (decl. order)
			+ ".samplingIntervalSeconds";
	/** Parameter name for the initial delay to be used in the web.xml file. */
	public static final String CONTEXT_PARAM_NAME_INITIAL_SAMPLING_DELAY_SECONDS = CONTEXT_PARAM_NAME_PREFIX // NOCS (decl. order)
			+ ".initialSamplingDelaySeconds";

	/**
	 * Empty constructor.
	 */
	public JVMMemoryServletContextListener() {
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
		return new ISampler[] { new MemorySampler() };
	}

}
