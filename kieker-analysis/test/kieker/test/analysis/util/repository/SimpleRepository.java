/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.analysis.util.repository;

import kieker.analysis.IProjectContext;
import kieker.analysis.repository.AbstractRepository;
import kieker.analysis.repository.annotation.Repository;
import kieker.common.configuration.Configuration;

/**
 * A simple repository, used only for test purposes.
 *
 * @author Nils Christian Ehmke, Jan Waller
 *
 * @since 1.6
 */
@Repository(programmaticOnly = true, name = SimpleRepository.REPOSITORY_NAME, description = SimpleRepository.REPOSITORY_DESCRIPTION)
public class SimpleRepository extends AbstractRepository { // NOPMD (SubClassOfTest)

	/** The repository's dummy name. */
	public static final String REPOSITORY_NAME = "repoName-hNcuzIKc8e";
	/** The repository's dummy description. */
	public static final String REPOSITORY_DESCRIPTION = "repoDescription-DEYmVN6sEp";

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration for this repository.
	 * @param projectContext
	 *            The project context for this repository.
	 */
	public SimpleRepository(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}
}
