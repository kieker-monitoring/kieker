# Kieker Tools

The `tools` directory contains a set of tools prepacked as tar and zip
archives. Each archive contains one tool with all its libraries and
start scripts. The start scripts are located in the `bin` directory and
the libraries in the `lib` directory. In the tool root directory, e.g.,
`trace-analysis-1.14`, you can find a `log4j.cfg` file, used to
configure the logging output for your tool.

To change the logging setup you can either change that file or define
additional options with the `JAVA_OPTS` environment variable, e.g.,
```
export JAVA_OPTS="-Dlog4j.configuration=file:///full/path/to/logger/config/log4j.cfg"
```
or use the tool specific `_OPTS` variable, e.g., `TRACE_ANALYSIS_OPTS`
for the `trace-analysis` tool.

Furthermore, you can use both variables to pass additional JVM
parameters and options to a tool.

## Tool Overview

You may find more detailed information on all tools at:
https://kieker-monitoring.atlassian.net/wiki/spaces/DOC/pages/24215566/Kieker+Tools

- collector Can receive events via binary TCP and other means and store
them in a Kieker log file or any other Kieker writer storage.
- convert-logging-timestamp convert the logging timestamp of events.
- log-replayer replay log files
- resource-monitor monitor resource utilization.
- trace-analysis perform a trace analysis on Kieker monitoring data
- trace-analysis-gui a graphical UI controlling the trace-analysis tool
