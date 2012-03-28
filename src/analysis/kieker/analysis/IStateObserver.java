package kieker.analysis;

import kieker.analysis.AnalysisController.STATE;

/**
 * This interface can be used for observers which want to get notified about state changes of an analysis controller.
 * 
 * @author Nils Christian Ehmke
 * @version 1.0
 */
public interface IStateObserver {

	/**
	 * This method will be called for every update of the state.
	 * 
	 * @param controller
	 *            The controller which updated its state.
	 * @param state
	 *            The new state of the given controller.
	 */
	public void update(final AnalysisController controller, final STATE state);

}
