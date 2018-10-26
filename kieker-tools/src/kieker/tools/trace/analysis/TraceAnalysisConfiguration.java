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
package kieker.tools.trace.analysis;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import org.slf4j.Logger;

import com.beust.jcommander.Parameter;

import kieker.tools.common.DateConverter;

/**
 * @author Reiner Jung
 *
 * @since 1.15
 */
public class TraceAnalysisConfiguration {

	private static final String CMD_HELP = "--help";

	private static final String CMD_INPUTDIRS = "--inputdirs";

	private static final String CMD_DIR = "--outputdir";

	private static final String CMD_PREFIX = "--prefix";

	private static final String CMD_PLOT_DEPLOYMENT_SEQUENCE_DIAGRAMS = "--plot-Deployment-Sequence-Diagrams";

	private static final String CMD_PLOT_ASSEMBLY_SEQUENCE_DIAGRAMS = "--plot-Assembly-Sequence-Diagrams";

	private static final String CMD_PLOT_DEPLOYMENT_COMPONENT_DEPENDENCY_GRAPH = "--plot-Deployment-Component-Dependency-Graph";

	private static final String CMD_PLOT_ASSEMBLY_COMPONENT_DEPENDENCY_GRAPH = "--plot-Assembly-Component-Dependency-Graph";

	private static final String CMD_PLOT_CONTAINER_DEPENDENCY_GRAPH = "--plot-Container-Dependency-Graph";

	private static final String CMD_PLOT_DEPLOYMENT_OPERATION_DEPENDENCY_GRAPH = "--plot-Deployment-Operation-Dependency-Graph";

	private static final String CMD_PLOT_ASSEMBLY_OPERATION_DEPENDENCY_GRAPH = "--plot-Assembly-Operation-Dependency-Graph";

	private static final String CMD_PLOT_AGGREGATED_DEPLOYMENT_CALL_TREE = "--plot-Aggregated-Deployment-Call-Tree";

	private static final String CMD_PLOT_AGGREGATED_ASSEMBLY_CALL_TREE = "--plot-Aggregated-Assembly-Call-Tree";

	private static final String CMD_PLOT_CALL_TREES = "--plot-Call-Trees";

	private static final String CMD_PRINT_MESSAGE_TRACES = "--print-Message-Traces";

	private static final String CMD_PRINT_EXECUTION_TRACES = "--print-Execution-Traces";

	private static final String CMD_PRINT_INVALID_EXECUTION_TRACES = "--print-invalid-Execution-Traces";

	private static final String CMD_PRINT_SYSTEM_MODEL = "--print-System-Model";

	private static final String CMD_PRINT_DEPLOYMENT_EQUIVALENCE_CLASSES = "--print-Deployment-Equivalence-Classes";

	private static final String CMD_PRINT_ASSEMBLY_EQUIVALENCE_CLASSES = "--print-Assembly-Equivalence-Classes";

	private static final String CMD_SELECT_TRACES = "--select-traces";

	private static final String CMD_FILTER_TRACES = "--filter-traces";

	private static final String CMD_IGNORE_INVALID_TRACES = "--ignore-invalid-traces";

	private static final String CMD_REPAIR_EVENT_BASED_TRACES = "--repair-event-based-traces";

	private static final String CMD_MAX_TRACE_DURATION = "--max-trace-duration";

	private static final String CMD_IGNORE_EXECUTIONS_BEFORE_DATE = "--ignore-executions-before-date";

	private static final String CMD_IGNORE_EXECUTIONS_AFTER_DATE = "--ignore-executions-after-date";

	private static final String CMD_SHORT_LABELS = "--short-labels";

	private static final String CMD_INCLUDE_SELF_LOOPS = "--include-self-loops";

	private static final String CMD_IGNORE_ASSUMED_CALLS = "--ignore-assumed-calls";

	private static final String CMD_TRACE_COLORING = "--traceColoring";

	private static final String CMD_ADD_DESCRIPTIONS = "--addDescriptions";

	private static final String CMD_VERBOSE = "--verbose";

	private static final String CMD_DEBUG = "--debug";

	@Parameter(names = { "-v", CMD_VERBOSE }, description = "verbosely prints additional information")
	private boolean verbose;

	@Parameter(names = { "-d", CMD_DEBUG }, description = "prints additional debug information")
	private boolean debug;

