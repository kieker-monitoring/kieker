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

import java.time.Duration;
import java.time.Instant;

/**
*
*
* @author Sören Henning
*
*/
public class EquidistantTimeSeries extends TimeSeries {

	private final Duration distance;

	private Instant begin;

	public EquidistantTimeSeries(final Duration distance, final Instant begin) {
		super();
		this.distance = distance;
		this.begin = begin;
	}

	public EquidistantTimeSeries(final Duration distance, final TimeSeries timeSeries) {
		super(timeSeries);
		this.distance = distance;
	}

	public EquidistantTimeSeries(final EquidistantTimeSeries timeSeries) {
		super(timeSeries);
		this.distance = timeSeries.distance;
		this.begin = timeSeries.begin;
	}

	public void appendBegin(final double value) {
		super.appendBegin(new TimeSeriesPoint(getNextBeginTime(), value));
	}

	public void appendEnd(final double value) {
		super.appendEnd(new TimeSeriesPoint(getNextEndTime(), value));
	}

	@Override
	public void appendBegin(final TimeSeriesPoint timeSeriesPoint) {
		if (!timeSeriesPoint.getTime().equals(getNextBeginTime())) {
			// TODO throw exception
			throw new IllegalArgumentException("Point does not fit in time series. Expected time: " + getNextBeginTime());
		}
		super.appendBegin(timeSeriesPoint);
	}

	@Override
	public void appendEnd(final TimeSeriesPoint timeSeriesPoint) {
		if (!timeSeriesPoint.getTime().equals(getNextEndTime())) {
			// TODO throw exception
			throw new IllegalArgumentException("Point does not fit in time series. Expected time: " + getNextEndTime());
		}
		super.appendEnd(timeSeriesPoint);
	}

	@Override
	public TimeSeriesPoint removeBegin() {
		final TimeSeriesPoint point = super.removeBegin();
		if (isEmpty()) {
			this.begin = point.getTime();
		}
		return point;
	}

	@Override
	public TimeSeriesPoint removeEnd() {
		final TimeSeriesPoint point = super.removeEnd();
		if (isEmpty()) {
			this.begin = point.getTime();
		}
		return point;
	}

	public Duration getDistance() {
		return distance;
	}

	public Instant getNextBeginTime() {
		return isEmpty() ? this.begin : super.getEnd().getTime().plus(this.distance);
	}

	public Instant getNextEndTime() {
		return isEmpty() ? this.begin : super.getBegin().getTime().minus(this.distance);
	}

}
