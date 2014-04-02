/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.traceAnalysis.filter.visualization.util;

import kieker.common.util.signature.Signature;
import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.ExecutionContainer;
import kieker.tools.traceAnalysis.systemModel.Operation;

/**
 * @author Christian Wulf
 * 
 * @since 1.9
 */
public final class NamingConventions {

	/** The string used in the node labels for execution containers. */
	private static final String STEREOTYPE_EXECUTION_CONTAINER = "<<execution container>>";
	/** The string used in the node labels for assembly components. */
	private static final String STEREOTYPE_ASSEMBLY_COMPONENT = "<<assembly component>>";
	/** The string used in the node labels for deployment components. */
	private static final String STEREOTYPE_ALLOCATION_COMPONENT = "<<deployment component>>";

	private static final String NODE_ID_PREFIX = "depNode_";
	private static final String COMPONENT_NODE_ID_PREFIX = "component_";
	private static final String GRAPH_ID_PREFIX = "graph_";
	private static int index;

	private NamingConventions() {
		// utility class
	}

	public static String createOperationSignature(final Operation operation) {
		final StringBuilder builder = new StringBuilder();
		final Signature signature = operation.getSignature();

		builder.append(signature.getName());
		builder.append('(');

		final String[] parameterTypes = signature.getParamTypeList();
		if (parameterTypes.length > 0) { // // parameterTypes cannot be null (getParamTypeList never returns null)
			builder.append("..");
		}
		builder.append(')');

		return builder.toString();
	}

	public static String createNodeId(final int nodeId) {
		return NODE_ID_PREFIX + nodeId;
	}

	public static String createComponentId(final int nodeId) {
		return COMPONENT_NODE_ID_PREFIX + nodeId;
	}

	public static String createContainerNodeLabel(final ExecutionContainer container) {
		return STEREOTYPE_EXECUTION_CONTAINER + "\\n" + container.getName();
	}

	public static String createAllocationComponentNodeLabel(final AllocationComponent component, final boolean useShortLabels) {
		final StringBuilder builder = new StringBuilder();

		builder.append(STEREOTYPE_ALLOCATION_COMPONENT).append("\\n");
		builder.append(component.getAssemblyComponent().getName()).append(':');

		if (useShortLabels) {
			builder.append("..");
		} else {
			builder.append(component.getAssemblyComponent().getType().getPackageName()).append('.');
		}

		builder.append(component.getAssemblyComponent().getType().getTypeName());

		return builder.toString();
	}

	public static String createSubgraphId() {
		return GRAPH_ID_PREFIX + (index++);
	}
}
