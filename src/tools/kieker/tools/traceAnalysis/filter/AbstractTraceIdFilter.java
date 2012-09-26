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

package kieker.tools.traceAnalysis.filter;

import java.util.Set;
import java.util.TreeSet;

import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;

/**
 * Convenience class for plugins filtering by trace IDs.
 * 
 * @author Andre van Hoorn
 */
public abstract class AbstractTraceIdFilter extends AbstractFilterPlugin {
	/**
	 * List of trace IDs to accept. Set null to accept any ID.
	 */
	private final Set<Long> selectedTraceIds;

	/**
	 * 
	 * @param configuration
	 */
	public AbstractTraceIdFilter(final Configuration configuration) {
		super(configuration);
		this.selectedTraceIds = AbstractTraceIdFilter.extractIDsFromConfiguration(configuration, this.getConfigurationPropertySelectAllTraces(),
				this.getConfigurationPropertySelectedTraces());
	}

	/**
	 * Convenience function for inheriting filters.
	 * 
	 * @param configuration
	 * @param configurationPropertySelectAllTraces
	 * @param configurationPropertySelectedTraces
	 * @return
	 */
	private static Set<Long> extractIDsFromConfiguration(final Configuration configuration, final String configurationPropertySelectAllTraces,
			final String configurationPropertySelectedTraces) {
		final boolean passAll = configuration.getBooleanProperty(configurationPropertySelectAllTraces);
		if (passAll) {
			return null;
		} else {
			final Set<Long> idsToPass = new TreeSet<Long>();
			final String[] ids = configuration.getStringArrayProperty(configurationPropertySelectedTraces);
			for (final String id : ids) {
				idsToPass.add(Long.parseLong(id));
			}
			return idsToPass;
		}
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
	 * property used to indicate whether or not to select any ID.
	 * 
	 * @return
	 */
	protected abstract String getConfigurationPropertySelectAllTraces();

	/**
	 * Inheriting properties must provide the name of the configuration
	 * property used to store the set of selected trace IDs.
	 * 
	 * @return
	 */
	protected abstract String getConfigurationPropertySelectedTraces();

	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration(null);
		if (this.selectedTraceIds != null) {
			configuration.setProperty(this.getConfigurationPropertySelectAllTraces(), Boolean.toString(true));
			configuration.setProperty(this.getConfigurationPropertySelectedTraces(),
					Configuration.toProperty(this.selectedTraceIds.toArray(new Long[this.selectedTraceIds.size()])));
		} else {
			configuration.setProperty(this.getConfigurationPropertySelectAllTraces(), Boolean.toString(true));
			configuration.setProperty(this.getConfigurationPropertySelectedTraces(), "");
		}
		return configuration;
	}
}
