.. _kieker-tools-collector:

Collector
=========

Kieker supports for different programming languages and probes to send
monitoring data via TCP to a remote host. In distributed systems, this
can imply that multiple probes send data simultaneously. The Kieker
Collector allows to collect this data from different sources and stores them in
one kieker log, a database or relays it to analysis software.

The **Collector** supports any ISourceCompositeStage compatible event
source provided by Kieker tools, including TCP binary streams. As sinks,
the collector supports all Kieker writers based on the ``DataSinkStage`` from
Kieker. The *Collector* is configured via a configuration file. The
configuration file consists of three parts one for basic Kieker settings, one
for the source and one for the sink.

Currently supported event sources (readers):

-  :ref:`architecture-receive-events-via-tcp`
-  :ref:`architecture-receive-events-from-log-files`
-  :ref:`architecture-receive-events-via-http`

===== =============== ======== ==================
Short Long Option     Required Description
===== =============== ======== ==================
-c    --configuration true     Configuration file
===== =============== ======== ==================

Example configurations snippets
-------------------------------

The following snippets can be combined to configure the collector
application. A complete configuration must contain one general settings
block, one input configuration and one output configuration.

General setup parameter for Kieker.

.. code-block:: shell
  
  # Kieker settings
  
  ## The name of the Kieker instance.
  kieker.monitoring.name=KIEKER
  
  ## Auto detect hostname for the writer
  kieker.monitoring.hostname=
  
  ## Output metadata record
  kieker.monitoring.metadata=true


Receive data via TCP (binary encoding). This can be used to receive binary
logging data from Kieker in Java and C/C++/Fortran.

.. code-block:: shell
  
  # TCP servcer for multiple connections
   
  kieker.tools.source=kieker.tools.source.MultipleConnectionTcpSourceCompositeStage
  kieker.tools.source.MultipleConnectionTcpSourceCompositeStage.port=9876
  kieker.tools.source.MultipleConnectionTcpSourceCompositeStage.capacity=8192


Read another Kieker log. This can be useful to inspect binary logs, 
replay logs, make text based logs more compact and even compress them
in the process.

.. code-block:: shell
  
  # File reader
  
  kieker.tools.source=kieker.tools.source.LogsReaderCompositeStage
  kieker.tools.source.LogsReaderCompositeStage.logDirectories=$INPUT_DIR
  ## Buffer size
  kieker.tools.source.LogsReaderCompositeStage.bufferSize = 8192

Store the output in a Kieker log.

.. code-block:: shell

  # Define output
  
  ## Data sink stage (FileWriter)
  kieker.monitoring.writer=kieker.monitoring.writer.filesystem.FileWriter
  
  ## FileWriter settings
  ## output path
  kieker.monitoring.writer.filesystem.FileWriter.customStoragePath=$OUTPUT_DATA_DIR/
  kieker.monitoring.writer.filesystem.FileWriter.charsetName=UTF-8
  
  ## Number of entries per file
  kieker.monitoring.writer.filesystem.FileWriter.maxEntriesInFile=25000
  
  ## Limit of the log file size; -1 no limit
  kieker.monitoring.writer.filesystem.FileWriter.maxLogSize=-1
  
  ## Limit number of log files; -1 no limit
  kieker.monitoring.writer.filesystem.FileWriter.maxLogFiles=-1
  
  ## Map files are written as text files
  kieker.monitoring.writer.filesystem.FileWriter.mapFileHandler=kieker.monitoring.writer.filesystem.TextMapFileHandler
  
  ## Flush map file after each record
  kieker.monitoring.writer.filesystem.TextMapFileHandler.flush=true
  
  ## Do not compress the map file
  kieker.monitoring.writer.filesystem.TextMapFileHandler.compression=kieker.monitoring.writer.compression.NoneCompressionFilter
  
  ## Log file pool handler
  kieker.monitoring.writer.filesystem.FileWriter.logFilePoolHandler=kieker.monitoring.writer.filesystem.RotatingLogFilePoolHandler
  
  ## Text log for record data
  kieker.monitoring.writer.filesystem.FileWriter.logStreamHandler=kieker.monitoring.writer.filesystem.TextLogStreamHandler
  
  ## Do not compress the log file
  kieker.monitoring.writer.filesystem.TextLogStreamHandler.compression=kieker.monitoring.writer.compression.NoneCompressionFilter
  
  ## Flush log data after every record
  kieker.monitoring.writer.filesystem.FileWriter.flush=true
  
  ## buffer size. The log buffer size must be big enough to hold the biggest record
  kieker.monitoring.writer.filesystem.FileWriter.bufferSize=81920

Instead of generating text log files, you may use the
``BinaryLogStreamHandler`` to produce binary output. Also can specify a
compression algorithm for the log and map files, or use a totally
different Kieker writer. For more details on the writer see 
`architecture-java-file-writer`.

