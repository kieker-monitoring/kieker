.. _quick_start_guide:

Quick Start Guide
=================

This tutorial discusses how to get traces from a small Java application packaged
in a jar-file and subsequently create graphics from the collected monitoring
data.

Monitoring
----------

- For this tutorial, we assume your program is storad in ``MyJar.jar``

- Download Kieker complete archive https://github.com/kieker-monitoring/kieker/releases/download/1.15.4/kieker-1.15.4-binaries.zip
- Extract the package
- Copy ``kieker-1.15.4-aspectj.jar`` from the ``build/libs/ directory`` into the same directory as your jar file.
- Execute ``java -javaagent:kieker-1.15.4-aspectj.jar -jar MyJar.jar``

Kieker writes the monitoring log files into the system’s default temporary 
directory (e.g. ``/tmp/``) in a directory named ``kieker-<date>-<timestamp>-UTC.``
The precise path can be found in the console output.

Alternatively, you can specify the path to the kieker property file
``kieker.properties`` and the AspectJ file ``aop.xml`` as follows:

``java -javaagent:kieker-1.15.4-aspectj.jar -Dkieker.monitoring.configuration=file://path/to/kieker.properties -Dorg.aspectj.weaver.loadtime.configuration=file://path/to/aop.xml -jar MyJar.jar``

Analysis
--------

Under Linux you should use in the following the corresponding .sh-scripts instead of the .bat-scripts.

- Install Graphviz and make sure that the binaries are accessible via the system’s path.
- Execute ``bin\trace-analysis.bat -i <temporary directory>\kieker-<date>-<timestamp>-UTC -o . --plot-Aggregated-Assembly-Call-Tree --plot-Assembly-Component-Dependency-Graph``
- Execute ``bin\dotPic-fileConverter.bat . png``



