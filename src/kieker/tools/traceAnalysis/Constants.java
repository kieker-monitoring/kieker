package kieker.tools.traceAnalysis;

/**
 * Externalized Strings from {@link TraceAnalysisTool}
 * 
 * @author Robert von Massow
 * 
 */
public class Constants {

	public static final String CMD_OPT_NAME_INPUTDIRS = "inputdirs";
	public static final String CMD_OPT_NAME_OUTPUTDIR = "outputdir";
	public static final String CMD_OPT_NAME_OUTPUTFNPREFIX = "output-filename-prefix";
	public static final String CMD_OPT_NAME_SELECTTRACES = "select-traces";
	public static final String CMD_OPT_NAME_TRACEEQUIVCLASSMODE = "trace-equivalence-mode";
	public static final String CMD_OPT_NAME_SHORTLABELS = "short-labels";
	public static final String CMD_OPT_NAME_IGNOREINVALIDTRACES = "ignore-invalid-traces";
	public static final String CMD_OPT_NAME_TASK_PLOTSEQDS = "plot-Sequence-Diagrams";
	public static final String CMD_OPT_NAME_TASK_PLOTCOMPONENTDEPG = "plot-Component-Dependency-Graph";
	public static final String CMD_OPT_NAME_TASK_PLOTCONTAINERDEPG = "plot-Container-Dependency-Graph";
	public static final String CMD_OPT_NAME_TASK_PLOTOPERATIONDEPG = "plot-Operation-Dependency-Graph";
	public static final String CMD_OPT_NAME_TASK_PLOTAGGREGATEDCALLTREE = "plot-Aggregated-Call-Tree";
	public static final String CMD_OPT_NAME_TASK_PLOTCALLTREES = "plot-Call-Trees";
	public static final String CMD_OPT_NAME_TASK_PRINTMSGTRACES = "print-Message-Traces";
	public static final String CMD_OPT_NAME_TASK_PRINTEXECTRACES = "print-Execution-Traces";
	public static final String CMD_OPT_NAME_TASK_PRINTINVALIDEXECTRACES = "print-invalid-Execution-Traces";
	public static final String CMD_OPT_NAME_TASK_EQUIVCLASSREPORT = "print-Equivalence-Classes";
	public static final String CMD_OPT_NAME_MAXTRACEDURATION = "max-trace-duration";
	public static final String CMD_OPT_NAME_IGNORERECORDSBEFOREDATE = "ignore-records-before-date";
	public static final String CMD_OPT_NAME_IGNORERECORDSAFTERDATE = "ignore-records-after-date";
	public static final String SEQUENCE_DIAGRAM_FN_PREFIX = "sequenceDiagram";
	public static final String COMPONENT_DEPENDENCY_GRAPH_FN_PREFIX = "componentDependencyGraph";
	public static final String CONTAINER_DEPENDENCY_GRAPH_FN_PREFIX = "containerDependencyGraph";
	public static final String OPERATION_DEPENDENCY_GRAPH_FN_PREFIX = "operationDependencyGraph";
	public static final String AGGREGATED_CALL_TREE_FN_PREFIX = "aggregatedCallTree";
	public static final String CALL_TREE_FN_PREFIX = "callTree";
	public static final String MESSAGE_TRACES_FN_PREFIX = "messageTraces";
	public static final String EXECUTION_TRACES_FN_PREFIX = "executionTraces";
	public static final String INVALID_TRACES_FN_PREFIX = "invalidTraceArtifacts";
	public static final String TRACE_EQUIV_CLASSES_FN_PREFIX = "traceEquivClasses";
	public static final String DATE_FORMAT_PATTERN = "yyyyMMdd'-'HHmmss";
	public static final String TRACE_EQUIVALENCE_MODE_STR_DISABLED = "disabled";
	public static final String TRACE_EQUIVALENCE_MODE_STR_ASSEMBLY = "assembly";
	public static final String TRACE_EQUIVALENCE_MODE_STR_ALLOCATION = "allocation";

}
