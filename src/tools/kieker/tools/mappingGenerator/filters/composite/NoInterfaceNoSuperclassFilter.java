/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.tools.mappingGenerator.filters.composite;

import java.lang.reflect.Method;

import kieker.tools.mappingGenerator.IMethodFilter;
import kieker.tools.mappingGenerator.filters.basic.NoInterfacesFilter;
import kieker.tools.mappingGenerator.filters.basic.NoSuperclassMethodsFilter;

/**
 * Exclude Interface and superclass methods from output
 * 
 * @author Robert von Massow
 * 
 */
public class NoInterfaceNoSuperclassFilter implements IMethodFilter {

	private final IMethodFilter f1 = new NoInterfacesFilter();
	private final IMethodFilter f2 = new NoSuperclassMethodsFilter();

	@Override
	public boolean accept(final Method m, final Class<?> c) {
		return this.f1.accept(m, c) && this.f2.accept(m, c);
	}

}
