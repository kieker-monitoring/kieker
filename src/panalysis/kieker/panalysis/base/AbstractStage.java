package kieker.panalysis.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractStage<InputPort extends Enum<InputPort>> implements Stage<InputPort> {

	protected long id;
	/**
	 * A unique logger instance per stage instance
	 */
	protected Logger logger;

	public AbstractStage(final long id) {
		this.id = id;
		this.logger = LoggerFactory.getLogger(Long.toString(id));
	}

	abstract public InputPort chooseInputPort();

	final public void execute() {
		final InputPort inputPort = this.chooseInputPort();
		this.execute(inputPort);
	}
}
