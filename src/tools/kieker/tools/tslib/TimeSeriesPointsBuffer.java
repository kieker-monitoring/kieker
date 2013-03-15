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

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * This class is using a Deque to implement a bounded FifoBuffer.
 * 
 * @author Tom Frotscher
 * 
 * @param <T>
 */
public class TimeSeriesPointsBuffer<T> implements ITimeSeriesPointsBuffer<T> {

	private final Deque<T> buffer;
	private final int capacity;
	private boolean unbounded;

	/**
	 * Creates a new TimeSeriesPointsBuffer with the given capacity.
	 * If capacity <= 0, the capacity is infinite.
	 * 
	 * @param cap
	 *            Capacity of the Buffer
	 */
	public TimeSeriesPointsBuffer(final int cap) {
		if (cap <= 0) {
			this.capacity = cap;
			this.unbounded = true;
			this.buffer = new ArrayDeque<T>(1);
		} else {
			this.capacity = cap;
			this.unbounded = false;
			this.buffer = new ArrayDeque<T>(this.capacity);
		}
	}

	public void add(final T o) {
		if (this.unbounded) {
			this.buffer.add(o);
		} else {
			this.addBounded(o);
		}
	}

	private void addBounded(final T o) {
		if (this.buffer.size() == this.capacity) {
			this.buffer.remove();
			this.buffer.add(o);
		} else {
			this.buffer.add(o);
		}

	}

	public void remove() {
		this.buffer.remove();
	}

	public int getSize() {
		return this.buffer.size();
	}

	public void printBuffer() {
		System.out.println(this.buffer.toString());
	}

	public Deque<T> getBuffer() {
		return this.buffer;
	}

}