	@Parameter(names = { "-h", CMD_HELP }, help = true, description = "prints the usage information for the tool, including available options")
	private boolean help;

	@Parameter(names = { "-i", CMD_INPUTDIRS }, variableArity = true, description = "Log directories to read data from")
	private List<File> inputDirs;

	@Parameter(names = { "-o", CMD_DIR }, required = true, description = "Directory for the generated file(s)")
	private File outputDir;

	@Parameter(names = { "-p", CMD_PREFIX }, description = "Prefix for output filenames")
	private String prefix;

	@Parameter(names = { CMD_PLOT_DEPLOYMENT_SEQUENCE_DIAGRAMS }, description = "Generate and store deployment-level sequence diagrams (.pic)")
	private boolean plotDeploymentSequenceDiagrams;

	@Parameter(names = { CMD_PLOT_ASSEMBLY_SEQUENCE_DIAGRAMS }, description = "Generate and store assembly-level sequence diagrams (.pic)")
	private boolean plotAssemblySequenceDiagrams;

	@Parameter(names = { CMD_PLOT_DEPLOYMENT_COMPONENT_DEPENDENCY_GRAPH }, description = "Generate and store a deployment-level component dependency graph (.dot)")
	private List<String> plotDeploymentComponentDependencyGraph = new ArrayList<>();

	@Parameter(names = { CMD_PLOT_ASSEMBLY_COMPONENT_DEPENDENCY_GRAPH }, description = "Generate and store an assembly-level component dependency graph (.dot)")
	private List<String> plotAssemblyComponentDependencyGraph = new ArrayList<>();

	@Parameter(names = { CMD_PLOT_CONTAINER_DEPENDENCY_GRAPH }, description = "Generate and store a container dependency graph (.dot file)")
	private boolean plotContainerDependencyGraph;

	@Parameter(names = { CMD_PLOT_DEPLOYMENT_OPERATION_DEPENDENCY_GRAPH }, description = "Generate and store a deployment-level operation dependency graph (.dot)")
	private List<String> plotDeploymentOperationDependencyGraph = new ArrayList<>();

	@Parameter(names = { CMD_PLOT_ASSEMBLY_OPERATION_DEPENDENCY_GRAPH }, description = "Generate and store an assembly-level operation dependency graph (.dot)")
	private List<String> plotAssemblyOperationDependencyGraph = new ArrayList<>();

	@Parameter(names = { CMD_PLOT_AGGREGATED_DEPLOYMENT_CALL_TREE }, description = "Generate and store an aggregated deployment-level call tree (.dot)")
	private boolean plotAggregatedDeploymentCallTree;

	@Parameter(names = { CMD_PLOT_AGGREGATED_ASSEMBLY_CALL_TREE }, description = "Generate and store an aggregated assembly-level call tree (.dot)")
	private boolean plotAggregatedAssemblyCallTree;

	@Parameter(names = { CMD_PLOT_CALL_TREES }, description = "Generate and store call trees for the selected traces (.dot)")
	private boolean plotCallTrees;

	@Parameter(names = { CMD_PRINT_MESSAGE_TRACES }, description = "Save message trace representations of valid traces (.txt)")
	private boolean printMessageTraces;

	@Parameter(names = { CMD_PRINT_EXECUTION_TRACES }, description = "Save execution trace representations of valid traces (.txt)")
	private boolean printExecutionTraces;

	@Parameter(names = { CMD_PRINT_INVALID_EXECUTION_TRACES }, description = "Save a execution trace representations of invalid trace artifacts (.txt)")
	private boolean printInvalidExecutionTraces;

	@Parameter(names = { CMD_PRINT_SYSTEM_MODEL }, description = "Save a representation of the internal system model (.html)")
	private boolean printSystemModel;

	@Parameter(names = { CMD_PRINT_DEPLOYMENT_EQUIVALENCE_CLASSES }, description = "Output an overview about the deployment-level trace equivalence classes")
	private boolean printDeploymentEquivalenceClasses;

	@Parameter(names = { CMD_PRINT_ASSEMBLY_EQUIVALENCE_CLASSES }, description = "Output an overview about the assembly-level trace equivalence classes")
	private boolean printAssemblyEquivalenceClasses;

