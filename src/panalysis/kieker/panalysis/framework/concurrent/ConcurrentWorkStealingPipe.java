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

package kieker.panalysis.framework.concurrent;

import java.util.LinkedList;
import java.util.List;

import de.chw.concurrent.CircularWorkStealingDeque;

import kieker.panalysis.framework.concurrent.steal.IStealStrategy;
import kieker.panalysis.framework.core.AbstractPipe;
import kieker.panalysis.framework.core.IInputPort;
import kieker.panalysis.framework.core.IOutputPort;
import kieker.panalysis.framework.core.ISink;
import kieker.panalysis.framework.core.ISource;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class ConcurrentWorkStealingPipe<T> extends AbstractPipe<T> {

	private final CircularWorkStealingDeque<T> circularWorkStealingDeque = new CircularWorkStealingDeque<T>();
	private final IStealStrategy<T> stealStrategy;

	// BETTER use a prioritized list that considers
	// <ul>
	// <li>the size of each deque to minimize steals
	// <li>the core's locality to improve cache access performance
	// </ul>

	private List<ConcurrentWorkStealingPipe<T>> pipesToStealFrom;
	private int numTakenElements = 0;

	ConcurrentWorkStealingPipe(final IStealStrategy<T> stealStrategy) {
		this.stealStrategy = stealStrategy;
	}

	public <S0 extends ISource, S1 extends ISink<S1>> void connect(final IOutputPort<S0, T> sourcePort, final IInputPort<S1, T> targetPort) {
		this.setSourcePort(sourcePort);
		this.setTargetPort(targetPort);
	}

	@Override
	protected void putInternal(final T token) {
		this.circularWorkStealingDeque.pushBottom(token);
	}

	public void putMultiple(final List<T> elements) {
		this.circularWorkStealingDeque.pushBottomMultiple(elements);
	}

	@Override
	protected T tryTakeInternal() {
		T record = this.circularWorkStealingDeque.popBottom();
		if (record == null) {
			record = this.stealStrategy.steal(this.getTargetPort(), this.pipesToStealFrom);
			if (record == null) {
				return null;
			}
			System.out.println("stolen (" + Thread.currentThread() + "): " + record);
		}
		this.numTakenElements++;
		return record;
	}

	public T take() {
		T record = this.circularWorkStealingDeque.popBottomEx();
		if (record == null) {
			record = this.stealStrategy.steal(this.getTargetPort(), this.pipesToStealFrom);
			if (record == null) {
				return null;
			}
		}
		this.numTakenElements++;
		return record;
	}

	public List<T> tryTakeMultiple(final int maxItemsToTake) {
		// TODO Auto-generated method stub
		// BETTER find a way to take multiple elements directly without a loop
		return null;
	}

	public T read() {
		final T record = this.circularWorkStealingDeque.readBottom();
		return record;
	}

	public boolean isEmpty() {
		return this.circularWorkStealingDeque.isEmpty();
	}

	public T steal() {
		return this.circularWorkStealingDeque.steal();
	}

	public List<T> stealMultiple(final int maxItemsToSteal) {
		int maxItemsToStealCounter = maxItemsToSteal;
		final List<T> stolenElements = new LinkedList<T>();
		while (maxItemsToStealCounter-- > 0) {
			final T stolenElement = this.steal();
			if (stolenElement == null) {
				break;
			}
			stolenElements.add(stolenElement);
		}
		// BETTER find a way to steal multiple elements directly without a loop
		return stolenElements;
	}

	public List<ConcurrentWorkStealingPipe<T>> getPipesToStealFrom() {
		return this.pipesToStealFrom;
	}

	public void setPipesToStealFrom(final List<ConcurrentWorkStealingPipe<T>> pipesToStealFrom) {
		this.pipesToStealFrom = pipesToStealFrom;
	}

	@Override
	public void onPipelineStarts() {
		this.pipesToStealFrom = new LinkedList<ConcurrentWorkStealingPipe<T>>(this.pipesToStealFrom);
		this.pipesToStealFrom.remove(this);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "={" + "numTakenElements=" + this.numTakenElements + "}";
	}

}
