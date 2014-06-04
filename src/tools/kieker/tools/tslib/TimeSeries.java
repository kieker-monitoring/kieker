/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.tslib;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Andre van Hoorn, Tobias Rudolph, Andreas Eberlein
 * @since 1.10
 * @param <T>
 */
public class TimeSeries<T> implements ITimeSeries<T> {
	private long startTime;
	private long nextTime;
	private final long deltaTime;
	private final TimeUnit deltaTimeUnit;
	private final int frequency;
	private final int capacity;
	private final TimeSeriesPointsBuffer<ITimeSeriesPoint<T>> points;
	// approach of avh: private final CopyOnWriteArrayList<ITimeSeriesPoint<T>> points;
	private long oneStepMillis;

	// ITimeSeries<Double> newTS = new TimeSeries<Double>(ts.getStartTime(), ts.getDeltaTime()*2, ts.getDeltaTimeUnit(), ts.getFrequency(),(int)
	// ts.getMaxPeriods(),ts.getSkippedValues());
	/**
	 * 
	 * @param startTime
	 *            start time of Timeseries
	 * @param deltaTime
	 *            time of timeseries
	 * @param deltaTimeUnit
	 *            Time unit
	 * @param frequency
	 *            frquency
	 * 
	 * @param capacity
	 *            length of timeseries
	 */
	public TimeSeries(final long startTime, final long deltaTime, final TimeUnit deltaTimeUnit, final int frequency,
			final int capacity) {
		this.startTime = startTime;
		this.deltaTime = deltaTime;
		this.deltaTimeUnit = deltaTimeUnit;
		this.frequency = frequency;
		this.capacity = capacity;
		this.oneStepMillis = TimeUnit.MILLISECONDS.convert(this.deltaTime, this.deltaTimeUnit);

		if (ITimeSeries.INFINITE_CAPACITY == capacity) {
			this.points = new TimeSeriesPointsBuffer<ITimeSeriesPoint<T>>(capacity);
		} else {
			this.points = new TimeSeriesPointsBuffer<ITimeSeriesPoint<T>>(this.capacity);
		}

		this.nextTime = this.startTime;
		this.setNextTime();
	}

	/**
	 * @param startTime
	 *            start time of Timeseries
	 * @param deltaTime
	 *            time of timeseries
	 * @param deltaTimeUnit
	 *            Time unit
	 * @param capacity
	 *            length of timeseries
	 */
	public TimeSeries(final long startTime, final long deltaTime, final TimeUnit deltaTimeUnit, final int capacity) {
		// frequenc = 24 best practice
		this(startTime, deltaTime, deltaTimeUnit, 24, capacity);
	}

	/**
	 * @param startTime
	 *            start time of Timeseries
	 * @param deltaTime
	 *            time of timeseries
	 * @param deltaTimeUnit
	 *            Time unit
	 */
	public TimeSeries(final long startTime, final long deltaTime, final TimeUnit deltaTimeUnit) {
		this(startTime, deltaTime, deltaTimeUnit, ITimeSeries.INFINITE_CAPACITY);
	}

	public long getStartTime() {
		return this.startTime;
	}

	public long getDeltaTime() {
		return this.deltaTime;
	}

	public TimeUnit getDeltaTimeUnit() {
		return this.deltaTimeUnit;
	}

	/**
	 * @param value
	 *            value which should append to timeseries
	 * 
	 * @return tspoint
	 */
	public synchronized ITimeSeriesPoint<T> append(final T value) {
		final ITimeSeriesPoint<T> point;

		synchronized (value) {
			point = new TimeSeriesPoint<T>(this.nextTime, value);
			this.points.add(point);
			this.setNextTime();
		}

		// if ((this.capacity != ITimeSeries.INFINITE_CAPACITY) && (this.points.size() >= this.capacity)) {
		// this.startTime = this.startTime + this.deltaTimeUnit.toMillis(this.deltaTime);
		// this.skippedValues++;
		// }

		return point;
	}

	private void setNextTime() {
		this.nextTime = this.nextTime + this.oneStepMillis;
	}

	public List<ITimeSeriesPoint<T>> getPoints() {
		return new ArrayList<ITimeSeriesPoint<T>>(this.points);
	}

	/**
	 * @return value
	 */
	public List<T> getValues() {
		final List<ITimeSeriesPoint<T>> pointsCopy = this.getPoints();
		final List<T> retVals = new ArrayList<T>(pointsCopy.size());
		for (final ITimeSeriesPoint<T> curPoint : pointsCopy) {
			retVals.add(curPoint.getValue());
		}
		return retVals;
	}

	public int getCapacity() {
		return this.capacity;
	}

	/**
	 * @return size
	 */
	public int size() {
		return this.points.getSize();
	}

	public long getEndTime() {
		return this.getStartTime() + (this.oneStepMillis * this.size());
	}

	/**
	 * @param values
	 *            values to append in Timeseries
	 * @return Values
	 */
	public List<ITimeSeriesPoint<T>> appendAll(final T[] values) {
		final List<ITimeSeriesPoint<T>> retVals = new ArrayList<ITimeSeriesPoint<T>>(values.length);
		for (final T value : values) {
			retVals.add(this.append(value));
		}
		return retVals;
	}

	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append("Time Series with delta: " + this.deltaTime + " " + this.deltaTimeUnit + " starting at: " + this.getStartTime());
		for (final ITimeSeriesPoint<T> curPoint : this.getPoints()) {
			buf.append(curPoint);
		}
		return buf.toString();
	}

	public int getFrequency() {
		return this.frequency;
	}
}
