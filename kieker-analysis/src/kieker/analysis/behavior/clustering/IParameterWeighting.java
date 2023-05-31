/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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

package kieker.analysis.behavior.clustering;

/**
 * An interface to assign insertion and duplication costs to events.
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public interface IParameterWeighting {

	/**
	 *
	 * @param parameterNames
	 *            The array of parameter names, an event has.
	 * @return The cost to insert the event with the given parameters
	 */
	double getInsertCost(String[] parameterNames);

	/**
	 *
	 * @param parameterNames
	 *            The array of parameter names, an event has.
	 * @return The cost to duplicate the event with the given parameters
	 */
	double getDuplicateCost(String[] parameterNames);

}
