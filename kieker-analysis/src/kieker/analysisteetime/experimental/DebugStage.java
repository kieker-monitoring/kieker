package kieker.analysisteetime.experimental;

import teetime.stage.basic.AbstractFilter;

/**
 * A simple stage that can be used to place breakpoints to debugging.
 *
 * @author S�ren Henning
 */
public class DebugStage<T> extends AbstractFilter<T> {

	@Override
	protected void execute(final T element) {
		this.outputPort.send(element);
	}

}
