package kieker.common.tools.mappingGeneratorTool.filters.basic;

import java.lang.reflect.Method;
import kieker.common.tools.mappingGeneratorTool.MethodFilter;

/**
 * Excludes Interface methods from output
 * 
 * @author skomp
 * 
 */
public class NoInterfacesFilter implements MethodFilter {

	@Override
	public boolean accept(final Method m, final Class<?> c) {
		return !c.isInterface();
	}

}
