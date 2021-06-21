/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.trace.analysis.filter.visualization;

/**
 * @author Reiner Jung
 *
 * @since 1.15
 */
public final class VisualizationConstants {

	/** The suffix for dot files. */
	public static final String DOT_FILE_SUFFIX = ".dot";

	/** The name of the component for the trace reconstruction of event records. */
	public static final String EXECEVENTRACESFROMEVENTTRACES_COMPONENT_NAME = "Trace reconstruction (event records -> event record traces)";
	public static final String PRINTMSGTRACE_COMPONENT_NAME = "Print message traces";
	public static final String PRINTEXECTRACE_COMPONENT_NAME = "Print execution traces";
	public static final String PRINTINVALIDEXECTRACE_COMPONENT_NAME = "Print invalid execution traces";
	public static final String PLOTALLOCATIONCOMPONENTDEPGRAPH_COMPONENT_NAME = "Component dependency graph (deployment level)";
	public static final String PLOTASSEMBLYCOMPONENTDEPGRAPH_COMPONENT_NAME = "Component dependency graph (assembly level)";
	public static final String PLOTCONTAINERDEPGRAPH_COMPONENT_NAME = "Container dependency graph";
	public static final String PLOTALLOCATIONOPERATIONDEPGRAPH_COMPONENT_NAME = "Operation dependency graph (deployment level)";
	public static final String PLOTASSEMBLYOPERATIONDEPGRAPH_COMPONENT_NAME = "Operation dependency graph (assembly level)";
	public static final String PLOTALLOCATIONSEQDIAGR_COMPONENT_NAME = "Sequence diagrams (deployment level)";
	public static final String PLOTASSEMBLYSEQDIAGR_COMPONENT_NAME = "Sequence diagrams (assembly level)";
	public static final String PLOTAGGREGATEDALLOCATIONCALLTREE_COMPONENT_NAME = "Aggregated call tree (deployment level)";
	public static final String PLOTAGGREGATEDASSEMBLYCALLTREE_COMPONENT_NAME = "Aggregated call tree (assembly level)";
	public static final String PLOTCALLTREE_COMPONENT_NAME = "Trace call trees";

	// TODO this should become an enumeration
	/** Node decorator flags. */
	public static final String RESPONSE_TIME_DECORATOR_FLAG_NS = "responseTimes-ns";
	public static final String RESPONSE_TIME_DECORATOR_FLAG_US = "responseTimes-us";
	public static final String RESPONSE_TIME_DECORATOR_FLAG_MS = "responseTimes-ms";
	public static final String RESPONSE_TIME_DECORATOR_FLAG_S = "responseTimes-s";

	/** The prefix for the files of the allocation sequence diagram. */
	public static final String ALLOCATION_SEQUENCE_DIAGRAM_FN_PREFIX = "deploymentSequenceDiagram";
	/** The prefix for the files of the assembly sequence diagram. */
	public static final String ASSEMBLY_SEQUENCE_DIAGRAM_FN_PREFIX = "assemblySequenceDiagram";
	/** The prefix for the files of the allocation component dependency graphs. */
	public static final String ALLOCATION_COMPONENT_DEPENDENCY_GRAPH_FN_PREFIX = "deploymentComponentDependencyGraph";
	/** The prefix for the files of the assembly component dependency graphs. */
	public static final String ASSEMBLY_COMPONENT_DEPENDENCY_GRAPH_FN_PREFIX = "assemblyComponentDependencyGraph";
	/** The prefix for the files of the container dependency graphs. */
	public static final String CONTAINER_DEPENDENCY_GRAPH_FN_PREFIX = "containerDependencyGraph";
	/** The prefix for the files of the allocation operation dependency graphs. */
	public static final String ALLOCATION_OPERATION_DEPENDENCY_GRAPH_FN_PREFIX = "deploymentOperationDependencyGraph";
	public static final String ASSEMBLY_OPERATION_DEPENDENCY_GRAPH_FN_PREFIX = "assemblyOperationDependencyGraph";
	public static final String AGGREGATED_ALLOCATION_CALL_TREE_FN_PREFIX = "aggregatedDeploymentCallTree";
	public static final String AGGREGATED_ASSEMBLY_CALL_TREE_FN_PREFIX = "aggregatedAssemblyCallTree";

	private VisualizationConstants() {
		// utility class
	}

}
