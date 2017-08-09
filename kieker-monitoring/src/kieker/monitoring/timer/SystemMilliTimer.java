/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

/**
 * A timer implementation, counting in milliseconds since a specified offset.
 * 
 * @author Jan Waller
 * 
 * @since 1.5
 */
public final class SystemMilliTimer extends AbstractTimeSource {
	/** The name of the configuration for the offset. */
	public static final String CONFIG_OFFSET = SystemMilliTimer.class.getName() + ".offset";
	/** The name of the configuration for the time unit (0 = nanoseconds, 1 = microseconds, 2 = milliseconds, 3 = seconds). */
	public static final String CONFIG_UNIT = SystemMilliTimer.class.getName() + ".unit";

	private static final Log LOG = LogFactory.getLog(SystemMilliTimer.class);

	private final long offset;
	private final TimeUnit timeunit;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this timer.
	 */
	public SystemMilliTimer(final Configuration configuration) {
		super(configuration);
		if (configuration.getStringProperty(CONFIG_OFFSET).length() == 0) {
			this.offset = System.currentTimeMillis();
		} else {
			this.offset = configuration.getLongProperty(CONFIG_OFFSET);
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

	@Override
	public final long getTime() {
		return this.timeunit.convert(System.currentTimeMillis() - this.offset, TimeUnit.MILLISECONDS);
	}

	@Override
	public long getOffset() {
		return this.timeunit.convert(this.offset, TimeUnit.MILLISECONDS);
	}

	@Override
	public final TimeUnit getTimeUnit() {
		return this.timeunit;
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder(64);
		sb.append("Time in " + this.timeunit.toString().toLowerCase(Locale.ENGLISH) + " (with milliseconds precision) since ");
		sb.append(new Date(this.offset));
		return sb.toString();
	}
}
