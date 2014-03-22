package kieker.panalysis.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Stage {

	protected long id;
	/**
	 * A unique logger instance per stage instance
	 */
	protected Logger logger;

	public Stage(final long id) {
		this.id = id;
		this.logger = LoggerFactory.getLogger(Long.toString(id));
	}

	public abstract void execute();
}
