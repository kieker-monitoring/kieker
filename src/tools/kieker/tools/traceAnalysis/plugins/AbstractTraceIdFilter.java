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

package kieker.tools.traceAnalysis.plugins;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;

/**
 * Convenience class for plugins filtering by trace IDs.
 * 
 * @author Andre van Hoorn
 * 
 */
public abstract class AbstractTraceIdFilter extends AbstractAnalysisPlugin {
	/**
	 * List of trace IDs to accept.
	 */
	private final Set<Long> selectedTraceIds;

	/**
	 * 
	 * @param configuration
	 * @param repositories
	 * @param selectedTraceIds
	 */
	public AbstractTraceIdFilter(final Configuration configuration, final Map<String, AbstractRepository> repositories, final Set<Long> selectedTraceIds) {
		super(configuration, repositories);
		this.selectedTraceIds = selectedTraceIds;
	}

	/**
	 * 
	 * @param configuration
	 * @param repositories
	 */
	public AbstractTraceIdFilter(final Configuration configuration, final Map<String, AbstractRepository> repositories) {
		this(configuration, repositories, null);
	}

	@Override
	public boolean execute() {
		return true; // do nothing
	}

	@Override
	public void terminate(final boolean error) {
		// do nothing
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration defaultConfiguration = new Configuration();
		// TODO: Provide default properties.
		return defaultConfiguration;
	}

	/**
	 * Returns true iff the given trace ID is in the set of IDs to accept.
	 * This method is a convenience function to inheriting filters.
	 * 
	 * @param traceId
	 * @return
	 */
	protected boolean passId(final long traceId) {
		if ((this.selectedTraceIds != null) && !this.selectedTraceIds.contains(traceId)) {
			// not interested in this trace
			return false;
		}

		return true;
	}

	/**
	 * Inheriting properties must provide the name of the configuration
	 * property used to store the set of selected trace IDs.
	 * 
	 * @return
	 */
	protected abstract String getConfigurationPropertySelectedTraces();

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration(null);

		if (this.selectedTraceIds != null) {
			final String selectedTracesArr[] = new String[this.selectedTraceIds.size()];
			final Iterator<Long> iter = this.selectedTraceIds.iterator();
			int i = 0;
			while (iter.hasNext()) {
				selectedTracesArr[i++] = iter.next().toString();
			}
			configuration.setProperty(this.getConfigurationPropertySelectedTraces(), Configuration.toProperty(selectedTracesArr));
		}
		return configuration;
	}

	@Override
	protected Map<String, AbstractRepository> getDefaultRepositories() {
		return new HashMap<String, AbstractRepository>();
	}

	@Override
	public Map<String, AbstractRepository> getCurrentRepositories() {
		return new HashMap<String, AbstractRepository>();
	}
}
