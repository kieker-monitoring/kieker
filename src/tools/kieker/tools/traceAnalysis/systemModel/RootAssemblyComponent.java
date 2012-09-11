/***************************************************************************
 * Copyright 2012 by
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

package kieker.tools.traceAnalysis.systemModel;

import kieker.tools.traceAnalysis.systemModel.repository.AbstractSystemSubRepository;
import kieker.tools.traceAnalysis.systemModel.repository.TypeRepository;

/**
 * This class represents a root assembly component.
 * 
 * @author Holger Knoche
 * 
 */
public class RootAssemblyComponent extends AssemblyComponent {

	private static final String ROOT_COMPONENT_NAME = "$";

	/**
	 * Creates a new root assembly component.
	 */
	public RootAssemblyComponent() {
		super(AbstractSystemSubRepository.ROOT_ELEMENT_ID, ROOT_COMPONENT_NAME, TypeRepository.ROOT_COMPONENT);
	}

	@Override
	public boolean isRootComponent() {
		return true;
	}
}
