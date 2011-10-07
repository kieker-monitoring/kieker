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

import kieker.monitoring.core.configuration.Configuration;

public class DefaultSystemTimer extends AbstractTimeSource {
	/**
	 * Offset used to determine the number of nanoseconds since 1970-1-1. This
	 * is necessary since System.nanoTime() returns the elapsed nanoseconds
	 * since *some* fixed but arbitrary time.)
	 */
	private static final long OFFSET = (System.currentTimeMillis() * 1000000) - System.nanoTime(); // NOCS

	public DefaultSystemTimer(final Configuration configuration) {
		super(configuration);
	}

	/**
	 * @return the singleton instance of DefaultSystemTimer
	 */
	public final static DefaultSystemTimer getInstance() {
		return LazyHolder.INSTANCE;
	}

	@Override
	public long getTime() {
		return System.nanoTime() + DefaultSystemTimer.OFFSET;
	}

	/**
	 * SINGLETON
	 */
	private final static class LazyHolder { // NOCS (MissingCtorCheck)
		private static final DefaultSystemTimer INSTANCE = new DefaultSystemTimer(Configuration.createDefaultConfiguration().getPropertiesStartingWith(
				DefaultSystemTimer.class.getName()));
	}
}
