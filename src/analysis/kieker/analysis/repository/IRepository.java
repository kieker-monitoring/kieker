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

package kieker.analysis.repository;

import kieker.analysis.analysisComponent.IAnalysisComponent;

/**
 * This is the interface for repositories within Kieker.
 * 
 * @author Andre van Hoorn, Nils Christian Ehmke, Jan Waller
 * 
 * @since 1.5
 */
public interface IRepository extends IAnalysisComponent {

	/**
	 * This method delivers the repository name of this repository type. The name should be unique, e.g., the classname.
	 * 
	 * @return The name of the repository type.
	 * 
	 * @since 1.5
	 */
	public abstract String getRepositoryName();

	/**
	 * This method delivers the description of this repository type.
	 * 
	 * @return The description of the repository type.
	 * 
	 * @since 1.5
	 */
	public abstract String getRepositoryDescription();

}
