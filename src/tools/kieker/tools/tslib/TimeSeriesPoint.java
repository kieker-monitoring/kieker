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

/**
 * @author Andre van Hoorn
 * @since 1.10
 * @param <T>
 */
public class TimeSeriesPoint<T> implements ITimeSeriesPoint<T> {

	private final long time;
	private final T value;

	// TODO: Add possibility to attach arbitrary objects

	/**
	 * @param time
	 *            time of timeseriespoint
	 * @param value
	 *            value of timeseriespoint
	 */
	public TimeSeriesPoint(final long time, final T value) {
		// TODO is that a good pattern or should we ensure that the object is immutable from outside?
		this.time = time;
		this.value = value;
	}

	public long getTime() {
		return this.time;
	}

	public T getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return "[" + this.getTime() + "=" + this.getValue() + "]";
	}

}
