/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.util.stage.trigger;

import java.util.ArrayDeque;
import java.util.Deque;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import teetime.stage.basic.ITransformation;

/**
 * Stage that queues all incoming elements and forwards them when receiving {@link Trigger}.
 *
 * @param <T>
 *            Type of elements
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class ForwardOnTriggerStage<T> extends AbstractStage implements ITransformation<T, T> {

	private final SendStrategy sendStrategy;

	private final Deque<T> deque = new ArrayDeque<>();
	private final InputPort<T> inputPort = this.createInputPort();
	private final InputPort<Trigger> triggerInputPort = this.createInputPort();
	private final OutputPort<T> outputPort = this.createOutputPort();

	public ForwardOnTriggerStage() {
		this(SendStrategy.FIFO);
	}

	public ForwardOnTriggerStage(final SendStrategy sendStrategy) {
		this.sendStrategy = sendStrategy;
	}

	@Override
	protected void execute() {
		final T element = this.getInputPort().receive();
		if (element != null) {
			this.handleElement(element);
		}
		final Trigger trigger = this.getTriggerInputPort().receive();
		if (trigger != null) {
			this.handleTrigger();
		}
	}

	private void handleElement(final T element) {
		this.deque.addLast(element);
	}

	private void handleTrigger() {
		while (this.deque.size() > 0) {
			final T element;
			switch (this.sendStrategy) {
			case FIFO:
				element = this.deque.removeFirst();
				break;
			case LIFO:
				element = this.deque.removeLast();
				break;
			default:
				throw new IllegalStateException();
			}
			this.getOutputPort().send(element);
		}
	}

	public InputPort<Trigger> getTriggerInputPort() {
		return this.triggerInputPort;
	}

	@Override
	public InputPort<T> getInputPort() {
		return this.inputPort;
	}

	@Override
	public OutputPort<T> getOutputPort() {
		return this.outputPort;
	}

	/**
	 * @author Sören Henning
	 *
	 * @since 1.14
	 */
	public static enum SendStrategy {
		FIFO, LIFO;
	}

}
