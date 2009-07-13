package kieker.common.tools.mappingGeneratorTool.filters.basic;

import java.lang.reflect.Method;
import kieker.common.tools.mappingGeneratorTool.MethodFilter;

/**
 * 
 * Excludes superclass methods from output
 * 
 * @author Robert von Massow
 * 
 */
public class NoSuperclassMethodsFilter implements MethodFilter {

	public boolean accept(final Method m, final Class<?> c) {
		return m.getDeclaringClass().equals(c);
	}

}
