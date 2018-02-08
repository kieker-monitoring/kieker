/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.util;

import java.util.PriorityQueue;
import java.util.function.BiFunction;

/**
 * This class represents a median that changes in the course of time. Whenever a new element is added the median changes.
 *
 * @author Sören Henning
 *
 * @param <T>
 *            Type of elements the median should be calculated for
 */
public class RunningMedian<T extends Comparable<T>> {

	private final PriorityQueue<T> maxHeap = new PriorityQueue<>((x, y) -> y.compareTo(x));
	private final PriorityQueue<T> minHeap = new PriorityQueue<>((x, y) -> x.compareTo(y));
	private final BiFunction<T, T, T> meanBuilder;

	public RunningMedian() {
		this((x, y) -> x);
	}

	public RunningMedian(final BiFunction<T, T, T> meanBuilder) {
		this.meanBuilder = meanBuilder;
	}

	public void add(final T element) {
		this.insertToHeap(element);
		this.balanceHeaps();
	}

	private void insertToHeap(final T element) {
		if ((this.maxHeap.peek() == null) || (element.compareTo(this.maxHeap.peek()) < 0)) {
			// element < maxHeap.peek
			this.maxHeap.add(element);
		} else {
			// element >= maxHeap.peek
			this.minHeap.add(element);
		}
	}

	private void balanceHeaps() {
		if ((this.maxHeap.size() - this.minHeap.size()) > 1) {
			final T maxHeapRoot = this.maxHeap.poll();
			this.minHeap.add(maxHeapRoot);
		} else if ((this.minHeap.size() - this.maxHeap.size()) > 1) {
			final T minHeapRoot = this.minHeap.poll();
			this.maxHeap.add(minHeapRoot);
		}
	}

	public T getMedian() {
		if (this.maxHeap.isEmpty() && this.minHeap.isEmpty()) {
			throw new IllegalStateException("There are no present values for this running median.");
		} else if (this.maxHeap.size() == this.minHeap.size()) {
			return this.meanBuilder.apply(this.maxHeap.peek(), this.minHeap.peek());
		} else if (this.maxHeap.size() > this.minHeap.size()) {
			return this.maxHeap.peek();
		} else {
			return this.minHeap.peek();
		}
	}

	public static RunningMedian<Integer> forInteger() {
		return new RunningMedian<>((x, y) -> (x + y) / 2);
	}

	public static RunningMedian<Long> forLong() {
		return new RunningMedian<>((x, y) -> (x + y) / 2);
	}

	public static RunningMedian<Double> forDouble() {
		return new RunningMedian<>((x, y) -> (x + y) / 2);
	}

}