	@Parameter(names = {
		CMD_SELECT_TRACES }, variableArity = true, description = "Consider only the traces identified by the list of trace IDs. Defaults to all traces.")
	private List<Long> selectTraces = new ArrayList<>();

	@Parameter(names = {
		CMD_FILTER_TRACES }, variableArity = true, description = "Consider only the traces not identified by the list of trace IDs. Defaults to no traces.")
	private List<Long> filterTraces = new ArrayList<>();

	@Parameter(names = { CMD_IGNORE_INVALID_TRACES }, description = "If selected, the execution aborts on the occurence of an invalid trace.")
	private boolean ignoreInvalidTraces;

	@Parameter(names = {
		CMD_REPAIR_EVENT_BASED_TRACES }, description = "If selected, BeforeEvents with missing AfterEvents e.g. because of software crash will be repaired.")
	private boolean repairEventBasedTraces;

	// "duration in ms"
	@Parameter(names = {
		CMD_MAX_TRACE_DURATION }, description = "Threshold (in ms) after which incomplete traces become invalid. Defaults to 600,000 (i.e, 10 minutes).")
	private Long maxTraceDuration;

	// DATE_FORMAT_PATTERN_CMD_USAGE_HELP
	@Parameter(names = {
		CMD_IGNORE_EXECUTIONS_BEFORE_DATE }, description = "Executions starting before this date (UTC timezone) or monitoring timestamp are ignored.", converter = DateConverter.class)
	private Long ignoreExecutionsBeforeDate;

	@Parameter(names = {
		CMD_IGNORE_EXECUTIONS_AFTER_DATE }, description = "Executions ending after this date (UTC timezone) or monitoring timestamp  are ignored.", converter = DateConverter.class)
	private Long ignoreExecutionsAfterDate;

	@Parameter(names = { CMD_SHORT_LABELS }, description = "If selected, abbreviated labels (e.g., package names) are used in the visualizations.")
	private boolean shortLabels;

	@Parameter(names = { CMD_INCLUDE_SELF_LOOPS }, description = "If selected, self-loops are included in the visualizations.")
	private boolean includeSelfLoops;

	@Parameter(names = { CMD_IGNORE_ASSUMED_CALLS }, description = "If selected, assumed calls are visualized just as regular calls.")
	private boolean ignoreAssumedCalls;

	// COLORING_FILE_OPTION_NAME
	@Parameter(names = {
		CMD_TRACE_COLORING }, description = "Color traces according to the given color map given as a properties file (key: trace ID, value: color in hex format, e.g., 0xff0000 for red; use trace ID 'default' to specify the default color)")
	private File traceColoringFile;

	// DESCRIPTIONS_FILE_OPTION_NAME
	@Parameter(names = {
		CMD_ADD_DESCRIPTIONS }, description = "Adds descriptions to elements according to the given file as a properties file (key: component ID, e.g., @1; value: description)")
	private File addDescriptions;

	/** derived settings. */

	/** invert trace id filter. */
	private boolean invertTraceIdFilter;

	/** selected traces. */
	private final Set<Long> selectedTraces = new TreeSet<>();

	/**
	 * Do not need any parameter.
	 */
	public TraceAnalysisConfiguration() {
		// pojo for jcommander
	}

	public boolean isVerbose() {
		return this.verbose;
	}

	public boolean isDebug() {
		return this.debug;
	}

	public boolean isHelp() {
		return this.help;
	}

	public List<File> getInputDirs() {
		return this.inputDirs;
	}

