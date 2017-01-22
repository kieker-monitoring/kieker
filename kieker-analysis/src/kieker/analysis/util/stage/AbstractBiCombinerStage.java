package kieker.analysis.util.stage;

import java.util.LinkedList;
import java.util.Queue;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;

public abstract class AbstractBiCombinerStage<I, J> extends AbstractStage {

	protected final InputPort<I> inputPort1 = this.createInputPort();
	protected final InputPort<J> inputPort2 = this.createInputPort();

	private final Queue<I> elements1 = new LinkedList<>();
	private final Queue<J> elements2 = new LinkedList<>();

	public final InputPort<I> getInputPort1() {
		return this.inputPort1;
	}

	public final InputPort<J> getInputPort2() {
		return this.inputPort2;
	}

	@Override
	protected void execute() {

		final I element1 = this.getInputPort1().receive();
		if (element1 != null) {
			elements1.add(element1);
		}
		final J element2 = this.getInputPort2().receive();
		if (element2 != null) {
			elements2.add(element2);
		}

		if (elements1.size() > 0 && elements2.size() > 0) {
			this.combine(elements1.poll(), elements2.poll());
		}
	}

	protected abstract void combine(final I element1, final J element2);

}
