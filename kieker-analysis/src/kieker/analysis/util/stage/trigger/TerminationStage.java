package kieker.analysis.util.stage.trigger;

import teetime.framework.AbstractProducerStage;

public class TerminationStage<O> extends AbstractProducerStage<O> {

	private O value;

	public TerminationStage(O value) {
		this.value = value;
	}
	
	@Override
	protected void execute() throws Exception {
		this.workCompleted();
	}

	@Override
	protected void onTerminating() {
		this.outputPort.send(value);
		super.onTerminating();
	}
}
