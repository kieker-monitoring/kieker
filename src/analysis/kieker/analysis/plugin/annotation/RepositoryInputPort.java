package kieker.analysis.plugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface RepositoryInputPort {

	/**
	 * The human-readable description of this port.
	 * 
	 * @return The description for this port.
	 */
	String description() default "Repository Input Port";

	/**
	 * The name which is used to identify this port. It should be unique within the class.
	 * 
	 * @return The name of this port.
	 */
	String name();

	/**
	 * The event types which are used for this port. If this is empty, everything can be received through the port.
	 * 
	 * @return The event types for this class.
	 */
	Class<?>[] eventTypes() default {};

	Class<?> returnType() default Void.class;
}
