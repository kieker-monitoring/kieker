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
 * 
 * @param <T>
 */
public interface ITimeSeries<T> {

	public static final int INFINITE_CAPACITY = -1;

	/**
	 * Returns the start of the time series, i.e., the time of the first value.
	 * 
	 * @return
	 */
	public long getStartTime();

	/**
	 * Returns the temporal distance between to time series values with respect to the configured {@link TimeUnit} {@link #getDeltaTime()}.
	 * 
	 * @return
	 */
	// TODO rather take Timespan object!
	public long getDeltaTime();

	/**
	 * The {@link TimeUnit} used to specify the temporal distance between to values ({@link #getDeltaTime()}).
	 * 
	 * @return
	 */
	// TODO rather take Timespan object!
	public TimeUnit getDeltaTimeUnit();

	/**
	 * Appends the given value to the time series.
	 * 
	 * @param value
	 *            the value to append
	 * @return
	 */
	public ITimeSeriesPoint<T> append(T value);

	/**
	 * Appends the given value to the time series.
	 * 
	 * @param value
	 * @return
	 */
	public List<ITimeSeriesPoint<T>> appendAll(T[] values);

	/**
	 * Returns the {@link ITimeSeriesPoint}s of this time series.
	 * 
	 * @return
	 */
	public List<ITimeSeriesPoint<T>> getPoints();

	/**
	 * Returns a list of all {@link #getPoints()#getValues()}
	 * 
	 * @return
	 */
	public List<T> getValues();

	/**
	 * Returns the maximum number of elements held in this time series.
	 * 
	 * @return the capacity; {@link #INFINITE_CAPACITY} if the capacity is infinite
	 */
	public int getCapacity();

	/**
	 * Returns the number of value contained in the time series
	 * 
	 * @return
	 */
	public int size();

	/**
	 * Returns the time corresponding to the most recent value in the time series
	 * 
	 * @return
	 */
	public long getEndTime();
}
