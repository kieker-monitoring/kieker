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
package kieker.panalysis.framework.sequential;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import de.chw.concurrent.CircularWorkStealingDeque;

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
public class QueuePipe<T> extends AbstractPipe<T> {

	private final Queue<T> queue = new LinkedList<T>();

	static public <S0 extends ISource, S1 extends ISink<S1>, T> void connect(final IOutputPort<S0, T> sourcePort, final IInputPort<S1, T> targetPort) {
		final QueuePipe<T> pipe = new QueuePipe<T>();
		pipe.setSourcePort(sourcePort);
		pipe.setTargetPort(targetPort);
	}

	@Override
	public void putInternal(final T element) {
		this.queue.add(element);
	}

	public void putMultiple(final List<T> elements) {
		this.queue.addAll(elements);
	}

	@Override
	public T tryTakeInternal() {
		return this.queue.poll();
	}

	public T take() {
		final T element = this.tryTake();
		if (element == null) {
			throw CircularWorkStealingDeque.DEQUE_IS_EMPTY_EXCEPTION;
		}
		return element;
	}

	public T read() {
		return this.queue.peek();
	}

	public List<?> tryTakeMultiple(final int numElementsToTake) {
		throw new IllegalStateException("Taking more than one element is not possible. You tried to take " + numElementsToTake + " items.");
	}

	public boolean isEmpty() {
		return this.queue.isEmpty();
	}

}
