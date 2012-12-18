package kieker.analysis;

import kieker.analysis.AnalysisController.STATE;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.model.analysisMetaModel.MIProject;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.7
 */
public interface IProjectContext {

	/**
	 * Delivers the value for the given (global) property within the analysis.
	 * 
	 * @return The value for the given property if it exists, null otherwise.
	 * 
	 * @since 1.7
	 */
	public String getProperty(final String key);

	/**
	 * This method delivers the current configuration of this instance as an instance of <code>MIProject</code>.
	 * 
	 * @return A filled meta model instance.
	 * @throws AnalysisConfigurationException
	 *             If the current configuration is somehow invalid.
	 */
	public MIProject getCurrentConfiguration() throws AnalysisConfigurationException;

	/**
	 * Delivers the current name of the project.
	 * 
	 * @return The current project name.
	 */
	public String getProjectName();

	/**
	 * Delivers the current state of the analysis controller.
	 * 
	 * @return The current state.
	 */
	public STATE getState();
}
