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

package kieker.tools.traceAnalysis;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

/**
 * Externalized Strings from {@link TraceAnalysisTool}.
 * 
 * @author Robert von Massow
 */
@SuppressWarnings({ "static-access", "static" })
public final class Constants {

	public static final String CMD_OPT_NAME_INPUTDIRS = "inputdirs";
	public static final String CMD_OPT_NAME_OUTPUTDIR = "outputdir";
	public static final String CMD_OPT_NAME_OUTPUTFNPREFIX = "output-filename-prefix";
	public static final String CMD_OPT_NAME_SELECTTRACES = "select-traces";
	public static final String CMD_OPT_NAME_SHORTLABELS = "short-labels";
	public static final String CMD_OPT_NAME_INCLUDESELFLOOPS = "include-self-loops";
	public static final String CMD_OPT_NAME_IGNOREINVALIDTRACES = "ignore-invalid-traces";
	public static final String CMD_OPT_NAME_TASK_PLOTALLOCATIONSEQDS = "plot-Deployment-Sequence-Diagrams";
	public static final String CMD_OPT_NAME_TASK_PLOTASSEMBLYSEQDS = "plot-Assembly-Sequence-Diagrams";
	public static final String CMD_OPT_NAME_TASK_PLOTALLOCATIONCOMPONENTDEPG = "plot-Deployment-Component-Dependency-Graph";
	public static final String CMD_OPT_NAME_TASK_PLOTASSEMBLYCOMPONENTDEPG = "plot-Assembly-Component-Dependency-Graph";
	public static final String CMD_OPT_NAME_TASK_PLOTCONTAINERDEPG = "plot-Container-Dependency-Graph";
	public static final String CMD_OPT_NAME_TASK_PLOTALLOCATIONOPERATIONDEPG = "plot-Deployment-Operation-Dependency-Graph";
	public static final String CMD_OPT_NAME_TASK_PLOTASSEMBLYOPERATIONDEPG = "plot-Assembly-Operation-Dependency-Graph";
	public static final String CMD_OPT_NAME_TASK_PLOTAGGREGATEDALLOCATIONCALLTREE = "plot-Aggregated-Deployment-Call-Tree";
	public static final String CMD_OPT_NAME_TASK_PLOTAGGREGATEDASSEMBLYCALLTREE = "plot-Aggregated-Assembly-Call-Tree";
	public static final String CMD_OPT_NAME_TASK_PLOTCALLTREES = "plot-Call-Trees";
	public static final String CMD_OPT_NAME_TASK_PRINTMSGTRACES = "print-Message-Traces";
	public static final String CMD_OPT_NAME_TASK_PRINTEXECTRACES = "print-Execution-Traces";
	public static final String CMD_OPT_NAME_TASK_PRINTINVALIDEXECTRACES = "print-invalid-Execution-Traces";
	public static final String CMD_OPT_NAME_TASK_ALLOCATIONEQUIVCLASSREPORT = "print-Deployment-Equivalence-Classes";
	public static final String CMD_OPT_NAME_TASK_ASSEMBLYEQUIVCLASSREPORT = "print-Assembly-Equivalence-Classes";
	public static final String CMD_OPT_NAME_MAXTRACEDURATION = "max-trace-duration";
	public static final String CMD_OPT_NAME_IGNOREEXECUTIONSBEFOREDATE = "ignore-executions-before-date";
	public static final String CMD_OPT_NAME_IGNOREEXECUTIONSAFTERDATE = "ignore-executions-after-date";
	public static final String ALLOCATION_SEQUENCE_DIAGRAM_FN_PREFIX = "deploymentSequenceDiagram";
	public static final String ASSEMBLY_SEQUENCE_DIAGRAM_FN_PREFIX = "assemblySequenceDiagram";
	public static final String ALLOCATION_COMPONENT_DEPENDENCY_GRAPH_FN_PREFIX = "deploymentComponentDependencyGraph";
	public static final String ASSEMBLY_COMPONENT_DEPENDENCY_GRAPH_FN_PREFIX = "assemblyComponentDependencyGraph";
	public static final String CONTAINER_DEPENDENCY_GRAPH_FN_PREFIX = "containerDependencyGraph";
	public static final String ALLOCATION_OPERATION_DEPENDENCY_GRAPH_FN_PREFIX = "deploymentOperationDependencyGraph";
	public static final String ASSEMBLY_OPERATION_DEPENDENCY_GRAPH_FN_PREFIX = "assemblyOperationDependencyGraph";
	public static final String AGGREGATED_ALLOCATION_CALL_TREE_FN_PREFIX = "aggregatedDeploymentCallTree";
	public static final String AGGREGATED_ASSEMBLY_CALL_TREE_FN_PREFIX = "aggregatedAssemblyCallTree";
	public static final String DOT_FILE_SUFFIX = ".dot";
	public static final String CALL_TREE_FN_PREFIX = "callTree";
	public static final String MESSAGE_TRACES_FN_PREFIX = "messageTraces";
	public static final String EXECUTION_TRACES_FN_PREFIX = "executionTraces";
	public static final String INVALID_TRACES_FN_PREFIX = "invalidTraceArtifacts";
	public static final String TRACE_ALLOCATION_EQUIV_CLASSES_FN_PREFIX = "traceDeploymentEquivClasses";
	public static final String TRACE_ASSEMBLY_EQUIV_CLASSES_FN_PREFIX = "traceAssemblyEquivClasses";
	public static final String DATE_FORMAT_PATTERN = "yyyyMMdd'-'HHmmss";
	public static final String EXEC_TRACE_RECONSTR_COMPONENT_NAME = "Execution record transformation";
	public static final String TRACERECONSTR_COMPONENT_NAME = "Trace reconstruction (execution records -> execution traces)";
	public static final String TRACEALLOCATIONEQUIVCLASS_COMPONENT_NAME = "Trace equivalence class filter (deployment mode)";
	public static final String TRACEASSEMBLYEQUIVCLASS_COMPONENT_NAME = "Trace equivalence class filter (assembly mode)";
	public static final String EVENTRECORDTRACERECONSTR_COMPONENT_NAME = "Trace reconstruction (trace event records -> event record traces)";
	public static final String EXECTRACESFROMEVENTTRACES_COMPONENT_NAME = "Trace reconstruction (event record traces -> execution traces)";
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
	public static final HelpFormatter CMD_HELP_FORMATTER = new HelpFormatter();
	public static final Options CMDL_OPTIONS = new Options();
	public static final List<Option> SORTED_OPTION_LIST = new CopyOnWriteArrayList<Option>();

