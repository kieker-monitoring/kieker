/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.util.time;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * Helper class for {@link ChronoUnit}s.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public final class ChronoUnits {

	private ChronoUnits() {}

	/**
	 * Will be obsolete in Java 9 and can be replaced by {@code TimeUnit.toChronoUnit()}.
	 * See: https://bugs.openjdk.java.net/browse/JDK-8141452
	 *
	 */
	public static ChronoUnit createFromTimeUnit(final TimeUnit timeUnit) {
		switch (timeUnit) {
		case DAYS:
			return ChronoUnit.DAYS;
		case HOURS:
			return ChronoUnit.HOURS;
		case MINUTES:
			return ChronoUnit.MINUTES;
		case SECONDS:
			return ChronoUnit.SECONDS;
		case MILLISECONDS:
			return ChronoUnit.MILLIS;
		case MICROSECONDS:
			return ChronoUnit.MICROS;
		case NANOSECONDS:
			return ChronoUnit.NANOS;
		default:
			throw new IllegalArgumentException("Unknown TimeUnit constant");
		}
	}

}
