package kieker.tools.traceAnalysis;

import java.util.Comparator;
import java.util.List;
import java.util.Vector;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

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
    public static final String CMD_OPT_NAME_IGNOREEXECUTIONSBEFOREDATE = "ignore-executions-before-date";
    public static final String CMD_OPT_NAME_IGNOREEXECUTIONSAFTERDATE = "ignore-executions-after-date";
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
    public static final String EXEC_TRACE_RECONSTR_COMPONENT_NAME = "Execution record transformation";
    public static final String TRACERECONSTR_COMPONENT_NAME = "Trace reconstruction";
    public static final String TRACEEEQUIVCLASS_COMPONENT_NAME = "Trace equivalence class filter";
    public static final String PRINTMSGTRACE_COMPONENT_NAME = "Print message traces";
    public static final String PRINTEXECTRACE_COMPONENT_NAME = "Print execution traces";
    public static final String PRINTINVALIDEXECTRACE_COMPONENT_NAME = "Print invalid execution traces";
    public static final String PLOTCOMPONENTDEPGRAPH_COMPONENT_NAME = "Component dependency graph";
    public static final String PLOTCONTAINERDEPGRAPH_COMPONENT_NAME = "Container dependency graph";
    public static final String PLOTOPERATIONDEPGRAPH_COMPONENT_NAME = "Operation dependency graph";
    public static final String PLOTSEQDIAGR_COMPONENT_NAME = "Sequence diagrams";
    public static final String PLOTAGGREGATEDCALLTREE_COMPONENT_NAME = "Aggregated call tree";
    public static final String PLOTCALLTREE_COMPONENT_NAME = "Trace call trees";
    public static final HelpFormatter CMD_HELP_FORMATTER = new HelpFormatter();
    public static final Options CMDL_OPTIONS = new Options();
    public static final List<Option> SORTED_OPTION_LIST = new Vector<Option>();

    static {
        SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_INPUTDIRS).withArgName("dir1 ... dirN").hasArgs().isRequired(true).withDescription(
                "Log directories to read data from").withValueSeparator('=').create("i"));
        SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_OUTPUTDIR).withArgName("dir").hasArg(
                true).isRequired(true).withDescription(
                "Directory for the generated file(s)").withValueSeparator('=').create("o"));
        SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_OUTPUTFNPREFIX).withArgName("prefix").hasArg(true).isRequired(false).withDescription(
                "Prefix for output filenames\n").withValueSeparator('=').create("p"));

        // OptionGroup cmdlOptGroupTask = new OptionGroup();
        // cmdlOptGroupTask.isRequired();
        SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_TASK_PLOTSEQDS).hasArg(false).withDescription(
                "Generate and store sequence diagrams (.pic files)").create());
        SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_TASK_PLOTCOMPONENTDEPG).hasArg(false).withDescription(
                "Generate and store a component dependency graph (.dot file)").create());
        SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_TASK_PLOTCONTAINERDEPG).hasArg(false).withDescription(
                "Generate and store a container dependency graph (.dot file)").create());
        SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_TASK_PLOTOPERATIONDEPG).hasArg(false).withDescription(
                "Generate and store an operation dependency graph (.dot file)").create());
        SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_TASK_PLOTAGGREGATEDCALLTREE).hasArg(
                false).withDescription(
                "Generate and store an aggregated call tree (.dot files)").create());
        SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(Constants.CMD_OPT_NAME_TASK_PLOTCALLTREES).hasArg(false).withDescription(
                "Generate and store call trees for the selected traces (.dot files)").create());
        SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(Constants.CMD_OPT_NAME_TASK_PRINTMSGTRACES).hasArg(false).withDescription(
                "Save message trace representations of valid traces (.txt files)").create());
        SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_TASK_PRINTEXECTRACES).hasArg(false).withDescription(
                "Save execution trace representations of valid traces (.txt files)").create());
        SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_TASK_PRINTINVALIDEXECTRACES).hasArg(false).withDescription(
                "Save a execution trace representations of invalid trace artifacts (.txt files)").create());
        SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_TASK_EQUIVCLASSREPORT).hasArg(false).withDescription(
                "Output an overview about the trace equivalence classes").create());

        /*
         * These tasks should be moved to a dedicated tool, since this tool
         * covers trace analysis
         */
        // cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_INITJMSREADER).hasArg(false).withDescription("Creates a jms reader and shows incomming data in the command line").create());
        // cmdlOpts.addOption(OptionBuilder.withLongOpt(CMD_OPT_NAME_TASK_INITJMSREADERJFX).hasArg(false).withDescription("Creates a jms reader and shows incomming data in the command line and visualizes with javafx").create());

        // cmdlOpts.addOptionGroup(cmdlOptGroupTask);
        SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(Constants.CMD_OPT_NAME_SELECTTRACES).withArgName("id0 ... idn").hasArgs().isRequired(false).withDescription(
                "Consider only the traces identified by the comma-separated list of trace IDs. Defaults to all traces.").create());
        SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(Constants.CMD_OPT_NAME_TRACEEQUIVCLASSMODE).withArgName(
                String.format(
                "%s|%s|%s",
                Constants.TRACE_EQUIVALENCE_MODE_STR_ALLOCATION,
                Constants.TRACE_EQUIVALENCE_MODE_STR_ASSEMBLY,
                Constants.TRACE_EQUIVALENCE_MODE_STR_DISABLED)).hasArg(true).isRequired(false).withDescription(
                "If selected, the selected tasks are performed on representatives of the equivalence classes only.").create());
        SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(Constants.CMD_OPT_NAME_IGNOREINVALIDTRACES).hasArg(false).isRequired(false).withDescription(
                "If selected, the execution aborts on the occurence of an invalid trace.").create());
        SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(Constants.CMD_OPT_NAME_MAXTRACEDURATION).withArgName("duration in ms").hasArg().isRequired(false).withDescription(
                "Threshold (in milliseconds) after which an incomplete trace becomes invalid. Defaults to infinity.").create());
        SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_IGNOREEXECUTIONSBEFOREDATE).withArgName(
                TraceAnalysisTool.DATE_FORMAT_PATTERN_CMD_USAGE_HELP).hasArg().isRequired(false).withDescription(
                "Executions starting before this date (UTC timezone) are ignored.").create());
        SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(
                Constants.CMD_OPT_NAME_IGNOREEXECUTIONSAFTERDATE).withArgName(
                TraceAnalysisTool.DATE_FORMAT_PATTERN_CMD_USAGE_HELP).hasArg().isRequired(false).withDescription(
                "Executions ending after this date (UTC timezone) are ignored.").create());
        SORTED_OPTION_LIST.add(OptionBuilder.withLongOpt(Constants.CMD_OPT_NAME_SHORTLABELS).hasArg(false).isRequired(false).withDescription(
                "If selected, the hostnames of the executions are NOT considered.").create());

        for (final Option o : SORTED_OPTION_LIST) {
            CMDL_OPTIONS.addOption(o);
        }
        CMD_HELP_FORMATTER.setOptionComparator(new Comparator() {

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
