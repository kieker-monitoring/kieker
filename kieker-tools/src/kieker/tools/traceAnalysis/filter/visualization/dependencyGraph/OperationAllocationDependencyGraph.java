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

package kieker.tools.traceAnalysis.filter.visualization.dependencyGraph;

import kieker.tools.traceAnalysis.systemModel.util.AllocationComponentOperationPair;

/**
 * This class represents operation dependency graphs on the allocation level.
 * 
 * @author Holger Knoche
 * 
 * @since 1.6
 */
public class OperationAllocationDependencyGraph extends AbstractDependencyGraph<AllocationComponentOperationPair> {

	/**
	 * Creates a new graph with the given root entity.
	 * 
	 * @param rootEntity
	 *            The root entity to use for this graph
	 */
	public OperationAllocationDependencyGraph(final AllocationComponentOperationPair rootEntity) {
		super(rootEntity);
	}

}
