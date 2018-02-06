package kieker.analysisteetime.experimental;

import teetime.stage.basic.AbstractFilter;

/**
 * A simple stage that can be used to place breakpoints to debugging.
 *
 * @author Sören Henning
 */
public class DebugStage<T> extends AbstractFilter<T> {

	public DebugStage() {
		super();
	}

	@Override
	protected void execute(final T element) {
		this.outputPort.send(element);
	}

}
