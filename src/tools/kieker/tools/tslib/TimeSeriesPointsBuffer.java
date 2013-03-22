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

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class is using a Deque to implement a bounded FifoBuffer.
 * 
 * @author Tom Frotscher
 * 
 * @param <T>
 */
public class TimeSeriesPointsBuffer<T> extends CopyOnWriteArrayList<T> {

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
		super();
		if (cap <= 0) {
			this.capacity = cap;
			this.unbounded = true;
		} else {
			this.capacity = cap;
			this.unbounded = false;
		}
	}

	@Override
	public boolean add(final T o) {
		if (this.unbounded) {
			return super.add(o);
		} else {
			return this.addBounded(o);
		}
	}

	private boolean addBounded(final T o) {
		if (this.size() == this.capacity) {
			super.remove(0);
			return super.add(o);
		} else {
			return super.add(o);
		}

	}

	public void remove() {
		super.remove(0);
	}

	public int getSize() {
		return this.size();
	}

	public void printBuffer() {
		System.out.println(this.toString());
	}

}
