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

package kieker.monitoring.timer;

import java.util.Properties;

import kieker.monitoring.core.configuration.Configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractTimeSource implements ITimeSource {
	private static final Log LOG = LogFactory.getLog(AbstractTimeSource.class);

	protected final Configuration configuration;

	protected AbstractTimeSource(final Configuration configuration) {
		try {
			// somewhat dirty hack...
			final Properties defaultProps = getDefaultProperties(); // NOPMD by jwa on 20.09.11 15:19
			if (defaultProps != null) {
				configuration.setDefaultProperties(defaultProps);
			}
		} catch (final IllegalAccessException ex) {
			AbstractTimeSource.LOG.error("Unable to set timer custom default properties");
		}
		this.configuration = configuration;
	}

	/**
	 * This method should be overwritten, iff the timer is external to Kieker and
	 * thus its default configuration is not included in the default config file.
	 * 
	 * @return
	 */
	protected Properties getDefaultProperties() { // NOPMD
		return null;
	}

	@Override
	public abstract long getTime();
}
