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

package kieker.tools.trace.analysis;

/**
 * Externalized Strings from {@link TraceAnalysisTool}.
 *
 * @author Robert von Massow, Andre van Hoorn
 *
 * @since 1.2
 */
public final class Constants {

	/** Command for the input directories containing monitoring records. */
	public static final String CMD_OPT_NAME_INPUTDIRS = "inputdirs";
	/** Command for the output directories. */
	public static final String CMD_OPT_NAME_OUTPUTDIR = "outputdir";
	public static final String CMD_OPT_NAME_OUTPUTFNPREFIX = "output-filename-prefix";
	public static final String CMD_OPT_NAME_SELECTTRACES = "select-traces";
	public static final String CMD_OPT_NAME_FILTERTRACES = "filter-traces";
	/** Command whether to use short labels or not. */
	public static final String CMD_OPT_NAME_SHORTLABELS = "short-labels";
	public static final String CMD_OPT_NAME_IGNORE_ASSUMED = "ignore-assumed-calls";
	/** Command whether to include self loops or not. */
	public static final String CMD_OPT_NAME_INCLUDESELFLOOPS = "include-self-loops";
	/** Command whether to ignore invalid traces or not. */
	public static final String CMD_OPT_NAME_IGNOREINVALIDTRACES = "ignore-invalid-traces";
	/** Command whether to repair traces with missing AfterEvents (e.g. because of software crash) or not. */
	public static final String CMD_OPT_NAME_REPAIR_EVENT_BASED_TRACES = "repair-event-based-traces";
	/** Command whether to plot deployment sequence diagrams or not. */
	public static final String CMD_OPT_NAME_TASK_PLOTALLOCATIONSEQDS = "plot-Deployment-Sequence-Diagrams";
	/** Command whether to plot assembly sequence diagrams or not. */
	public static final String CMD_OPT_NAME_TASK_PLOTASSEMBLYSEQDS = "plot-Assembly-Sequence-Diagrams";
	/** Command whether to plot deployment component dependency graphs or not. */
	public static final String CMD_OPT_NAME_TASK_PLOTALLOCATIONCOMPONENTDEPG = "plot-Deployment-Component-Dependency-Graph";
	/** Command whether to plot assembly component dependency graphs or not. */
	public static final String CMD_OPT_NAME_TASK_PLOTASSEMBLYCOMPONENTDEPG = "plot-Assembly-Component-Dependency-Graph";
	/** Command whether to plot container dependency graphs or not. */
	public static final String CMD_OPT_NAME_TASK_PLOTCONTAINERDEPG = "plot-Container-Dependency-Graph";
	/** Command whether to plot deployment operation dependency graphs or not. */
	public static final String CMD_OPT_NAME_TASK_PLOTALLOCATIONOPERATIONDEPG = "plot-Deployment-Operation-Dependency-Graph";
	/** Command whether to plot assembly operation dependency graphs or not. */
	public static final String CMD_OPT_NAME_TASK_PLOTASSEMBLYOPERATIONDEPG = "plot-Assembly-Operation-Dependency-Graph";
	/** Command whether to plot aggregated deployment call trees or not. */
	public static final String CMD_OPT_NAME_TASK_PLOTAGGREGATEDALLOCATIONCALLTREE = "plot-Aggregated-Deployment-Call-Tree";
	/** Command whether to plot aggregated assembly call trees or not. */
	public static final String CMD_OPT_NAME_TASK_PLOTAGGREGATEDASSEMBLYCALLTREE = "plot-Aggregated-Assembly-Call-Tree";
	/** Command whether to plot call trees or not. */
	public static final String CMD_OPT_NAME_TASK_PLOTCALLTREES = "plot-Call-Trees";
	/** Command whether to print message traces or not. */
	public static final String CMD_OPT_NAME_TASK_PRINTMSGTRACES = "print-Message-Traces";
	/** Command whether to print execution traces or not. */
	public static final String CMD_OPT_NAME_TASK_PRINTEXECTRACES = "print-Execution-Traces";
	/** Command whether to print the system model or not. */
	public static final String CMD_OPT_NAME_TASK_PRINTSYSTEMMODEL = "print-System-Model";
	/** Command whether to print invalid execution traces or not. */
	public static final String CMD_OPT_NAME_TASK_PRINTINVALIDEXECTRACES = "print-invalid-Execution-Traces";
	public static final String CMD_OPT_NAME_TASK_ALLOCATIONEQUIVCLASSREPORT = "print-Deployment-Equivalence-Classes";
	public static final String CMD_OPT_NAME_TASK_ASSEMBLYEQUIVCLASSREPORT = "print-Assembly-Equivalence-Classes";
	public static final String CMD_OPT_NAME_MAXTRACEDURATION = "max-trace-duration";
	public static final String CMD_OPT_NAME_IGNOREEXECUTIONSBEFOREDATE = "ignore-executions-before-date";
	public static final String CMD_OPT_NAME_IGNOREEXECUTIONSAFTERDATE = "ignore-executions-after-date";

