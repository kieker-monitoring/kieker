package kieker.monitoring.core.controller;

import kieker.monitoring.timer.ITimeSource;

/**
 * @author Jan Waller
 */
public interface ITimeSourceController {

	/**
	 * Returns the ITimeSource used in this controller.
	 * 
	 * @return ITimeSource
	 */
	public abstract ITimeSource getTimeSource();

}
