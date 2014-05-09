/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.opad.record;

/**
 * Contains the beginning and end of an aggregation window.
 * 
 * @author Thomas Düllmann
 * @version 0.1
 */
public class AggregationWindow {
	private final long windowStart;
	private final long windowEnd;

	/**
	 * Constructor.
	 * 
	 * @param start
	 *            beginning of the aggregation window
	 * @param end
	 *            end of the aggregation window
	 */
	public AggregationWindow(final long start, final long end) {
		this.windowStart = start;
		this.windowEnd = end;
	}

	public long getWindowStart() {
		return this.windowStart;
	}

	public long getWindowEnd() {
		return this.windowEnd;
	}

	/**
	 * Checks whether the given timestamp is within the current aggregation window.
	 * 
	 * @param timestamp
	 *            timestamp to check
	 * @return true if timestamp is in window, else false
	 */
	public boolean isWithinWindow(final long timestamp) {
		boolean result = false;
		if ((timestamp >= this.windowStart) && (timestamp <= this.windowEnd)) {
			result = true;
		}
		return result;
	}

	/**
	 * Checks whether the given timestamp is before the current aggregation window.
	 * 
	 * @param timestamp
	 *            timestamp to check
	 * @return true if timestamp is before window, else false
	 */
	public boolean isBeforeWindow(final long timestamp) {
		return timestamp < this.windowStart;
	}

	/**
	 * Checks whether the given timestamp is after the current aggregation window.
	 * 
	 * @param timestamp
	 *            timestamp to check
	 * @return true if timestamp is after window, else false
	 */
	public boolean isAfterWindow(final long timestamp) {
		return timestamp > this.windowEnd;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(this.windowStart);
		sb.append(" -> ");
		sb.append(this.windowEnd);
		return sb.toString();
	}
}
