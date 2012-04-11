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

package kieker.monitoring.timer;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.monitoring.core.configuration.ConfigurationFactory;

/**
 * A timer implementation, counting in nanoseconds since 1970-1-1.
 * 
 * @author Jan Waller
 * 
 * @deprecated will be removed in Kieker 1.6
 */
@Deprecated
public final class DefaultSystemTimer extends AbstractTimeSource {
	private static final Log LOG = LogFactory.getLog(DefaultSystemTimer.class);

	private final ITimeSource timeSource;

	/**
	 * 
	 * @deprecated replaced with SystemNanoTimer
	 */
	@Deprecated
	public DefaultSystemTimer(final Configuration configuration) throws IllegalAccessException { // this error should never be thrown
		super(configuration);
		LOG.warn("This timer is deprecated, use 'kieker.monitoring.timer.SystemNanoTimer' instead.");
		final Configuration nanoConfiguration = configuration.getPropertiesStartingWith(SystemNanoTimer.class.getName());
		nanoConfiguration.setDefaultConfiguration(ConfigurationFactory.createDefaultConfiguration());
		nanoConfiguration.setProperty(SystemNanoTimer.CONFIG_OFFSET, "0");
		this.timeSource = new SystemNanoTimer(nanoConfiguration.getPropertiesStartingWith(SystemNanoTimer.class.getName()));
	}

	/**
	 * @return a singleton instance of SystemNanoTimer
	 */
	public static final ITimeSource getInstance() {
		return LazyHolder.INSTANCE;
	}

	public final long getTime() {
		return this.timeSource.getTime();
	}

	@Override
	public final String toString() {
		return this.timeSource.toString();
	}

	/**
	 * SINGLETON
	 */
	private static final class LazyHolder { // NOCS (MissingCtorCheck)
		private static final ITimeSource INSTANCE = new SystemNanoTimer(ConfigurationFactory.createDefaultConfiguration().getPropertiesStartingWith(
				SystemNanoTimer.class.getName()));
	}
}
