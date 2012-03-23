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

import java.util.Date;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationFactory;

/**
 * A timer implementation, counting in nanoseconds since a specified offset.
 * 
 * @author Jan Waller
 */
public final class SystemNanoTimer extends AbstractTimeSource {
	public static final String CONFIG_OFFSET = SystemNanoTimer.class.getName() + ".offset";
	private static final long NANO2MILLI = 1000L * 1000L;

	private final long offset;
	private final long clockdifference;

	public SystemNanoTimer(final Configuration configuration) {
		super(configuration);
		this.clockdifference = System.nanoTime() - (System.currentTimeMillis() * SystemNanoTimer.NANO2MILLI);
		if (configuration.getStringProperty(SystemNanoTimer.CONFIG_OFFSET).length() == 0) {
			this.offset = System.nanoTime();
		} else {
			this.offset = this.clockdifference + configuration.getLongProperty(SystemNanoTimer.CONFIG_OFFSET);
		}
	}

	public final long getTime() {
		return System.nanoTime() - this.offset;
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Time in nanoseconds since ");
		sb.append(new Date((this.offset - this.clockdifference) / SystemNanoTimer.NANO2MILLI));
		return sb.toString();
	}

	/**
	 * @return a singleton instance of SystemNanoTimer
	 */
	public static final ITimeSource getInstance() {
		return LazyHolder.INSTANCE;
	}

	/**
	 * SINGLETON
	 */
	private static final class LazyHolder { // NOCS (MissingCtorCheck)
		private static final ITimeSource INSTANCE = new SystemNanoTimer(ConfigurationFactory.createDefaultConfiguration().getPropertiesStartingWith(
				SystemNanoTimer.class.getName()));
	}
}
