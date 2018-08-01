/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * Specific subtype for the root execution container.
 * 
 * @author Holger Knoche
 * 
 * @since 1.6
 */
public class RootExecutionContainer extends ExecutionContainer {

	/**
	 * Creates a new root execution container.
	 */
	public RootExecutionContainer() {
		super(AbstractSystemSubRepository.ROOT_ELEMENT_ID, null, SystemModelRepository.ROOT_NODE_LABEL);
	}

	@Override
	public boolean isRootContainer() {
		return true;
	}
}
