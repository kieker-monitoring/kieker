package kieker.analysis.analysisComponent;

import kieker.common.configuration.Configuration;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.7
 */
public interface IAnalysisComponent {

	/**
	 * This method should deliver a {@code Configuration} object containing the current configuration of this instance. In other words: The constructor should be
	 * able to use the given object to initialize a new instance of this class with the same intern properties.
	 * 
	 * @return A completely filled configuration object.
	 */
	public abstract Configuration getCurrentConfiguration();

	/**
	 * This method delivers the current name of this component instance. The name does not have to be unique.
	 * 
	 * @return The current name of the component instance.
	 */
	public abstract String getName();

}
