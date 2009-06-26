package kieker.common.nameIdMapper.mappingGeneratorTool.filters.basic;

import java.lang.reflect.Method;
import kieker.common.nameIdMapper.mappingGeneratorTool.MethodFilter;

/**
 * 
 * Excludes superclass methods from output
 * 
 * @author Robert von Massow
 * 
 */
public class NoSuperclassMethodsFilter implements MethodFilter {

	@Override
	public boolean accept(final Method m, final Class<?> c) {
		return m.getDeclaringClass().equals(c);
	}

}
