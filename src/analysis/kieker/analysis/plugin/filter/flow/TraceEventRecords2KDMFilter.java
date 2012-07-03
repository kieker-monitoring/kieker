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

package kieker.analysis.plugin.filter.flow;

import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.analysis.repository.kdm.KDMRepository;
import kieker.common.configuration.Configuration;

/**
 * @author Jan Waller
 */
@Plugin(
		repositoryPorts = @RepositoryPort(name = TraceEventRecords2KDMFilter.REPOSITORY_PORT_NAME_KDM_MODEL, repositoryType = KDMRepository.class))
public class TraceEventRecords2KDMFilter extends AbstractFilterPlugin {

	public static final String REPOSITORY_PORT_NAME_KDM_MODEL = "kdmRepository";

	public TraceEventRecords2KDMFilter(final Configuration configuration) {
		super(configuration);
		// TODO Auto-generated constructor stub
	}

	public Configuration getCurrentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}
}
