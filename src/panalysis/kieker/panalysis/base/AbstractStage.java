package kieker.panalysis.base;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

public abstract class AbstractStage<InputPort extends Enum<InputPort>> implements Stage<InputPort> {

	protected int id;
	/**
	 * A unique logger instance per stage instance
	 */
	protected Log logger;

	public AbstractStage() {
		this.logger = LogFactory.getLog(Long.toString(this.id));
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

}
