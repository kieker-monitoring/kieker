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

package kieker.analysisteetime.util.timeseries;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.stream.Stream;

import kieker.analysisteetime.util.IBackwardsIterable;

/**
 *
 *
 * @param <T>
 *            Type of the elements in this time series.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class TimeSeries<T extends ITimeSeriesPoint> implements Iterable<T>, IBackwardsIterable<T> {

	private final Deque<T> timeSeriesPoints;

	/**
	 * Constructs an empty time series.
	 */
	public TimeSeries() {
		this.timeSeriesPoints = new ArrayDeque<>();
	}

	/**
	 * Constructs a copy of the specified time series.
	 */
	public TimeSeries(final TimeSeries<T> timeSeries) {
		this.timeSeriesPoints = new ArrayDeque<>(timeSeries.timeSeriesPoints);
	}

	/**
	 * Appends a point at the end of this time series, so the point is the new
	 * earliest point in this time series.
	 */
	public void appendBegin(final T timeSeriesPoint) {
		if (!this.timeSeriesPoints.isEmpty() && !timeSeriesPoint.getTime().isBefore(this.timeSeriesPoints.getFirst().getTime())) {
			throw new IllegalArgumentException("Time series point to append is not before current earliest point.");
		}

		this.timeSeriesPoints.addFirst(timeSeriesPoint);
	}

	/**
	 * Appends a point at the end of this time series, so the point is the new
	 * latest point in this time series.
	 */
	public void appendEnd(final T timeSeriesPoint) {
		if (!this.timeSeriesPoints.isEmpty() && !timeSeriesPoint.getTime().isAfter(this.timeSeriesPoints.getLast().getTime())) {
			throw new IllegalArgumentException("Time series point to append is not after current latest point.");
		}

		this.timeSeriesPoints.addLast(timeSeriesPoint);
	}

	/**
	 * Returns the first/earliest point of this time series, or {@Code null}
	 * if this time series is empty.
	 */
	public ITimeSeriesPoint getBegin() {
		return this.timeSeriesPoints.peekFirst();
	}

	/**
	 * Returns the last/latest point of this time series, or {@Code null}
	 * if this time series is empty.
	 */
	public ITimeSeriesPoint getEnd() {
		return this.timeSeriesPoints.peekLast();
	}

	/**
	 * Retrieves and removes the first/earliest point of this time series, or returns
	 * {@Code null} if this time series is empty.
	 */
	public ITimeSeriesPoint removeBegin() {
		return this.timeSeriesPoints.pollFirst();
	}

	/**
	 * Retrieves and removes the last/latest point of this time series, or returns
	 * {@Code null} if this time series is empty.
	 */
	public ITimeSeriesPoint removeEnd() {
		return this.timeSeriesPoints.pollLast();
	}

	/**
	 * Returns an iterator over the time series points in this time series. The
	 * points will be returned in temporal order from earliest to latest.
	 */
	@Override
	public Iterator<T> iterator() {
		return this.timeSeriesPoints.iterator();
	}

	/**
	 * Returns an iterator over the time series points in this time series. The
	 * points will be returned in temporal order from latest to earliest.
	 */
	@Override
	public Iterator<T> backwardsIterator() {
		return this.timeSeriesPoints.descendingIterator();
	}

	/**
	 * Returns a sequential {@code Stream} with this time series as its source.
	 */
	public Stream<T> stream() {
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
