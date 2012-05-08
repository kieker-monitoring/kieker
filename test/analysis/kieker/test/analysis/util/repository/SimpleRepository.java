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

package kieker.test.analysis.util.repository;

import kieker.analysis.repository.AbstractRepository;
import kieker.analysis.repository.annotation.Repository;
import kieker.common.configuration.Configuration;

/**
 * @author Nils Christian Ehmke, Jan Waller
 */
@Repository(name = SimpleRepository.REPOSITORY_NAME, description = SimpleRepository.REPOSITORY_DESCRIPTION)
public class SimpleRepository extends AbstractRepository { // NOPMD (SubClassOfTest)

	public static final String REPOSITORY_NAME = "repoName-hNcuzIKc8e";

	public static final String REPOSITORY_DESCRIPTION = "repoDescription-DEYmVN6sEp";

	public SimpleRepository(final Configuration configuration) {
		super(configuration);
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}
}
