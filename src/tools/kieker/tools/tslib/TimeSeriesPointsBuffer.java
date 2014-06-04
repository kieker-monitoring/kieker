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

import java.util.concurrent.ConcurrentLinkedQueue;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * This class is using a ConcurrentLinkedQueue to implement a bounded FifoBuffer.
 * 
 * @author Tom Frotscher
 * @since 1.10
 * @param <T>
 */
public class TimeSeriesPointsBuffer<T> extends ConcurrentLinkedQueue<T> implements ITimeSeriesPointsBuffer<T> {
	private static final long serialVersionUID = -7988633509408488397L;
	private static final Log LOG = LogFactory.getLog(TimeSeriesPointsBuffer.class);

	private final int capacity;
	private final boolean unbounded;

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
	public synchronized boolean add(final T o) { // NOPMD It would not make sense to sync within this method
		if (this.unbounded) {
			return super.add(o);
		} else {
			return this.addBounded(o);
		}
	}

	private synchronized boolean addBounded(final T o) { // NOPMD It would not make sense to sync within this method
		if (this.size() == this.capacity) {
			super.poll();
			return super.add(o);
		} else {
			return super.add(o);
		}

	}

	@Override
	public T remove() {
		return super.poll();
	}

	public int getSize() {
		return this.size();
	}

	/**
	 * print buffer.
	 */
	public void printBuffer() {
		LOG.info(this.toString());
	}
}
