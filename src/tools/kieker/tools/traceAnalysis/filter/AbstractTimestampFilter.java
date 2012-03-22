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

package kieker.tools.traceAnalysis.filter;

import kieker.analysis.plugin.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public abstract class AbstractTimestampFilter extends AbstractFilterPlugin {

	public static final long MAX_TIMESTAMP = Long.MAX_VALUE;
	public static final long MIN_TIMESTAMP = 0;

	private final long ignoreBeforeTimestamp;
	private final long ignoreAfterTimestamp;

	/**
	 * 
	 * @param configuration
	 *            ignored
	 * @param repositories
	 *            ignored
	 * @param ignoreExecutionsBeforeTimestamp
	 * @param ignoreExecutionsAfterTimestamp
	 */
	public AbstractTimestampFilter(final Configuration configuration) {
		super(configuration);
		/* Load the content for the fields from the given configuration. */
		this.ignoreBeforeTimestamp = configuration.getLongProperty(this.getConfigurationPropertyIgnoreBeforeTimestamp());
		this.ignoreAfterTimestamp = configuration.getLongProperty(this.getConfigurationPropertyIgnoreAfterTimestamp());
	}

	/**
	 * Returns true iff the given timestamp is within the configured time period.
	 * 
	 * @param timestamp
	 * @return
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

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(this.getConfigurationPropertyIgnoreBeforeTimestamp(), Long.toString(AbstractTimestampFilter.MIN_TIMESTAMP));
		configuration.setProperty(this.getConfigurationPropertyIgnoreAfterTimestamp(), Long.toString(AbstractTimestampFilter.MAX_TIMESTAMP));

		return configuration;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration(null);

		configuration.setProperty(this.getConfigurationPropertyIgnoreBeforeTimestamp(), Long.toString(this.ignoreBeforeTimestamp));
		configuration.setProperty(this.getConfigurationPropertyIgnoreAfterTimestamp(), Long.toString(this.ignoreAfterTimestamp));

		return configuration;
	}
}
