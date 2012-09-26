/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.core.controller;

/**
 * @author Jan Waller, Robert von Massow
 */
public interface IMonitoringController extends
		IJMXController,
		IProbeController,
		IRegistryController,
		ISamplingController,
		IStateController,
		ITimeSourceController,
		IWriterController {

	/**
	 * This method is used to log the status of the controllers to the console.
	 * It is included in this interface to ensure its publication over JMX.
	 * 
	 * @return a String representation of the current controller
	 */
	public String toString();
}
