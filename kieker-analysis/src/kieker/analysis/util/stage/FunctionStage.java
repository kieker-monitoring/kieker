package kieker.analysis.util.stage;

import java.util.function.Function;

import teetime.stage.basic.AbstractTransformation;

public class FunctionStage<I, O> extends AbstractTransformation<I, O> {

	private Function<I, O> function;

	public FunctionStage(final Function<I, O> function) {
		super();
		this.function = function;
	}

	public Function<I, O> getFunction() {
		return function;
	}

	public void setFunction(final Function<I, O> function) {
		this.function = function;
	}

	@Override
	protected void execute(final I element) {
		final O transformedElement = function.apply(element);
		this.getOutputPort().send(transformedElement);
	}

}
