.. _kieker-tools:

Kieker Tools 
============

All tools can be found in the binary bundle
``(kieker-$VERSION-binaries.zip``) in the ``tools`` directory. The
``tools`` directory contains a set of tools prepacked as tar and zip
archives. Each archive contains one tool with all its libraries and
start scripts. The start scripts are located in the ``bin`` directory
and the libraries in the ``lib`` directory. In the tool root directory,
e.g.,\ ``trace-analysis-1.14``, you can find a ``log4j.cfg`` file, used
to configure the logging output for your tool. The ``bin`` directory
contains two scripts one named after the tool usable in Linux, FreeBSD,
MacOS, etc. and one with ``.bat`` extension for Windows.

To change the logging setup you can either change that file or define
additional options with the JAVA_OPTS environment variable, e.g.,

``export JAVA_OPTS="-Dlog4j.configuration=file:///full/path/to/logger/config/log4j.cfg"``

or use the tool specific ``_OPTS`` variable, e.g.,
``TRACE_ANALYSIS_OPTS`` for the ``trace-analysis`` tool.

Furthermore, you can use both variables to pass additional JVM
parameters and options to a tool.

-  `WebGUI <WebGUI.rst>`_
-  `Trace Analysis Tool <Trace-Analysis-Tool.rst>`_
-  `Convert Logging Timestamps <Convert-Logging-Timestamps.rst>`_
-  `Log Replayer <Log-Replayer.rst>`_
-  `Collector - Kieker Data Bridge <Collector---Kieker-Data-Bridge.rst>`_
-  `Resource Monitor <Resource-Monitor.rst>`_
-  `Trace Analysis -- GUI <Trace-Analysis----GUI.rst>`_
-  `Dot-Pic File Converter <Dot-Pic-File-Converter.rst>`_

