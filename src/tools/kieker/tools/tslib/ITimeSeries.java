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

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Andre van Hoorn
 * @since 1.10
 * 
 * @param <T>
 */
public interface ITimeSeries<T> {

	/**
	 * infite capacity.
	 */
	public static final int INFINITE_CAPACITY = -1;

	/**
	 * Returns the start of the time series, i.e., the time of the first value.
	 * 
	 * @return start Time
	 */
	public long getStartTime();

	/**
	 * Returns the temporal distance between to time series values with respect to the configured {@link TimeUnit} {@link #getDeltaTime()}.
	 * 
	 * @return delta Time
	 */
	// TODO rather take Timespan object!
	public long getDeltaTime();

	/**
	 * The {@link TimeUnit} used to specify the temporal distance between to values ({@link #getDeltaTime()}).
	 * 
	 * @return DeltaTime
	 */
	// TODO rather take Timespan object!
	public TimeUnit getDeltaTimeUnit();

	/**
	 * Appends the given value to the time series.
	 * 
	 * @param value
	 *            the value to append
	 * @return TimeSeries
	 */
	public ITimeSeriesPoint<T> append(T value);

	/**
	 * Appends the given value to the time series.
	 * 
	 * @param value
	 * @return appen List
	 */
	public List<ITimeSeriesPoint<T>> appendAll(T[] values);

	/**
	 * Returns the {@link ITimeSeriesPoint}s of this time series.
	 * 
	 * @return Points
	 */
	public List<ITimeSeriesPoint<T>> getPoints();

	/**
	 * Returns a list of all {@link #getPoints()#getValues()}.
	 * 
	 * @return Values
	 */
	public List<T> getValues();

	/**
	 * Returns the maximum number of elements held in this time series.
	 * 
	 * @return the capacity; {@link #INFINITE_CAPACITY} if the capacity is infinite
	 */
	public int getCapacity();

	/**
	 * Returns the number of value contained in the time series.
	 * 
	 * @return size
	 */
	public int size();

	/**
	 * Returns the time corresponding to the most recent value in the time series.
	 * 
	 * @return endTime
	 */
	public long getEndTime();

	/**
	 * Returns the frequency of the time series (how many time series points add up to an time unit of interest)
	 * needed to improve forecast accuracy
	 * e.g. a time series point each 15 minutes, interested in forecasting days: frequency is 96
	 * 
	 * @return frequency as the number of time series points
	 *         that add up either to the next bigger time unit and/or
	 *         to the estimated length of seasonal patterns in focus.
	 *         The value should not be too small
	 *         (still able to approximate the shape of the seasonal pattern)
	 *         and not to high (to limit the computational effort of
	 *         complex forecast strategies)
	 */
	public int getFrequency();

	/**
	 * @return The number of time series points that have fallen
	 *         of the time series due to capacity constraints by max_periods
	 * 
	 */
	// public long getSkippedValues();

	/**
	 * a period is a timeUnit of interest in this case
	 * returns the maximum number of periods the TS can contain -
	 * for forecasting seasonal patterns with the maximum duration of a period, MaxPeriods should be at least 3
	 * 
	 * capacity = frequency * periods
	 * 
	 * The amount of Frequency time series points form a period.
	 * This parameter defines the maximum number of periods that
	 * fit into the time series.
	 * As in a `fifo` queue the oldest values fall off
	 * when more recent values are added.
	 * The value of this setting should be at least 3
	 * to enable reliable pattern detection by complex
	 * forecast strategies and multiplied the by
	 * Frequency value not be higher than 200 if
	 * the computational effort of more complex
	 * forecast strategies should stay below one minute.
	 */
	// public long getMaxPeriods();
}