	public File getOutputDir() {
		return this.outputDir;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public boolean isPlotDeploymentSequenceDiagrams() {
		return this.plotDeploymentSequenceDiagrams;
	}

	public boolean isPlotAssemblySequenceDiagrams() {
		return this.plotAssemblySequenceDiagrams;
	}

	public List<String> getPlotDeploymentComponentDependencyGraph() {
		return this.plotDeploymentComponentDependencyGraph;
	}

	public List<String> getPlotAssemblyComponentDependencyGraph() {
		return this.plotAssemblyComponentDependencyGraph;
	}

	public boolean isPlotContainerDependencyGraph() {
		return this.plotContainerDependencyGraph;
	}

	public List<String> getPlotDeploymentOperationDependencyGraph() {
		return this.plotDeploymentOperationDependencyGraph;
	}

	public List<String> getPlotAssemblyOperationDependencyGraph() {
		return this.plotAssemblyOperationDependencyGraph;
	}

	public boolean isPlotAggregatedDeploymentCallTree() {
		return this.plotAggregatedDeploymentCallTree;
	}

	public boolean isPlotAggregatedAssemblyCallTree() {
		return this.plotAggregatedAssemblyCallTree;
	}

	public boolean isPlotCallTrees() {
		return this.plotCallTrees;
	}

	public boolean isPrintMessageTraces() {
		return this.printMessageTraces;
	}

	public boolean isPrintExecutionTraces() {
		return this.printExecutionTraces;
	}

	public boolean isPrintInvalidExecutionTraces() {
		return this.printInvalidExecutionTraces;
	}

	public boolean isPrintSystemModel() {
		return this.printSystemModel;
	}

	public boolean isPrintDeploymentEquivalenceClasses() {
		return this.printDeploymentEquivalenceClasses;
	}

	public boolean isPrintAssemblyEquivalenceClasses() {
		return this.printAssemblyEquivalenceClasses;
	}

	public List<Long> getSelectTraces() {
		return this.selectTraces;
	}

	public List<Long> getFilterTraces() {
		return this.filterTraces;
	}

	public boolean isIgnoreInvalidTraces() {
		return this.ignoreInvalidTraces;
	}

	public boolean isRepairEventBasedTraces() {
		return this.repairEventBasedTraces;
	}

	public Long getMaxTraceDuration() {
		return this.maxTraceDuration;
	}

	public Long getIgnoreExecutionsBeforeDate() {
		return this.ignoreExecutionsBeforeDate;
	}

	public Long getIgnoreExecutionsAfterDate() {
		return this.ignoreExecutionsAfterDate;
	}

	public boolean isShortLabels() {
		return this.shortLabels;
	}

	public boolean isIncludeSelfLoops() {
		return this.includeSelfLoops;
	}

	public boolean isIgnoreAssumedCalls() {
		return this.ignoreAssumedCalls;
	}

	public File getTraceColoringFile() {
		return this.traceColoringFile;
	}

	public File getAddDescriptions() {
		return this.addDescriptions;
	}

	public void setInvertTraceIdFilter(final boolean invertTraceIdFilter) {
		this.invertTraceIdFilter = invertTraceIdFilter;
	}

	public boolean isInvertTraceIdFilter() {
		return this.invertTraceIdFilter;

	}

	public Set<Long> getSelectedTraces() {
		return this.selectedTraces;
	}

	/**
	 * This method dumps the configuration on the screen.
	 *
	 * @param logger
	 *            the output logger
	 */
	public void dumpConfiguration(final Logger logger) {
		final DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_PATTERN, Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		logger.debug("#");
		logger.debug("# Configuration");
		String val = "<null>";
		for (final File directory : this.inputDirs) {
			if ("<null>".equals(val)) {
				val = directory.getAbsolutePath();
			} else {
				val += ", " + directory.getAbsolutePath();
			}
		}
		logger.debug("{}: {}", CMD_INPUTDIRS, val);

		logger.debug("{}: {}", CMD_DIR, this.outputDir.getAbsolutePath());

		logger.debug("{}: {}", CMD_PREFIX, this.prefix);

		logger.debug("{}: {}", CMD_PRINT_DEPLOYMENT_EQUIVALENCE_CLASSES, String.valueOf(this.printDeploymentEquivalenceClasses));
		logger.debug("{}: {}", CMD_PRINT_ASSEMBLY_EQUIVALENCE_CLASSES, String.valueOf(this.printAssemblyEquivalenceClasses));
		logger.debug("{}: {}", CMD_PLOT_DEPLOYMENT_SEQUENCE_DIAGRAMS, String.valueOf(this.plotDeploymentSequenceDiagrams));
		logger.debug("{}: {}", CMD_PLOT_ASSEMBLY_SEQUENCE_DIAGRAMS, String.valueOf(this.plotAssemblySequenceDiagrams));
		logger.debug("{}: {}", CMD_PLOT_DEPLOYMENT_COMPONENT_DEPENDENCY_GRAPH, String.valueOf(this.plotDeploymentComponentDependencyGraph));
		logger.debug("{}: {}", CMD_PLOT_ASSEMBLY_COMPONENT_DEPENDENCY_GRAPH, String.valueOf(this.plotAssemblyComponentDependencyGraph));
		logger.debug("{}: {}", CMD_PLOT_CONTAINER_DEPENDENCY_GRAPH, String.valueOf(this.plotContainerDependencyGraph));
		logger.debug("{}: {}", CMD_PLOT_DEPLOYMENT_OPERATION_DEPENDENCY_GRAPH, String.valueOf(this.plotDeploymentOperationDependencyGraph));
		logger.debug("{}: {}", CMD_PLOT_ASSEMBLY_OPERATION_DEPENDENCY_GRAPH, String.valueOf(this.plotAssemblyOperationDependencyGraph));
		logger.debug("{}: {}", CMD_PLOT_AGGREGATED_DEPLOYMENT_CALL_TREE, String.valueOf(this.plotAggregatedDeploymentCallTree));
		logger.debug("{}: {}", CMD_PLOT_AGGREGATED_ASSEMBLY_CALL_TREE, String.valueOf(this.plotAggregatedAssemblyCallTree));
		logger.debug("{}: {}", CMD_PLOT_CALL_TREES, String.valueOf(this.plotCallTrees));
		logger.debug("{}: {}", CMD_PRINT_EXECUTION_TRACES, String.valueOf(this.printExecutionTraces));
		logger.debug("{}: {}", CMD_PRINT_INVALID_EXECUTION_TRACES, String.valueOf(this.printInvalidExecutionTraces));
		logger.debug("{}: {}", CMD_PRINT_MESSAGE_TRACES, String.valueOf(this.printMessageTraces));
		logger.debug("{}: {}", CMD_PRINT_SYSTEM_MODEL, String.valueOf(this.printSystemModel));
		logger.debug("{}: {}", CMD_DEBUG, String.valueOf(this.debug));
		logger.debug("{}: {}", CMD_VERBOSE, String.valueOf(this.verbose));
		logger.debug("{}: {}", CMD_HELP, String.valueOf(this.help));

		if (this.selectedTraces != null) {
			val = "<null>";
			for (final Long id : this.selectedTraces) {
				if ("<null>".equals(val)) {
					val = String.valueOf(id);
				} else {
					val += ", " + String.valueOf(id);
				}
			}
		} else {
			val = "<select all>";
		}
		if (this.selectTraces != null) {
			logger.debug("{}: {}", CMD_SELECT_TRACES, val);
		}
		if (this.filterTraces != null) {
			logger.debug("{}: {}", CMD_FILTER_TRACES, val);
		}

		logger.debug("{}: {}", CMD_SHORT_LABELS, String.valueOf(this.shortLabels));
		logger.debug("{}: {}", CMD_INCLUDE_SELF_LOOPS, String.valueOf(this.includeSelfLoops));
		logger.debug("{}: {}", CMD_IGNORE_ASSUMED_CALLS, String.valueOf(this.ignoreAssumedCalls));
		logger.debug("{}: {}", CMD_IGNORE_INVALID_TRACES, String.valueOf(this.ignoreInvalidTraces));
		logger.debug("{}: {}", CMD_REPAIR_EVENT_BASED_TRACES, String.valueOf(this.repairEventBasedTraces));
		if (this.maxTraceDuration != null) {
			logger.debug("{}: {} ms", CMD_MAX_TRACE_DURATION, String.valueOf(this.maxTraceDuration));
		}
		if (this.ignoreExecutionsBeforeDate != null) {
			logger.debug("{}: {} {}", CMD_IGNORE_EXECUTIONS_BEFORE_DATE, String.valueOf(this.ignoreExecutionsBeforeDate),
					dateFormat.format(this.ignoreExecutionsBeforeDate));
		}
		if (this.ignoreExecutionsAfterDate != null) {
			logger.debug("{}: {}", CMD_IGNORE_EXECUTIONS_AFTER_DATE, String.valueOf(this.ignoreExecutionsAfterDate),
					dateFormat.format(this.ignoreExecutionsBeforeDate));
		}
		if (this.traceColoringFile != null) {
			logger.debug("{}: {}", CMD_TRACE_COLORING, this.traceColoringFile.getAbsolutePath());
		}
		if (this.addDescriptions != null) {
			logger.debug("{}: {}", CMD_ADD_DESCRIPTIONS, String.valueOf(this.addDescriptions));
		}

	}

}
