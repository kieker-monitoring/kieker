package kieker.panalysis.base;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

public abstract class AbstractStage<InputPort extends Enum<InputPort>> implements Stage<InputPort> {

	protected long id;
	/**
	 * A unique logger instance per stage instance
	 */
	protected Log logger;

	public AbstractStage(final long id) {
		this.id = id;
		this.logger = LogFactory.getLog(Long.toString(id));
	}

	abstract public InputPort chooseInputPort();

	final public void execute() {
		final InputPort inputPort = this.chooseInputPort();
		this.execute(inputPort);
	}
}
