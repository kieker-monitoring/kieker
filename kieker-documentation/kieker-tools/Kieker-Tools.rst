.. _kieker-tools:

Kieker Tools 
============

All tools can be found in the binary bundle
``(kieker-2.0.0-SNAPSHOT-binaries.zip``) in the ``tools`` directory. The
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

``export JAVA_OPTS="-Dlogback.configurationFile=/full/path/to/logger/config/logback-trace.groovy"``
or use the tool specific ``_OPTS`` variable, e.g.,
``TRACE_ANALYSIS_OPTS`` for the ``trace-analysis`` tool.

Furthermore, you can use both variables to pass additional JVM
parameters and options to a tool.

-  :ref:`kieker-tools-behavior-analysis`
-  :ref:`kieker-tools-webgui` (deprecated)
-  :ref:`kieker-tools-trace-analysis-tool`
-  :ref:`kieker-tools-trace-analysis-gui` (deprecated)
-  :ref:`kieker-tools-convert-logging-timestamps`
-  :ref:`kieker-tools-log-replayer`
-  :ref:`kieker-tools-collector`
-  :ref:`kieker-tools-resource-monitor`

-  :ref:`kieker-tools-irl`
-  :ref:`kieker-tools-dot-pic-file-converter`

-  :ref:`kieker-tools-allen-upper-limit.rst`
-  :ref:`kieker-tools-cmi`
-  :ref:`kieker-tools-dar`
-  :ref:`kieker-tools-delta`
-  :ref:`kieker-tools-fxca`
-  :ref:`kieker-tools-maa`
-  :ref:`kieker-tools-mktable`
-  :ref:`kieker-tools-mop`
-  :ref:`kieker-tools-mt`
-  :ref:`kieker-tools-mvis`
-  :ref:`kieker-tools-relabel`
-  :ref:`kieker-tools-rewrite-log-entries`
-  :ref:`kieker-tools-sar`
-  :ref:`kieker-tools-static-analysis-formats`

Please note there are other tools available for Kieker which are not
bundled with Kieker.

