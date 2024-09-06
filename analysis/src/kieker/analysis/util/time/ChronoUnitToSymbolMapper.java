/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.util.time;

import java.time.temporal.ChronoUnit;
import java.util.function.Function;

/**
 * This class is a {@link Function} that maps {@link ChronoUnit}s to their common symbols and abbreviations. If not common abbreviations exists a full name is
 * returned.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public final class ChronoUnitToSymbolMapper implements Function<ChronoUnit, String> { // NOPMD (switch statement leads to long class)

	private ChronoUnitToSymbolMapper() {}

	@Override
	public String apply(final ChronoUnit chronoUnit) { // NOPMD (switch statement leads to long method)
		switch (chronoUnit) {
		case NANOS:
			return "ns";
		case MICROS:
			return "�s";
		case MILLIS:
			return "ms";
		case SECONDS:
			return "s";
		case MINUTES:
			return "min";
		case HOURS:
			return "h";
		case DAYS:
			return "d";
		case YEARS:
			return "a";
		case WEEKS:
			return "weeks";
		case CENTURIES:
			return "centuries";
		case DECADES:
			return "decades";
		case ERAS:
			return "eras";
		case FOREVER:
			return "forever";
		case HALF_DAYS:
			return "half days";
		case MILLENNIA:
			return "millennia";
		case MONTHS:
			return "months";
		default:
			throw new IllegalArgumentException("Unknown ChronoUnit constant");
		}
	}

	public static ChronoUnitToSymbolMapper create() {
		return new ChronoUnitToSymbolMapper();
	}

}
