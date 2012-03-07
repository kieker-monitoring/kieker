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

package kieker.analysis.repository;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

public abstract class AbstractRepository {

	private static final Log LOG = LogFactory.getLog(AbstractRepository.class);
	protected final Configuration configuration;

	/**
	 * Each Repository requires a constructor with a single Configuration object!
	 */
	public AbstractRepository(final Configuration configuration) {
		try {
			// TODO: somewhat dirty hack...
			final Configuration defaultConfig = this.getDefaultConfiguration(); // NOPMD
			if (defaultConfig != null) {
				configuration.setDefaultConfiguration(defaultConfig);
			}
		} catch (final IllegalAccessException ex) {
			AbstractRepository.LOG.error("Unable to set repository default properties"); // ok to ignore ex here
		}
		this.configuration = configuration;
	}

	/**
	 * This method should deliver an instance of {@code Configuration} containing the default properties for this class. In other words: Every class inheriting from
	 * {@code AbstractRepository} should implement this method to deliver an object which can be used for the constructor of this class.
	 * 
	 * @return The default properties.
	 */
	protected abstract Configuration getDefaultConfiguration();

	/**
	 * This method should deliver a {@code Configuration} object containing the current configuration of this instance. In other words: The constructor should be
	 * able to use the given object to initialize a new instance of this class with the same intern properties.
	 * 
	 * @return A completely filled configuration object.
	 */
	public abstract Configuration getCurrentConfiguration();

}
