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

package kieker.analysis.plugin;

/**
 * @author Andre van Hoorn
 */
public interface IFilterPlugin {

	/**
	 * Initiates the start of a component.
	 * This method is called once when a AnalysisController's run() method is called.
	 * This implementation must not be blocking!
	 * Asynchronous consumers would spawn (an) asynchronous thread(s) in this method.
	 * 
	 * @return true on success; false otherwise.
	 */
	public boolean init();

	/**
	 * Initiates a termination of the component. The value of the parameter
	 * error indicates whether an error occurred.
	 * 
	 * @param error
	 *            true iff an error occurred.
	 */
	public void terminate(boolean error);

}