	public static final String DECORATORS_OPTION_NAME = "node decorations";
	public static final char DECORATOR_SEPARATOR = ',';
	public static final String RESPONSE_TIME_DECORATOR_FLAG = "responseTimes";

	public static final String CMD_OPT_NAME_TRACE_COLORING = "traceColoring";
	public static final String COLORING_FILE_OPTION_NAME = "color map file";

	public static final String CMD_OPT_NAME_ADD_DESCRIPTIONS = "addDescriptions";
	public static final String DESCRIPTIONS_FILE_OPTION_NAME = "descriptions file";

	static {
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_INPUTDIRS).withArgName("dir1 ... dirN").hasArgs().isRequired(true)
				.withDescription("Log directories to read data from").withValueSeparator('=').create("i"));
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_OUTPUTDIR).withArgName("dir").hasArg(true).isRequired(true)
				.withDescription("Directory for the generated file(s)").withValueSeparator('=').create("o"));
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_OUTPUTFNPREFIX).withArgName("prefix").hasArg(true).isRequired(false)
				.withDescription("Prefix for output filenames\n").withValueSeparator('=').create("p"));
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PLOTALLOCATIONSEQDS).hasArg(false)
				.withDescription("Generate and store deployment-level sequence diagrams (.pic files)").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PLOTASSEMBLYSEQDS).hasArg(false)
				.withDescription("Generate and store assembly-level sequence diagrams (.pic files)").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PLOTALLOCATIONCOMPONENTDEPG)
				.withArgName(DECORATORS_OPTION_NAME)
				.hasArg(true).hasOptionalArg().withValueSeparator(DECORATOR_SEPARATOR)
				.withDescription("Generate and store a deployment-level component dependency graph (.dot file)").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PLOTASSEMBLYCOMPONENTDEPG)
				.withArgName(DECORATORS_OPTION_NAME)
				.hasArg(true).hasOptionalArg().withValueSeparator(DECORATOR_SEPARATOR)
				.withDescription("Generate and store an assembly-level component dependency graph (.dot file)").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PLOTCONTAINERDEPG).hasArg(false)
				.withDescription("Generate and store a container dependency graph (.dot file)").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PLOTALLOCATIONOPERATIONDEPG)
				.withArgName(DECORATORS_OPTION_NAME)
				.hasArg(true).hasOptionalArg().withValueSeparator(DECORATOR_SEPARATOR)
				.withDescription("Generate and store a deployment-level operation dependency graph (.dot file)").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PLOTASSEMBLYOPERATIONDEPG)
				.withArgName(DECORATORS_OPTION_NAME)
				.hasArg(true).hasOptionalArg().withValueSeparator(DECORATOR_SEPARATOR)
				.withDescription("Generate and store an assembly-level operation dependency graph (.dot file)").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PLOTAGGREGATEDALLOCATIONCALLTREE).hasArg(false)
				.withDescription("Generate and store an aggregated deployment-level call tree (.dot files)").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PLOTAGGREGATEDASSEMBLYCALLTREE).hasArg(false)
				.withDescription("Generate and store an aggregated assembly-level call tree (.dot files)").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PLOTCALLTREES).hasArg(false)
				.withDescription("Generate and store call trees for the selected traces (.dot files)").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PRINTMSGTRACES).hasArg(false)
				.withDescription("Save message trace representations of valid traces (.txt files)").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PRINTEXECTRACES).hasArg(false)
				.withDescription("Save execution trace representations of valid traces (.txt files)").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_PRINTINVALIDEXECTRACES).hasArg(false)
				.withDescription("Save a execution trace representations of invalid trace artifacts (.txt files)").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_ALLOCATIONEQUIVCLASSREPORT).hasArg(false)
				.withDescription("Output an overview about the deployment-level trace equivalence classes").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_ASSEMBLYEQUIVCLASSREPORT).hasArg(false)
				.withDescription("Output an overview about the assembly-level trace equivalence classes").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_SELECTTRACES).withArgName("id0 ... idn").hasArgs().isRequired(false)
				.withDescription("Consider only the traces identified by the list of trace IDs. Defaults to all traces.").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_IGNOREINVALIDTRACES).hasArg(false).isRequired(false)
				.withDescription("If selected, the execution aborts on the occurence of an invalid trace.").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_MAXTRACEDURATION).withArgName("duration in ms").hasArg().isRequired(false)
				.withDescription("Threshold (in milliseconds) after which an incomplete trace becomes invalid. Defaults to 600000 (10 minutes).").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_IGNOREEXECUTIONSBEFOREDATE)
				.withArgName(TraceAnalysisTool.DATE_FORMAT_PATTERN_CMD_USAGE_HELP).hasArg().isRequired(false)
				.withDescription("Executions starting before this date (UTC timezone) are ignored.").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_IGNOREEXECUTIONSAFTERDATE)
				.withArgName(TraceAnalysisTool.DATE_FORMAT_PATTERN_CMD_USAGE_HELP).hasArg().isRequired(false)
				.withDescription("Executions ending after this date (UTC timezone) are ignored.").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_SHORTLABELS).hasArg(false).isRequired(false)
				.withDescription("If selected, abbreviated labels (e.g., package names) are used in the visualizations.").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_INCLUDESELFLOOPS).hasArg(false).isRequired(false)
				.withDescription("If selected, self-loops are included in the visualizations.").create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_TRACE_COLORING).hasArg().isRequired(false)
				.withDescription("Color traces according to the given color map.").withArgName(COLORING_FILE_OPTION_NAME).create());
		SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(CMD_OPT_NAME_ADD_DESCRIPTIONS).hasArg().isRequired(false)
				.withDescription("Adds descriptions to elements according to the given file.").withArgName(DESCRIPTIONS_FILE_OPTION_NAME).create());

		for (final Option o : SORTED_OPTION_LIST) {
			CMDL_OPTIONS.addOption(o);
		}
		CMD_HELP_FORMATTER.setOptionComparator(new Comparator<Object>() {

			public int compare(final Object o1, final Object o2) {
				if (o1 == o2) {
					return 0;
				}
				final int posO1 = SORTED_OPTION_LIST.indexOf(o1);
				final int posO2 = SORTED_OPTION_LIST.indexOf(o2);
				if (posO1 < posO2) {
					return -1;
				}
				if (posO1 > posO2) {
					return 1;
				}
				return 0;
			}
		});
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
