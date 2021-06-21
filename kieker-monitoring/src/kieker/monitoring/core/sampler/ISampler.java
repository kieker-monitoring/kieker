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

package kieker.monitoring.core.sampler;

import kieker.monitoring.core.controller.IMonitoringController;

/**
 * @author Andre van Hoorn
 *
 * @since 1.3
 */
public interface ISampler {

	/**
	 * Triggers this {@link ISampler} to perform a measurement and to pass the data to the given {@link IMonitoringController}.
	 *
	 * @param monitoringController
	 *            The controller to which the sampler should pass the data.
	 *
	 * @throws Exception
	 *             thrown to indicate an error.
	 *
	 * @since 1.3
	 */
	void sample(final IMonitoringController monitoringController) throws Exception;
}
