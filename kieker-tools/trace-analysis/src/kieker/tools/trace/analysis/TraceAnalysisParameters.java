/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import org.slf4j.Logger;

import com.beust.jcommander.Parameter;

import kieker.tools.settings.converters.DateConverter;

/**
 * @author Reiner Jung
 *
 * @since 1.15
 */
public final class TraceAnalysisParameters { // NOPMD configuration class

	@Parameter(names = { "-v", "--verbose" }, description = "verbosely prints additional information")
	private boolean verbose;

	@Parameter(names = { "-d", "--debug" }, description = "prints additional debug information")
	private boolean debug;

	@Parameter(names = { "-h", "--help" }, help = true, description = "prints the usage information for the tool, including available options")
	private boolean help;

	@Parameter(names = { "-i", "--inputdirs" }, variableArity = true, description = "Log directories to read data from")
	private List<File> inputDirs;

	@Parameter(names = { "-o", "--outputdir" }, required = true, description = "Directory for the generated file(s)")
	private File outputDir;

	@Parameter(names = { "-p", "--output-filename-prefix" }, description = "Prefix for output filenames")
	private String prefix;

	@Parameter(names = "--plot-Deployment-Sequence-Diagrams", description = "Generate and store deployment-level sequence diagrams (.pic)")
	private boolean plotDeploymentSequenceDiagrams;

	@Parameter(names = "--plot-Assembly-Sequence-Diagrams", description = "Generate and store assembly-level sequence diagrams (.pic)")
	private boolean plotAssemblySequenceDiagrams;

	@Parameter(names = "--plot-Deployment-Component-Dependency-Graph", validateWith = DecoratorValidator.class,
			description = "Generate and store a deployment-level component dependency graph (.dot)")
	private List<String> plotDeploymentComponentDependencyGraph;

	@Parameter(names = "--plot-Assembly-Component-Dependency-Graph", validateWith = DecoratorValidator.class,
			description = "Generate and store an assembly-level component dependency graph (.dot)")
	private List<String> plotAssemblyComponentDependencyGraph;

	@Parameter(names = "--plot-Container-Dependency-Graph", description = "Generate and store a container dependency graph (.dot file)")
	private boolean plotContainerDependencyGraph;

	@Parameter(names = "--plot-Deployment-Operation-Dependency-Graph", variableArity = true, validateWith = DecoratorValidator.class,
			description = "Generate and store a deployment-level operation dependency graph (.dot)")
	private List<String> plotDeploymentOperationDependencyGraph;

	@Parameter(names = "--plot-Assembly-Operation-Dependency-Graph", variableArity = true, validateWith = DecoratorValidator.class,
			description = "Generate and store an assembly-level operation dependency graph (.dot)")
	private List<String> plotAssemblyOperationDependencyGraph;

	@Parameter(names = "--plot-Aggregated-Deployment-Call-Tree",
			description = "Generate and store an aggregated deployment-level call tree (.dot)")
	private boolean plotAggregatedDeploymentCallTree;

	@Parameter(names = "--plot-Aggregated-Assembly-Call-Tree",
			description = "Generate and store an aggregated assembly-level call tree (.dot)")
	private boolean plotAggregatedAssemblyCallTree;

	@Parameter(names = "--plot-Call-Trees", description = "Generate and store call trees for the selected traces (.dot)")
	private boolean plotCallTrees;

	@Parameter(names = "--print-Message-Traces", description = "Save message trace representations of valid traces (.txt)")
	private boolean printMessageTraces;

	@Parameter(names = "--print-Execution-Traces", description = "Save execution trace representations of valid traces (.txt)")
	private boolean printExecutionTraces;

	@Parameter(names = "--print-invalid-Execution-Traces", description = "Save a execution trace representations of invalid trace artifacts (.txt)")
	private boolean printInvalidExecutionTraces;

	@Parameter(names = "--print-System-Model", description = "Save a representation of the internal system model (.html)")
	private boolean printSystemModel;

	@Parameter(names = "--print-Deployment-Equivalence-Classes", description = "Output an overview about the deployment-level trace equivalence classes")
	private boolean printDeploymentEquivalenceClasses;

	@Parameter(names = "--print-Assembly-Equivalence-Classes", description = "Output an overview about the assembly-level trace equivalence classes")
	private boolean printAssemblyEquivalenceClasses;

	@Parameter(names = "--select-traces", variableArity = true,
			description = "Consider only the traces identified by the list of trace IDs. Defaults to all traces.")
	private List<Long> selectTraces;

	@Parameter(names = "--filter-traces", variableArity = true,
			description = "Consider only the traces not identified by the list of trace IDs. Defaults to no traces.")
	private List<Long> filterTraces;

	@Parameter(names = "--ignore-invalid-traces", description = "If selected, the execution aborts on the occurence of an invalid trace.")
	private boolean ignoreInvalidTraces;

	@Parameter(names = "--repair-event-based-traces",
			description = "If selected, BeforeEvents with missing AfterEvents e.g. because of software crash will be repaired.")
	private boolean repairEventBasedTraces;

