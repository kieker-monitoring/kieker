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

package kieker.tools.opad.timeseries;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Andre van Hoorn
 * @since 1.10
 *
 * @param <T>
 *            The type of the series.
 */
public interface ITimeSeries<T> {

	/**
	 * infite capacity.
	 */
	public static final int INFINITE_CAPACITY = -1;

	/**
	 * @return returns the start of the time series, i.e., the time of the first value.
	 *
	 * @since 1.10
	 */
	public long getStartTime();

	/**
	 * The {@link TimeUnit} used to specify the temporal distance between to values ({@link #getStartTime()}).
	 *
	 * @return returns the {@link TimeUnit}
	 *
	 * @since 1.10
	 */
	public TimeUnit getTimeSeriesTimeUnit();

	/**
	 * Returns the temporal distance between to time series values with respect to the configured {@link TimeUnit} {@link #getDeltaTime()}.
	 *
	 * @return returns the time delta.
	 *
	 * @since 1.10
	 */
	public long getDeltaTime();

	/**
	 * The {@link TimeUnit} used to specify the temporal distance between to values ({@link #getDeltaTime()}).
	 *
	 * @return returns the {@link TimeUnit}
	 * @since 1.10
	 */
	public TimeUnit getDeltaTimeUnit();

	/**
	 * Appends the given value to the time series.
	 *
	 * @param value
	 *            the value to append
	 * @return returns a time series
	 *
	 * @since 1.10
	 */
	public ITimeSeriesPoint<T> append(T value);

	/**
	 * Appends the given value to the time series.
	 *
	 * @param values
	 *            values to be appended
	 * @return returns a list of time series
	 * @since 1.10
	 */
	public List<ITimeSeriesPoint<T>> appendAll(T[] values);

	/**
	 * Returns the {@link ITimeSeriesPoint}s of this time series.
	 *
	 * @return Points
	 *
	 * @since 1.10
	 */
	public List<ITimeSeriesPoint<T>> getPoints();

	/**
	 * Returns a list of all {@code getPoints()getValues()}.
	 *
	 * @return get all values
	 * @since 1.10
	 */
	public List<T> getValues();

	/**
	 * Returns the maximum number of elements held in this time series.
	 *
	 * @return the capacity; {@link #INFINITE_CAPACITY} if the capacity is infinite
	 *
	 * @since 1.10
	 */
	public int getCapacity();

	/**
	 * @return returns the number of value contained in the time series.
	 *
	 * @since 1.10
	 */
	public int size();

	/**
	 * @return returns the time corresponding to the most recent value in the time series.
	 *
	 * @since 1.10
	 */
	public long getEndTime();

	/**
	 * Returns the frequency of the time series (how many time series points add up to an time unit of interest)
	 * needed to improve forecast accuracy
	 * e.g. a time series point each 15 minutes, interested in forecasting days: frequency is 96
	 *
	 * @since 1.10
	 * @return frequency as the number of time series points
	 *         that add up either to the next bigger time unit and/or
	 *         to the estimated length of seasonal patterns in focus.
	 *         The value should not be too small
	 *         (still able to approximate the shape of the seasonal pattern)
	 *         and not to high (to limit the computational effort of
	 *         complex forecast strategies)
	 */
	public int getFrequency();
}
