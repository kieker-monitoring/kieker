/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

package kieker.model.system.model;

import kieker.model.repository.AbstractRepository;
import kieker.model.repository.SystemModelRepository;
import kieker.model.repository.TypeRepository;

/**
 * This class represents a root assembly component.
 *
 * @author Holger Knoche
 *
 * @since 1.6
 *
 */
public class RootAssemblyComponent extends AssemblyComponent {

	/**
	 * Creates a new root assembly component.
	 */
	public RootAssemblyComponent() {
		super(AbstractRepository.ROOT_ELEMENT_ID, SystemModelRepository.ROOT_NODE_LABEL, TypeRepository.ROOT_COMPONENT);
	}

	@Override
	public boolean isRootComponent() {
		return true;
	}
}
