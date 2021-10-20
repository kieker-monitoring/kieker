/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

import kieker.analysis.AnalysisController;
import kieker.analysis.IProjectContext;
import kieker.analysis.analysisComponent.AbstractAnalysisComponent;
import kieker.analysis.exception.InvalidProjectContextException;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.repository.annotation.Repository;
import kieker.common.configuration.Configuration;

/**
 * This class should be used as a base for every repository used within
 * <i>Kieker</i>.
 *
 * @author Nils Christian Ehmke?
 *
 * @since 1.5
 * @deprecated 1.15 can be removed when all tools and filters have been ported
 *             to TeeTime
 */
@Deprecated
@Repository
public abstract class AbstractRepository extends AbstractAnalysisComponent implements IRepository {

	/**
	 * The second "default constructor".
	 *
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component. The component
	 *            will be registered.
	 */
	public AbstractRepository(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		// Register the repository
		if (projectContext instanceof AnalysisController) {
			((AnalysisController) projectContext).registerRepository(this);
		} else {
			throw new InvalidProjectContextException("Invalid analysis controller in constructor");
		}
	}

	/**
	 * This method delivers an instance of {@code Configuration} containing the
	 * default properties for this class.
	 *
	 * @return The default properties.
	 */
	@Override
	protected final Configuration getDefaultConfiguration() {
		final Configuration defaultConfiguration = new Configuration();
		// Get the annotation from the class
		final Repository repoAnnotation = this.getClass().getAnnotation(Repository.class);
		final Property[] propertyAnnotations = repoAnnotation.configuration();
		// Run through all properties within the annotation and add them to the
		// configuration object
		for (final Property property : propertyAnnotations) {
			defaultConfiguration.setProperty(property.name(), property.defaultValue());
		}
		return defaultConfiguration;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getRepositoryName() {
		final String repositoryName = this.getClass().getAnnotation(Repository.class).name();
		if (repositoryName.equals(Repository.NO_NAME)) {
			return this.getClass().getSimpleName();
		} else {
			return repositoryName;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getRepositoryDescription() {
		return this.getClass().getAnnotation(Repository.class).description();
	}
}
