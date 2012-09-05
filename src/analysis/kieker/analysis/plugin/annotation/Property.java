package kieker.analysis.plugin.annotation;

/**
 * This annotation can be used to describe a single property for a plugin or a repository.
 * 
 * @author Nils Christian Ehmke
 */
public @interface Property {

	/**
	 * The name of the property.
	 * 
	 * @return The name of the property.
	 */
	String name();

	/**
	 * The default value for the property.
	 * 
	 * @return The default value for the property.
	 */
	String defaultValue();

}
