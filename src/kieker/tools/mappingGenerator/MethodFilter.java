package kieker.tools.mappingGenerator;

import java.lang.reflect.Method;

/**
 * Implementing classes can be used to filter the methods found in the analyzed
 * classes.
 * 
 * @author Robert von Massow
 * 
 */
public interface MethodFilter {
    
	public abstract boolean accept(Method m, Class<?> c);
}
