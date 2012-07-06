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

package kieker.analysis.repository;

import kieker.common.configuration.Configuration;

/**
 * 
 * @author Andre van Hoorn, Nils Christian Ehmke, Jan Waller
 * 
 */
public interface IRepository<C extends Configuration> {

	/**
	 * This method should deliver a {@code Configuration} object containing the current configuration of this instance. In other words: The constructor should be
	 * able to use the given object to initialize a new instance of this class with the same intern properties.
	 * 
	 * @return A completely filled configuration object.
	 */
	public abstract C getCurrentConfiguration();

	/**
	 * This method delivers the repository name of this repository type. The name should be unique, e.g., the classname.
	 * 
	 * @return The name of the repository type.
	 */
	public abstract String getRepositoryName();

	/**
	 * This method delivers the description of this repository type.
	 * 
	 * @return The description of the repository type.
	 */
	public abstract String getRepositoryDescription();

	/**
	 * This method delivers the current name of this repository instance. The name does not have to be unique.
	 * 
	 * @return The current name of the repository instance.
	 */
	public abstract String getName();
}
