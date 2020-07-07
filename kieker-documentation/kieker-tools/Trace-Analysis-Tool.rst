.. _kieker-tools-trace-analysis-tool:

Trace Analysis Tool 
====================================

Calls Kieker.TraceAnalysis to analyze and visualize monitored trace
data.

Usage
-----

== ===================================================== ======
=================================================================================================================================================================================================
   --addDescriptions                                            Adds descriptions to elements according to the given file as a properties file (key: component ID, e.g., @1; value: description)
-d --debug                                               false  Prints additional debug information.
   --filter-traces                                              Consider only the traces not identified by the list of trace IDs. Defaults to no traces.
-h --help                                                false  Prints the usage information for the tool, including available options.
   --ignore-assumed-calls                                false  If selected, assumed calls are visualized just as regular calls.
   --ignore-executions-after-date <timestamp>                   Executions ending after this date (UTC timezone) or monitoring timestamp are ignored.
   --ignore-executions-before-date <timestamp>                  Executions starting before this date (UTC timezone) or monitoring timestamp are ignored.
   --ignore-invalid-traces                                      If selected, the execution aborts on the occurrence of an invalid trace.
   --include-self-loops                                         If selected, self-loops are included in the visualizations.
-i --inputdirs <dir1,dir2,...,dirN>                             Log directories to read data from.
   --max-trace-duration <duration>                       600000 Threshold (in ms) after which incomplete traces become invalid. Defaults to 600,000 (i.e, 10 minutes).
-p --output-filename-prefix                                     Prefix for output filenames.
-o --outputdir                                                  Directory for the generated file(s).
   --plot-Aggregated-Assembly-Call-Tree                  false  Generate and store an aggregated assembly-level call tree (.dot)
   --plot-Aggregated-Deployment-Call-Tree                false  Generate and store an aggregated deployment-level call tree (.dot)
   --plot-Assembly-Component-Dependency-Graph <none|*>          Generate and store an assembly-level component dependency graph (.dot)
   --plot-Assembly-Operation-Dependency-Graph <none|*>          Generate and store an assembly-level operation dependency graph (.dot)
   --plot-Assembly-Sequence-Diagrams                     false  Generate and store assembly-level sequence diagrams (.pic)
   --plot-Call-Trees                                     false  Generate and store call trees for the selected traces (.dot)
   --plot-Container-Dependency-Graph                     false  Generate and store a container dependency graph (.dot file)
   --plot-Deployment-Component-Dependency-Graph <none|*>        Generate and store a deployment-level component dependency graph (.dot)
   --plot-Deployment-Operation-Dependency-Graph <none|*>        Generate and store a deployment-level operation dependency graph (.dot)
   --plot-Deployment-Sequence-Diagrams                   false  Generate and store deployment-level sequence diagrams (.pic)
   --print-Assembly-Equivalence-Classes                  false  Output an overview about the assembly-level trace equivalence classes
   --print-Deployment-Equivalence-Classes                false  Output an overview about the deployment-level trace equivalence classes
   --print-Execution-Traces                              false  Save execution trace representations of valid traces (.txt)
   --print-Message-Traces                                false  Save message trace representations of valid traces (.txt)
   --print-System-Model                                  false  Save a representation of the internal system model (.html)
   --print-invalid-Execution-Traces                      false  Save a execution trace representations of invalid trace artifacts (.txt)
   --repair-event-based-traces                           false  If selected, BeforeEvents with missing AfterEvents e.g. because of software crash will be repaired.
   --select-traces                                              Consider only the traces identified by the list of trace IDs. Defaults to all traces.
   --short-labels                                        false  If selected, abbreviated labels (e.g., package names) are used in the visualizations.
   --traceColoring                                              Color traces according to the given color map given as a properties file (key: trace ID, value: color in hex format, e.g., 0xff0000 for red; use trace ID 'default' to specify the default color)
-v --verbose                                             false  Verbosely prints additional information
== ===================================================== ======
=================================================================================================================================================================================================

Example
-------

The following commands generate a deployment-level operation dependency
graph and convert it to pdf format:

trace-analysis

--inputdirs
examples/userguide/ch5–trace-monitoring-aspectj/testdata/kieker-20100830-082225522-UTC

--outputdir .

--plot-Deployment-Operation-Dependency-Graph

dot -T pdf deploymentOperationDependencyGraph.dot >
deploymentOperationDependencyGraph.pdf

Listing A.5: Execution under UNIX-like systems

| 

trace-analysis.bat

--inputdirs
..\examples\userguide\ch5–trace-monitoring-aspectj\testdata\kieker-20100830-082225522-UTC

--outputdir .

--plot-Deployment-Operation-Dependency-Graph

dot -T pdf deploymentOperationDependencyGraph.dot >
deploymentOperationDependencyGraph.pdf

Listing A.6: Execution under Windows
