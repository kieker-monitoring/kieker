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
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.monitoring.core.configuration.ConfigurationFactory;

/**
 * A timer implementation, counting in nanoseconds since a specified offset.
 * 
 * @author Jan Waller
 */
public final class SystemNanoTimer extends AbstractTimeSource {
	public static final String CONFIG_OFFSET = SystemNanoTimer.class.getName() + ".offset";
	public static final String CONFIG_UNIT = SystemNanoTimer.class.getName() + ".unit";

	private static final Log LOG = LogFactory.getLog(SystemNanoTimer.class);

	private final long offset;
	private final long clockdifference;
	private final TimeUnit timeunit;

	public SystemNanoTimer(final Configuration configuration) {
		super(configuration);
		this.clockdifference = System.nanoTime() - (TimeUnit.MILLISECONDS.toNanos(System.currentTimeMillis()));
		if (configuration.getStringProperty(CONFIG_OFFSET).length() == 0) {
			this.offset = System.nanoTime();
		} else {
			this.offset = this.clockdifference + configuration.getLongProperty(CONFIG_OFFSET);
		}
		final int timeunitval = configuration.getIntProperty(CONFIG_UNIT);
		switch (timeunitval) {
		case 0:
			this.timeunit = TimeUnit.NANOSECONDS;
			break;
		case 1:
			this.timeunit = TimeUnit.MICROSECONDS;
			break;
		case 2:
			this.timeunit = TimeUnit.MILLISECONDS;
			break;
		case 3:
			this.timeunit = TimeUnit.SECONDS;
			break;
		default:
			LOG.warn("Failed to determine value of " + CONFIG_UNIT + " (0, 1, 2, or 3 expected). Setting to 0=nanoseconds");
			this.timeunit = TimeUnit.NANOSECONDS;
			break;
		}
	}

	public final long getTime() {
		return TimeUnit.NANOSECONDS.convert(System.nanoTime() - this.offset, this.timeunit);
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Time in " + this.timeunit.toString().toLowerCase(Locale.ENGLISH) + " (with nanoseconds precision) since ");
		sb.append(new Date(TimeUnit.NANOSECONDS.toMicros(this.offset - this.clockdifference)));
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
