/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.traceAnalysis;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

import kieker.tools.AbstractCommandLineTool;

/**
 * Externalized Strings from {@link TraceAnalysisTool}.
 * 
 * @author Robert von Massow, Andre van Hoorn
 * 
 * @since 1.2
 */
@SuppressWarnings({ "static-access", "static" })
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
	/** The suffix for dot files. */
	public static final String DOT_FILE_SUFFIX = ".dot";
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
	/** The format pattern which is used to print the date. */
	public static final String DATE_FORMAT_PATTERN = "yyyyMMdd'-'HHmmss";
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
	public static final Options CMDL_OPTIONS = new Options();
	public static final List<Option> SORTED_OPTION_LIST = new CopyOnWriteArrayList<Option>();

	public static final String DECORATORS_OPTION_NAME = "responseTimes-ns | responseTimes-us | responseTimes-ms | responseTimes-s> "
			+ "<responseTimeColoring threshold(ms)";
	public static final char DECORATOR_SEPARATOR = ',';
	public static final String RESPONSE_TIME_DECORATOR_FLAG_NS = "responseTimes-ns";
	public static final String RESPONSE_TIME_DECORATOR_FLAG_US = "responseTimes-us";
	public static final String RESPONSE_TIME_DECORATOR_FLAG_MS = "responseTimes-ms";
	public static final String RESPONSE_TIME_DECORATOR_FLAG_S = "responseTimes-s";
	public static final String RESPONSE_TIME_COLORING_DECORATOR_FLAG = "responseTimeColoring";

	public static final String CMD_OPT_NAME_TRACE_COLORING = "traceColoring";
	public static final String COLORING_FILE_OPTION_NAME = "color map file";

	public static final String CMD_OPT_NAME_ADD_DESCRIPTIONS = "addDescriptions";
	public static final String DESCRIPTIONS_FILE_OPTION_NAME = "descriptions file";

	static {
		// the following two options used to be required. However, then --help not working
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_INPUTDIRS).withArgName("dir1 ... dirN").hasArgs().isRequired(false)
				.withDescription("Log directories to read data from").withValueSeparator('=').create("i"));
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_OUTPUTDIR).withArgName("dir").hasArg(true).isRequired(false)
				.withDescription("Directory for the generated file(s)").withValueSeparator('=').create("o"));
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_OUTPUTFNPREFIX).withArgName("prefix").hasArg(true).isRequired(false)
				.withDescription("Prefix for output filenames\n").withValueSeparator('=').create("p"));
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(AbstractCommandLineTool.CMD_OPT_NAME_VERBOSE_LONG).hasArg(false)
				.withDescription("Verbosely list used parameters and processed traces").create(AbstractCommandLineTool.CMD_OPT_NAME_VERBOSE_SHORT));
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(AbstractCommandLineTool.CMD_OPT_NAME_DEBUG_LONG).hasArg(false)
				.withDescription("prints additional debug information").create(AbstractCommandLineTool.CMD_OPT_NAME_DEBUG_SHORT));
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PLOTALLOCATIONSEQDS).hasArg(false)
				.withDescription("Generate and store deployment-level sequence diagrams (.pic)").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PLOTASSEMBLYSEQDS).hasArg(false)
				.withDescription("Generate and store assembly-level sequence diagrams (.pic)").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PLOTALLOCATIONCOMPONENTDEPG)
				.withArgName(DECORATORS_OPTION_NAME)
				.hasArg(true).hasOptionalArgs().withValueSeparator(DECORATOR_SEPARATOR)
				.withDescription("Generate and store a deployment-level component dependency graph (.dot)").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PLOTASSEMBLYCOMPONENTDEPG)
				.withArgName(DECORATORS_OPTION_NAME)
				.hasArg(true).hasOptionalArgs().withValueSeparator(DECORATOR_SEPARATOR)
				.withDescription("Generate and store an assembly-level component dependency graph (.dot)").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PLOTCONTAINERDEPG).hasArg(false)
				.withDescription("Generate and store a container dependency graph (.dot file)").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PLOTALLOCATIONOPERATIONDEPG)
				.withArgName(DECORATORS_OPTION_NAME)
				.hasArg(true).hasOptionalArgs().withValueSeparator(DECORATOR_SEPARATOR)
				.withDescription("Generate and store a deployment-level operation dependency graph (.dot)").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PLOTASSEMBLYOPERATIONDEPG)
				.withArgName(DECORATORS_OPTION_NAME)
				.hasArg(true).hasOptionalArgs().withValueSeparator(DECORATOR_SEPARATOR)
				.withDescription("Generate and store an assembly-level operation dependency graph (.dot)").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PLOTAGGREGATEDALLOCATIONCALLTREE).hasArg(false)
				.withDescription("Generate and store an aggregated deployment-level call tree (.dot)").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PLOTAGGREGATEDASSEMBLYCALLTREE).hasArg(false)
				.withDescription("Generate and store an aggregated assembly-level call tree (.dot)").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PLOTCALLTREES).hasArg(false)
				.withDescription("Generate and store call trees for the selected traces (.dot)").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PRINTMSGTRACES).hasArg(false)
				.withDescription("Save message trace representations of valid traces (.txt)").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PRINTEXECTRACES).hasArg(false)
				.withDescription("Save execution trace representations of valid traces (.txt)").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PRINTINVALIDEXECTRACES).hasArg(false)
				.withDescription("Save a execution trace representations of invalid trace artifacts (.txt)").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PRINTSYSTEMMODEL).hasArg(false)
				.withDescription("Save a representation of the internal system model (.html)").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_ALLOCATIONEQUIVCLASSREPORT).hasArg(false)
				.withDescription("Output an overview about the deployment-level trace equivalence classes").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_ASSEMBLYEQUIVCLASSREPORT).hasArg(false)
				.withDescription("Output an overview about the assembly-level trace equivalence classes").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_SELECTTRACES).withArgName("id0 ... idn").hasArgs().isRequired(false)
				.withDescription("Consider only the traces identified by the list of trace IDs. Defaults to all traces.").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_FILTERTRACES).withArgName("id0 ... idn").hasArgs().isRequired(false)
				.withDescription("Consider only the traces not identified by the list of trace IDs. Defaults to no traces.").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_IGNOREINVALIDTRACES).hasArg(false).isRequired(false)
				.withDescription("If selected, the execution aborts on the occurence of an invalid trace.").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_REPAIR_EVENT_BASED_TRACES).hasArg(false).isRequired(false)
				.withDescription("If selected, BeforeEvents with missing AfterEvents e.g. because of software crash will be repaired.").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_MAXTRACEDURATION).withArgName("duration in ms").hasArg().isRequired(false)
				.withDescription("Threshold (in ms) after which incomplete traces become invalid. Defaults to 600,000 (i.e, 10 minutes).").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_IGNOREEXECUTIONSBEFOREDATE)
				.withArgName(TraceAnalysisTool.DATE_FORMAT_PATTERN_CMD_USAGE_HELP).hasArg().isRequired(false)
				.withDescription("Executions starting before this date (UTC timezone) or monitoring timestamp are ignored.").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_IGNOREEXECUTIONSAFTERDATE)
				.withArgName(TraceAnalysisTool.DATE_FORMAT_PATTERN_CMD_USAGE_HELP).hasArg().isRequired(false)
				.withDescription("Executions ending after this date (UTC timezone) or monitoring timestamp  are ignored.").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_SHORTLABELS).hasArg(false).isRequired(false)
				.withDescription("If selected, abbreviated labels (e.g., package names) are used in the visualizations.").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_INCLUDESELFLOOPS).hasArg(false).isRequired(false)
				.withDescription("If selected, self-loops are included in the visualizations.").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_IGNORE_ASSUMED).hasArg(false).isRequired(false)
				.withDescription("If selected, assumed calls are visualized just as regular calls.").create());
		SORTED_OPTION_LIST
				.add(OptionBuilder
						.withLongOpt(CMD_OPT_NAME_TRACE_COLORING)
						.hasArg()
						.isRequired(false)
						.withDescription(
								"Color traces according to the given color map given as a properties file (key: trace ID, value: color in hex format, e.g., 0xff0000 for red; use trace ID 'default' to specify the default color)") // NOCS
						.withArgName(COLORING_FILE_OPTION_NAME).create());
		SORTED_OPTION_LIST
				.add(OptionBuilder
						.withLongOpt(CMD_OPT_NAME_ADD_DESCRIPTIONS)
						.hasArg()
						.isRequired(false)
						.withDescription(
								"Adds descriptions to elements according to the given file as a properties file (key: component ID, e.g., @1; value: description)")
						.withArgName(DESCRIPTIONS_FILE_OPTION_NAME).create());

		for (final Option o : SORTED_OPTION_LIST) {
			CMDL_OPTIONS.addOption(o);
		}
	}

	/**
	 * Private constructor to avoid instantiation.
	 */
	private Constants() {}

	public static String stringArrToStringList(final String[] strs) {
		final StringBuilder strB = new StringBuilder();
		boolean first = true;
		for (final String s : strs) {
			if (!first) {
				strB.append(", ");
			} else {
				first = false;
			}
			strB.append(s);
		}
		return strB.toString();
	}
}
