/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.timer;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * @author Jan Waller
 */
public abstract class AbstractTimeSource implements ITimeSource {
	private static final Log LOG = LogFactory.getLog(AbstractTimeSource.class);

	/**
	 * @deprecated to be removed in Kieker 1.8
	 */
	@Deprecated
	protected final Configuration configuration;

	protected AbstractTimeSource(final Configuration configuration) {
		try {
			// somewhat dirty hack...
			final Configuration defaultConfig = this.getDefaultConfiguration(); // NOPMD (overrideable)
			if (defaultConfig != null) {
				configuration.setDefaultConfiguration(defaultConfig);
			}
		} catch (final IllegalAccessException ex) {
			LOG.error("Unable to set timer custom default properties"); // ok to ignore ex here
		}
		this.configuration = configuration;
	}

	/**
	 * This method should be overwritten, iff the timer is external to Kieker and
	 * thus its default configuration is not included in the default config file.
	 * 
	 * @return The configuration object containing the default configuration.
	 */
	protected Configuration getDefaultConfiguration() { // NOPMD (default implementation)
		return null;
	}

	@Override
	public abstract String toString(); // findbugs: This has to be declared here to make this method abstract!
}
