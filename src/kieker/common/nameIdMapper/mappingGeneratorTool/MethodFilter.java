package kieker.common.nameIdMapper.mappingGeneratorTool;

import java.lang.reflect.Method;

/**
 * Implementing classes can be used to filter the methods found in the analyzed
 * classes.
 * 
 * @author Robert von Massow
 * 
 */
public interface MethodFilter {

	/**
	 * This method is called from within the MethodExtractor to filter the
	 * output methods.
	 * 
	 * @param m
	 *            the method object
	 * @param c
	 *            the analyzed class
	 * @return true if method is accepted
	 */
	public abstract boolean accept(Method m, Class<?> c);
}