	// "duration in ms"
	@Parameter(names = "--max-trace-duration",
			description = "Threshold (in ms) after which incomplete traces become invalid. Defaults to 600,000 (i.e, 10 minutes).")
	private Long maxTraceDurationMillis;

	// DATE_FORMAT_PATTERN_CMD_USAGE_HELP
	@Parameter(names = "--ignore-executions-before-date",
			description = "Executions starting before this date (UTC timezone) or monitoring timestamp are ignored.",
			converter = DateConverter.class)
	private Long ignoreExecutionsBeforeDate;

	@Parameter(names = "--ignore-executions-after-date",
			description = "Executions ending after this date (UTC timezone) or monitoring timestamp  are ignored.",
			converter = DateConverter.class)
	private Long ignoreExecutionsAfterDate;

	@Parameter(names = "--short-labels", description = "If selected, abbreviated labels (e.g., package names) are used in the visualizations.")
	private boolean shortLabels;

	@Parameter(names = "--include-self-loops", description = "If selected, self-loops are included in the visualizations.")
	private boolean includeSelfLoops;

	@Parameter(names = "--ignore-assumed-calls", description = "If selected, assumed calls are visualized just as regular calls.")
	private boolean ignoreAssumedCalls;

	@Parameter(names = "--buffer-size", description = "Size of the read buffer, default is 8192 bytes")
	private Integer readBufferSize;

	// COLORING_FILE_OPTION_NAME
	@Parameter(names = "--traceColoring",
			description = "Color traces according to the given color map given as a properties file (key: trace ID, value: color in hex format,"
					+ " e.g., 0xff0000 for red; use trace ID 'default' to specify the default color)")
	private File traceColoringFile;

	// DESCRIPTIONS_FILE_OPTION_NAME
	@Parameter(names = "--addDescriptions",
			description = "Adds descriptions to elements according to the given file as a properties file (key: component ID, e.g., @1; value: description)")
	private File addDescriptions;

	/** derived settings. */

	/** invert trace id filter. */
	private boolean invertTraceIdFilter;

	/** selected traces. */
	private final Set<Long> selectedTraces = new TreeSet<>();

	/**
	 * Do not need any parameter.
	 */
	public TraceAnalysisParameters() {
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

	/**
	 * Get max trace duration, default is 600 000 ms.
	 *
	 * @return returns the max trace duration
	 */
	public Long getMaxTraceDuration() {
		if (this.maxTraceDurationMillis == null) {
			this.maxTraceDurationMillis = (long) (10 * 60 * 1000);
		}
		return this.maxTraceDurationMillis;
	}

	/**
	 * @return returns the ignore execution before date value, if none is specified created default value.
	 */
	public Long getIgnoreExecutionsBeforeDate() {
		return this.ignoreExecutionsBeforeDate;
	}

	public void setIgnoreExecutionsBeforeDate(final long ignoreExecutionsBeforeDate) {
		this.ignoreExecutionsBeforeDate = ignoreExecutionsBeforeDate;
	}

	/**
	 * @return returns the ignore execution after date value, if none is specified created default value.
	 */
	public Long getIgnoreExecutionsAfterDate() {
		return this.ignoreExecutionsAfterDate;
	}

	public void setIgnoreExecutionsAfterDate(final long ignoreExecutionsAfterDate) {
		this.ignoreExecutionsAfterDate = ignoreExecutionsAfterDate;
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
		final DateFormat dateFormat = new SimpleDateFormat(DateConverter.DATE_FORMAT_PATTERN, Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		if (logger.isDebugEnabled()) {
			logger.debug("#");
			logger.debug("# Configuration");
		}
		String val = "<null>";
		for (final File directory : this.inputDirs) {
			if ("<null>".equals(val)) {
				val = directory.getAbsolutePath();
			} else {
				val += ", " + directory.getAbsolutePath();
			}
		}

		for (final Field field : this.getClass().getDeclaredFields()) {
			final Parameter parameter = field.getAnnotation(Parameter.class);
			if (parameter != null) {
				final String[] names = parameter.names();
				Object data = null;
				try {
					data = field.get(this);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
				if (data == null) {
					data = "<none>";
				}
				if (logger.isDebugEnabled()) {
					logger.debug("{}: {}", names[names.length - 1], data);
				}
			}
		}

		if (this.selectedTraces.isEmpty()) {
			val = "<null>";
			for (final Long id : this.selectedTraces) {
				if ("<null>".equals(val)) {
					val = String.valueOf(id);
				} else {
					val += ", " + id;
				}
			}
		} else {
			val = "<select all>";
		}
		if (this.selectTraces != null) {
			logger.debug("{}: {}", "--selected-traces", val);
		}
		if (this.filterTraces != null) {
			logger.debug("{}: {}", "--filter-traces", val);
		}

		if (this.traceColoringFile != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("{}: {}", "--cmd-trace-coloring", this.traceColoringFile.getAbsolutePath());
			}
		} else {
			logger.debug("{}: <none>", "--cmd-trace-coloring");
		}
		if (this.addDescriptions != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("{}: {}", "--add-description", String.valueOf(this.addDescriptions));
			}
		} else {
			logger.debug("{}: <none>", "--add-description");
		}
	}

	public Integer getReadBufferSize() {
		return this.readBufferSize;
	}

}
