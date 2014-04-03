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

package kieker.panalysis.concurrent;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import kieker.panalysis.base.Pipe;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class StealableConcurrentPipe<T> implements Pipe<T> {

	private final List<StealableConcurrentPipe<T>> otherPipes;
	/** FIXME must be thread-safe */
	private final Queue<T> items = new LinkedList<T>();

	private int numItemsToSteal;

	public StealableConcurrentPipe(final List<StealableConcurrentPipe<T>> otherPipes) {
		this.otherPipes = otherPipes;
	}

	public T take() {
		final T item = this.tryTake();
		if (item == null) {
			this.steal();
		}
		return this.tryTake();
	}

	public void put(final T record) {
		this.items.add(record);
	}

	public T tryTake() {
		return this.items.poll();
	}

	public boolean isEmpty() {
		return this.items.isEmpty();
	}

	public List<T> tryTakeMultiple(int numItemsToTake) {
		final List<T> tookItems = new LinkedList<T>();
		while (numItemsToTake-- > 0) {
			final T item = this.tryTake();
			if (item == null) {
				break;
			}
			tookItems.add(item);
		}
		return tookItems;
	}

	public void putMultiple(final List<T> newItems) {
		this.items.addAll(newItems);
	}

	private void steal() {
		for (final StealableConcurrentPipe<T> otherPipe : this.otherPipes) {
			final List<T> stolenItems = otherPipe.tryTakeMultiple(this.numItemsToSteal);
			if (stolenItems != null) {
				this.items.addAll(stolenItems);
				return;
			}
		}
	}

}
