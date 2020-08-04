.. _kieker-tools-collector:

Collector - Kieker Data Bridge 
===============================================

Created by Reiner Jung, last modified on Apr 23, 2020

There are two bridges available in Kieker. The new version is called
**Collector** and the old deprecated one is called **Kieker Data
Bridge** (KDB). This is the documentation for the new *Collector*. For
the old KDB please refer to its usage message for help.

The C\ *ollector* supports any ISourceCompositeStage compatible event
source provided by Kieker tools. As sinks, the collector supports all
Kieker writers based on the ``DataSinkStage`` from Kieker. The
C\ *ollector* is configured via a configuration file. The configuration
file consists of two parts one for the source and one for the sink. Here
is an example accepting events via TCP and writing them to a Kieker log
directory.

Currently supported event sources (readers):

-  :ref:`architecture-receive-events-via-tcp`
-  :ref:`architecture-receive-events-from-log-files`
-  :ref:`architecture-receive-events-via-http`

===== =============== ======== ==================
Short Long Option     Required Description
===== =============== ======== ==================
-c    --configuration true     Configuration file
===== =============== ======== ==================

Example configuration
---------------------

Receive data via TCP (binary encoding) and log the files without 
compression to text files.

.. code-block:: shell
  
  # TCP servcer for multiple connections
  
  ## Define input
  
  kieker.tools.source=kieker.tools.source.MultipleConnectionTcpSourceCompositeStage
  kieker.tools.source.MultipleConnectionTcpSourceCompositeStage.port=9876
  kieker.tools.source.MultipleConnectionTcpSourceCompositeStage.capacity=8192
  
  ## Kieker settings
  
  ## The name of the Kieker instance.
  kieker.monitoring.name=KIEKER
  
  ## Auto detect hostname for the writer
  kieker.monitoring.hostname=
  
  ## Output metadata record
  kieker.monitoring.metadata=true
  
  ## Define output
  
  ## Data sink stage (FileWriter)
  kieker.monitoring.writer=kieker.monitoring.writer.filesystem.FileWriter
  
  ## FileWriter settings
  ## output path
  kieker.monitoring.writer.filesystem.FileWriter.customStoragePath=$COLLECTOR_DATA_DIR/
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
different Kieker writer. For more details on the writer see `architecture-java-file-writer`.
