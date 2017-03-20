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

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 *
 *
 * @author Sören Henning
 *
 */
public class TimeSeries implements Iterable<TimeSeriesPoint>, kieker.analysisteetime.util.BackwardsIterable<TimeSeriesPoint> {

	private final Deque<TimeSeriesPoint> timeSeriesPoints;

	/**
	 * Constructs an empty time series.
	 */
	public TimeSeries() {
		this.timeSeriesPoints = new ArrayDeque<>();
	}

	/**
	 * Constructs a time series containing the time series points of the
	 * specified collection, in the order they are returned by the
	 * collection's iterator.
	 */
	public TimeSeries(final Collection<TimeSeriesPoint> timeSeriesPoints) {
		this.timeSeriesPoints = new ArrayDeque<>(timeSeriesPoints.size());
		for (TimeSeriesPoint timeSeriesPoint : timeSeriesPoints) {
			appendEnd(timeSeriesPoint);
		}
		// TODO Catch exception to provide new
	}

	/**
	 * Constructs a copy of the specified time series.
	 */
	public TimeSeries(final TimeSeries timeSeries) {
		this.timeSeriesPoints = new ArrayDeque<>(timeSeries.timeSeriesPoints);
	}

	/**
	 * Appends a point at the end of this time series, so the point is the new
	 * earliest point in this time series.
	 */
	public void appendBegin(final TimeSeriesPoint timeSeriesPoint) {
		if (!this.timeSeriesPoints.isEmpty() && !timeSeriesPoint.getTime().isBefore(this.timeSeriesPoints.getFirst().getTime())) {
			// TODO throw expection
		}

		this.timeSeriesPoints.addFirst(timeSeriesPoint);
	}

	/**
	 * Appends a point at the end of this time series, so the point is the new
	 * latest point in this time series.
	 */
	public void appendEnd(final TimeSeriesPoint timeSeriesPoint) {
		if (!this.timeSeriesPoints.isEmpty() && !timeSeriesPoint.getTime().isAfter(this.timeSeriesPoints.getLast().getTime())) {
			// TODO throw expection
		}

		this.timeSeriesPoints.addLast(timeSeriesPoint);
	}

	/**
	 * Returns the first/earliest point of this time series, or {@Code null}
	 * if this time series is empty.
	 */
	public TimeSeriesPoint getBegin() {
		return this.timeSeriesPoints.peekFirst();
	}

	/**
	 * Returns the last/latest point of this time series, or {@Code null}
	 * if this time series is empty.
	 */
	public TimeSeriesPoint getEnd() {
		return this.timeSeriesPoints.peekLast();
	}

	/**
	 * Retrieves and removes the first/earliest point of this time series, or returns
	 * {@Code null} if this time series is empty.
	 */
	public TimeSeriesPoint removeBegin() {
		return this.timeSeriesPoints.pollFirst();
	}

	/**
	 * Retrieves and removes the last/latest point of this time series, or returns
	 * {@Code null} if this time series is empty.
	 */
	public TimeSeriesPoint removeEnd() {
		return this.timeSeriesPoints.pollLast();
	}

	/**
	 * Returns an iterator over the time series points in this time series. The
	 * points will be returned in temporal order from earliest to latest.
	 */
	@Override
	public Iterator<TimeSeriesPoint> iterator() {
		return this.timeSeriesPoints.iterator();
	}

	/**
	 * Returns an iterator over the time series points in this time series. The
	 * points will be returned in temporal order from latest to earliest.
	 */
	@Override
	public Iterator<TimeSeriesPoint> backwardsIterator() {
		return this.timeSeriesPoints.descendingIterator();
	}

	/**
	 * Returns an array containing all of the values of the time series point
	 * in this time series in temporal order from earliest to latest.
	 */
	public double[] toValuesArray() {
		double[] array = new double[size()];
		int i = 0;
		for (final TimeSeriesPoint point : this.timeSeriesPoints) {
			array[i] = point.getValue();
			i++;
		}
		return array;
	}

	/**
	 * Returns a sequential Stream with this time series as its source.
	 */
	public Stream<TimeSeriesPoint> stream() {
		return this.timeSeriesPoints.stream();
	}

	/**
	 * Returns the number of time series points in this time series.
	 */
	public int size() {
		return this.timeSeriesPoints.size();
	}

	/**
	 * Returns {@code true} if this time series contains no time series points.
	 */
	public boolean isEmpty() {
		return this.timeSeriesPoints.isEmpty();
	}

	@Override
	public String toString() {
		return this.timeSeriesPoints.toString();
	}
}
