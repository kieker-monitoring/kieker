package kieker.analysis.plugin.port;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This method can be used to mark plugins and to describe the corresponding output ports.
 * 
 * @author Nils Christian Ehmke
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface APlugin {

	/**
	 * The output ports which the current plugin has.
	 * 
	 * @return The output ports of this annotation.
	 */
	AOutputPort[] outputPorts() default {};

}
