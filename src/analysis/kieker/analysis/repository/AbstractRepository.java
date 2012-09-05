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

package kieker.analysis.repository;

import kieker.analysis.plugin.annotation.Property;
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
	 * This method delivers an instance of {@code Configuration} containing the default properties for this class.
	 * 
	 * @return The default properties.
	 */
	protected final Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();

		// Get the annotation from the class
		final Repository repoAnnotation = this.getClass().getAnnotation(Repository.class);
		final Property[] propertyAnnotations = repoAnnotation.configuration();

		// Run through all properties within the annotation and add them to the configuration object
		for (final Property property : propertyAnnotations) {
			configuration.setProperty(property.name(), property.defaultValue());
		}

		return configuration;
	}

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