	/** The prefix for the call tree files. */
	public static final String CALL_TREE_FN_PREFIX = "callTree";
	/** The name prefix for the message traces files. */
	public static final String MESSAGE_TRACES_FN_PREFIX = "messageTraces";
	/** The name prefix for the execution traces files. */
	public static final String EXECUTION_TRACES_FN_PREFIX = "executionTraces";
	/** The name prefix for the invalid traces files. */
	public static final String INVALID_TRACES_FN_PREFIX = "invalidTraceArtifacts";
	public static final String TRACE_ALLOCATION_EQUIV_CLASSES_FN_PREFIX = "traceDeploymentEquivClasses";
	public static final String TRACE_ASSEMBLY_EQUIV_CLASSES_FN_PREFIX = "traceAssemblyEquivClasses";

	/** The name of the component for the execution trace reconstruction. */
	public static final String EXEC_TRACE_RECONSTR_COMPONENT_NAME = "Execution record transformation";
	/** The name of the component for the trace reconstruction of execution records. */
	public static final String TRACERECONSTR_COMPONENT_NAME = "Trace reconstruction (execution records -> execution traces)";
	/** The name of the component for the trace equivalence class filtering (deployment mode). */
	public static final String TRACEALLOCATIONEQUIVCLASS_COMPONENT_NAME = "Trace equivalence class filter (deployment mode)";
	/** The name of the component for the trace equivalence class filtering (assembly mode). */
	public static final String TRACEASSEMBLYEQUIVCLASS_COMPONENT_NAME = "Trace equivalence class filter (assembly mode)";
	/** The name of the component for the trace reconstruction of trace event records. */
	public static final String EVENTRECORDTRACERECONSTR_COMPONENT_NAME = "Trace reconstruction (trace event records -> event record traces)";
	/** The name of the component for the trace reconstruction of event record traces. */
	public static final String EXECTRACESFROMEVENTTRACES_COMPONENT_NAME = "Trace reconstruction (event record traces -> execution traces)";

	public static final String DECORATORS_OPTION_NAME = "responseTimes-ns | responseTimes-us | responseTimes-ms | responseTimes-s> "
			+ "<responseTimeColoring threshold(ms)";
	public static final char DECORATOR_SEPARATOR = ',';

	public static final String RESPONSE_TIME_COLORING_DECORATOR_FLAG = "responseTimeColoring";
	public static final String CMD_OPT_NAME_TRACE_COLORING = "traceColoring";
	public static final String COLORING_FILE_OPTION_NAME = "color map file";

	public static final String CMD_OPT_NAME_ADD_DESCRIPTIONS = "addDescriptions";
	public static final String DESCRIPTIONS_FILE_OPTION_NAME = "descriptions file";

	/**
	 * Private constructor to avoid instantiation.
	 */
	private Constants() {}
}
