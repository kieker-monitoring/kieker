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
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.buffer.CircularFifoBuffer;

/**
 * @author Andre van Hoorn
 * 
 * @param <T>
 */
public class TimeSeries<T> implements ITimeSeries<T> {
	private final Date startTime;
	private final Date nextTime;
	private final long deltaTime;
	private final TimeUnit deltaTimeUnit;
	private final int capacity;
	private final CircularFifoBuffer points;
	// approach of avh: private final CopyOnWriteArrayList<ITimeSeriesPoint<T>> points;
	private long oneStepMillis;

	/**
	 * @param startTime
	 * @param deltaTime
	 * @param deltaTimeUnit
	 * @param capacity
	 */
	public TimeSeries(final Date startTime, final long deltaTime, final TimeUnit deltaTimeUnit, final int capacity) {
		this.startTime = (Date) startTime.clone();
		this.deltaTime = deltaTime;
		this.deltaTimeUnit = deltaTimeUnit;
		this.capacity = capacity;
		this.oneStepMillis = TimeUnit.MILLISECONDS.convert(this.deltaTime, this.deltaTimeUnit);

		if (ITimeSeries.INFINITE_CAPACITY == capacity) {
			this.points = new CircularFifoBuffer();
		} else {
			this.points = new CircularFifoBuffer(this.capacity);
		}

		this.nextTime = (Date) this.startTime.clone();
		this.setNextTime();
	}

	public TimeSeries(final Date startTime, final long deltaTime, final TimeUnit deltaTimeUnit) {
		this(startTime, deltaTime, deltaTimeUnit, ITimeSeries.INFINITE_CAPACITY);
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public long getDeltaTime() {
		return this.deltaTime;
	}

	public TimeUnit getDeltaTimeUnit() {
		return this.deltaTimeUnit;
	}

	public synchronized ITimeSeriesPoint<T> append(final T value) {
		final ITimeSeriesPoint<T> point = new TimeSeriesPoint<T>(this.nextTime, value);
		this.points.add(point);
		this.setNextTime();
		return point;
	}

	private void setNextTime() {
		this.nextTime.setTime(this.nextTime.getTime() + this.oneStepMillis);
	}

	public List<ITimeSeriesPoint<T>> getPoints() {
		return new ArrayList<ITimeSeriesPoint<T>>(this.points);
	}

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

	public int size() {
		return this.points.size();
	}

	public Date getEndTime() {
		return new Date(this.getStartTime().getTime() + (this.oneStepMillis * this.size()));
	}

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
}
