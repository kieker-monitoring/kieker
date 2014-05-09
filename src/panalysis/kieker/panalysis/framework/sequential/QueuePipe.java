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

import kieker.panalysis.framework.core.IInputPort;
import kieker.panalysis.framework.core.IOutputPort;
import kieker.panalysis.framework.core.IPipe;
import kieker.panalysis.framework.core.ISink;
import kieker.panalysis.framework.core.ISource;
import kieker.panalysis.framework.core.IStage;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class QueuePipe<T> implements IPipe<T> {

	private final Queue<T> queue = new LinkedList<T>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.panalysis.framework.core.IPipe#put(java.lang.Object)
	 */
	public void put(final T element) {
		this.queue.add(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.panalysis.framework.core.IPipe#putMultiple(java.util.List)
	 */
	public void putMultiple(final List<T> elements) {
		this.queue.addAll(elements);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.panalysis.framework.core.IPipe#tryTake()
	 */
	public T tryTake() {
		return this.queue.poll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.panalysis.framework.core.IPipe#take()
	 */
	public T take() {
		final T element = this.tryTake();
		if (element == null) {
			throw CircularWorkStealingDeque.DEQUE_IS_EMPTY_EXCEPTION;
		}
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.panalysis.framework.core.IPipe#read()
	 */
	public T read() {
		return this.queue.peek();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.panalysis.framework.core.IPipe#tryTakeMultiple(int)
	 */
	public List<?> tryTakeMultiple(final int numElementsToTake) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.panalysis.framework.core.IPipe#fireSignalClosing()
	 */
	public void fireSignalClosing() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.panalysis.framework.core.IPipe#setSourcePort(kieker.panalysis.framework.core.IOutputPort)
	 */
	public <S extends ISource, A extends T> void setSourcePort(final IOutputPort<S, T> sourcePort) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.panalysis.framework.core.IPipe#setTargetPort(kieker.panalysis.framework.core.IInputPort)
	 */
	public <S extends ISink<S>, A extends T> void setTargetPort(final IInputPort<S, T> targetPort) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.panalysis.framework.core.IPipe#getTargetStage()
	 */
	public IStage getTargetStage() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.panalysis.framework.core.IPipe#onPipelineStarts()
	 */
	public void onPipelineStarts() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.panalysis.framework.core.IPipe#onPipelineStops()
	 */
	public void onPipelineStops() {
		// TODO Auto-generated method stub

	}

}
