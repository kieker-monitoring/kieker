package kieker.analysisteetime.util.stage.trigger;

import java.util.ArrayDeque;
import java.util.Deque;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import teetime.stage.basic.ITransformation;

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
			handleElement(element);
		}
		final Trigger trigger = this.getTriggerInputPort().receive();
		if (trigger != null) {
			handleTrigger(trigger);
		}
	}

	private void handleElement(final T element) {
		this.deque.addLast(element);
	}

	private void handleTrigger(final Trigger trigger) {
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

	public static enum SendStrategy {
		FIFO, LIFO;
	}

}
