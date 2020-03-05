/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;

/**
 * A timer implementation, counting in nanoseconds since a specified offset.
 *
 * @author Jan Waller
 *
 * @since 1.5
 */
public final class SystemNanoTimer extends AbstractTimeSource {
	/** This is the name of the configuration determining the used offset (in nanoseconds). */
	public static final String CONFIG_OFFSET = SystemNanoTimer.class.getName() + ".offset";
	/** This is the name of the configuration determining the used time unit (0 = nanoseconds, 1 = microseconds, 2 = milliseconds, 3 = seconds). */
	public static final String CONFIG_UNIT = SystemNanoTimer.class.getName() + ".unit";

	private static final Logger LOGGER = LoggerFactory.getLogger(SystemNanoTimer.class);

	private final long offset;
	private final long clockdifference;
	private final TimeUnit timeunit;

	/**
	 *
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration for this timer.
	 */
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
			LOGGER.warn("Failed to determine value of {} (0, 1, 2, or 3 expected). Setting to 0=nanoseconds", CONFIG_UNIT);
			this.timeunit = TimeUnit.NANOSECONDS;
			break;
		}
	}

	@Override
	public final long getTime() {
		return this.timeunit.convert(System.nanoTime() - this.offset, TimeUnit.NANOSECONDS);
	}

	@Override
	public long getOffset() {
		return this.timeunit.convert(this.offset - this.clockdifference, TimeUnit.NANOSECONDS);
	}

	@Override
	public final TimeUnit getTimeUnit() {
		return this.timeunit;
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder(64);
		sb.append("Time in ")
				.append(this.timeunit.toString().toLowerCase(Locale.ENGLISH))
				.append(" (with nanoseconds precision) since ")
				.append(new Date(TimeUnit.NANOSECONDS.toMillis(this.offset - this.clockdifference)));
		return sb.toString();
	}

}
