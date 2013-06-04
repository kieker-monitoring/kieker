/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

/**
 * Specific subtype for the root execution container.
 * 
 * @author Holger Knoche
 * 
 * @since 1.6
 */
public class RootExecutionContainer extends ExecutionContainer {

	private static final String ROOT_EXECUTION_CONTAINER_NAME = "#";

	/**
	 * Creates a new root execution container.
	 */
	public RootExecutionContainer() {
		super(AbstractSystemSubRepository.ROOT_ELEMENT_ID, null, ROOT_EXECUTION_CONTAINER_NAME);
	}

	@Override
	public boolean isRootContainer() {
		return true;
	}
}
