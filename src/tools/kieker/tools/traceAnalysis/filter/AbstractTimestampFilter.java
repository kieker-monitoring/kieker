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

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;

/**
 * This is an abstract base for components which filter elements based on the timestamp.
 * 
 * @author Andre van Hoorn
 */
public abstract class AbstractTimestampFilter extends AbstractFilterPlugin {

	public static final long MAX_TIMESTAMP = Long.MAX_VALUE;
	public static final long MIN_TIMESTAMP = 0;

	private final long ignoreBeforeTimestamp;
	private final long ignoreAfterTimestamp;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 * 
	 * @since 1.7
	 */
	public AbstractTimestampFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		// Load the content for the fields from the given configuration.
		this.ignoreBeforeTimestamp = configuration.getLongProperty(this.getConfigurationPropertyIgnoreBeforeTimestamp());
		this.ignoreAfterTimestamp = configuration.getLongProperty(this.getConfigurationPropertyIgnoreAfterTimestamp());
	}

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * 
	 * @deprecated To be removed in Kieker 1.8.
	 */
	@Deprecated
	public AbstractTimestampFilter(final Configuration configuration) {
		this(configuration, null);
	}

	/**
	 * Returns true iff the given timestamp is within the configured time period.
	 * 
	 * @param timestamp
	 *            The timestamp to check.
	 * @return true if and only if the timestamp is within the time period.
	 */
	protected boolean inRange(final long timestamp) {
		return (timestamp >= this.ignoreBeforeTimestamp) && (timestamp <= this.ignoreAfterTimestamp);
	}

	/**
	 * Inheriting properties must provide the name of the configuration
	 * property used to store the minimum time value to be accepted.
	 * 
	 * @return
	 */
	protected abstract String getConfigurationPropertyIgnoreBeforeTimestamp();

	/**
	 * Inheriting properties must provide the name of the configuration
	 * property used to store the maximum time value to be accepted.
	 * 
	 * @return
	 */
	protected abstract String getConfigurationPropertyIgnoreAfterTimestamp();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration(null);

		configuration.setProperty(this.getConfigurationPropertyIgnoreBeforeTimestamp(), Long.toString(this.ignoreBeforeTimestamp));
		configuration.setProperty(this.getConfigurationPropertyIgnoreAfterTimestamp(), Long.toString(this.ignoreAfterTimestamp));

		return configuration;
	}
}
