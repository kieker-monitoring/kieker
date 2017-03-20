/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.util.timeseries;

import java.time.Instant;

/**
 * {@link TimeSeriesPoint}s are the elements of {@link TimeSeries}. They
 * consists of an instantaneous time stamp and a value.
 *
 * @author Sören Henning
 *
 */
public class TimeSeriesPoint {

	private final Instant time;

	private final long value;

	public TimeSeriesPoint(final Instant time, final long value) {
		this.time = time;
		this.value = value;
	}

	public Instant getTime() {
		return time;
	}

	public long getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "[" + this.time + "=" + this.value + "]";
	}

}
