package kieker.monitoring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Matthias Rohr, Thilo Focke
 * 
 *         History:
 *         2008/01/14: Refactoring for the first release of
 *         Kieker and publication under an open source licence
 *         2007/03/05: New Prototype
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OperationExecutionMonitoringProbe {
	// String context();
}
