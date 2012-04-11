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

import kieker.analysis.repository.annotation.Repository;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * @author Nils Christian Ehmke?
 */
@Repository
public abstract class AbstractRepository implements IRepository {

	public static final String CONFIG_NAME = "name-hiddenAndNeverExportedProperty";

	private static final Log LOG = LogFactory.getLog(AbstractRepository.class);
	protected final Configuration configuration;

	private final String name;

	/**
	 * Each Repository requires a constructor with a single Configuration object!
	 */
	public AbstractRepository(final Configuration configuration) {
		try {
			// TODO: somewhat dirty hack...
			final Configuration defaultConfig = this.getDefaultConfiguration();
			if (defaultConfig != null) {
				configuration.setDefaultConfiguration(defaultConfig);
			}
		} catch (final IllegalAccessException ex) {
			LOG.error("Unable to set repository default properties"); // ok to ignore ex here
		}
		this.configuration = configuration;

		/* try to determine name */
		this.name = configuration.getStringProperty(CONFIG_NAME);
	}

	/**
	 * This method should deliver an instance of {@code Configuration} containing the default properties for this class. In other words: Every class inheriting from
	 * {@code AbstractRepository} should implement this method to deliver an object which can be used for the constructor of this class.
	 * 
	 * @return The default properties.
	 */
	protected abstract Configuration getDefaultConfiguration();

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.analysis.repository.IRepository#getCurrentConfiguration()
	 */
	public final String getRepositoryName() {
		final String repositoryName = this.getClass().getAnnotation(Repository.class).name();
		if (repositoryName.equals(Repository.NO_NAME)) {
			return this.getClass().getSimpleName();
		} else {
			return repositoryName;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.analysis.repository.IRepository#getCurrentConfiguration()
	 */
	public final String getRepositoryDescription() {
		return this.getClass().getAnnotation(Repository.class).description();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.analysis.repository.IRepository#getCurrentConfiguration()
	 */
	public final String getName() {
		return this.name;
	}
}
