package kieker.tools.mappingGeneratorTool.filters.composite;

import java.lang.reflect.Method;
import kieker.tools.mappingGeneratorTool.MethodFilter;
import kieker.tools.mappingGeneratorTool.filters.basic.NoInterfacesFilter;
import kieker.tools.mappingGeneratorTool.filters.basic.NoSuperclassMethodsFilter;

/**
 * Exclude Interface and superclass methods from output
 * 
 * @author Robert von Massow
 * 
 */
public class NoInterfaceNoSuperclassFilter implements MethodFilter {

	private final MethodFilter f1 = new NoInterfacesFilter();
	private final MethodFilter f2 = new NoSuperclassMethodsFilter();

	public boolean accept(final Method m, final Class<?> c) {
		return f1.accept(m, c) && f2.accept(m, c);
	}

}
