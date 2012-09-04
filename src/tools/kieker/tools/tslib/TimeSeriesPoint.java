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

import java.util.Date;

/**
 * 
 * @author Andre van Hoorn
 * 
 * @param <T>
 */
public class TimeSeriesPoint<T> implements ITimeSeriesPoint<T> {

	private final Date time;
	private final T value;

	// TODO: Add possibility to attach arbitrary objects

	/**
	 * @param time
	 * @param value
	 */
	public TimeSeriesPoint(final Date time, final T value) {
		// TODO is that a good pattern or should we ensure that the object is immutable from outside?
		this.time = (Date) time.clone();
		this.value = value;
	}

	public Date getTime() {
		return this.time;
	}

	public T getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return "[" + this.getTime().getTime() + "=" + this.getValue() + "]";
	}

}
