.. _kieker-tools-collector:

Collector
=========

The **Collector** supports any ISourceCompositeStage compatible event
source provided by Kieker tools, including TCP binary streams and file storages.
As sinks, the collector supports all Kieker writers based on the ``DataSinkStage``
from Kieker. The *Collector* is configured via a configuration file. The
configuration file consists of three parts one for basic Kieker settings, one
for the source and one for the sink.

The Collector can be used as a collector of runtime events via, e.g., TCP and
HTTP. It also can replay events from filesystem monitoring logs created by
Kieker.Monitoring. Example applications are:

-  Merging multiple directories containing monitoring data into a single
   output directory.
-  Importing a filesystem monitoring log to another monitoring log,
   e.g., a database. Therefore, an appropriate Kieker. Monitoring
   configuration file must be passed to the script.
-  Replaying a recorded filesystem monitoring log in real-time (or
   faster/slower) in order to simulate incoming monitoring data from a
   running system, e.g., via JMS.


Currently supported event sources (readers):

-  :ref:`architecture-receive-events-via-tcp`
-  :ref:`architecture-receive-events-from-log-files`
-  :ref:`architecture-receive-events-via-http`

Usage
-----

usage: collector [-a <factor>] [-c <monitoring configuration>] 
[-d] [-h] [-i <dir1 ... dirN>]
[--ignore-records-after-date <yyyyMMdd-HHmmss>]
[--ignore-records-before-date <yyyyMMdd-HHmmss>] [-k <true|false>]
[-n <num>] [-r <true|false>] [-v]

===== =========================================================== ======== =================================================================================================================================================================================================
Short Long Option                                                 Required Description
===== =========================================================== ======== =================================================================================================================================================================================================
-D    --delay <factor>                                            false    Factor by which to accelerate (>1.0) or slow down (<1.0) the replay in real time mode (defaults to 1.0, i .e ., no acceleration /slow down).
-c    --configuration <configuration file>                        false    Configuration file for Kieker stages
-h    --help                                                      false    prints the usage information for the tool , including available options
-i    --input <dir1 ... dirN>                                     false    Log directories to read data from
      --ignore-records-after-date <yyyyMMdd-HHmmss>               false    Records logged after this date (UTC timezone) are ignored (disabled by default)
      --ignore-records-before-date <yyyyMMdd-HHmmss>              false    Records logged before this date (UTC timezone) are ignored (disabled by default)
-r    --time-rewrite                                              false    Replay, but rewrite timestamps in records when specified
-n    --no-delay                                                  false    Replay log data without delay 
-v    --verbose                                                   false    verbosely prints additional information
===== =========================================================== ======== =================================================================================================================================================================================================

In the following, a long explanation of the settings is given in their
command line way and the name of the option for the Kieker configuration
file:

-D, --delay <factor>, kieker.tools.delay.factor
  Factor by which to accelerate (>1.0) or slow down (<1.0) the replay in real time mode
  (defaults to 1.0, i.e., no acceleration and slow down. The configuration option is
  a floating point number. The setting has no effect when used together with -n (no delay)
-c, --configuration <configuration file>
  Specify the kieker configuration file to use.
-h, --help
  Print out a help page.
-i, --input, kieker.tools.source.LogsReaderCompositeStage.logDirectories
  specify one or more Kieker log directories. On command line separated by space
  in the configuration file, the Kieker path configuration applies, i.e., each
  directory is separated by a colon :
--ignore-records-after-date, kieker.tools.ignore.records.after.date
  Records logged after this date (UTC timezone) are ignored (disabled by default)
--ignore-records-before-date <yyyyMMdd-HHmmss>, kieker.tools.ignore.records.before.date
  Records logged before this date (UTC timezone) are ignored (disabled by default)
-r, --time-rewrite, kieker.tools.time.rewrite
  Rewrite the timestamp to the current time, if specified. In the configuration file
  you can also define `false` 
-V, --verbose, kieker.tools.verbose
  Verbosely prints additional information
  
If -i is missing, the collector uses the configuration from the monitoring configuration.
In case -c is missing, the collector uses the default kieker configuration for reading and for writing.


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


The following command replays the monitoring testdata included in the
binary release to another directory:

.. code::
  
  log-replayer --inputdirs
     examples/userguide/ch5–trace-monitoring-aspectj/testdata/kieker-20100830-082225522-UTC
     --keep-logging-timestamps true
     --realtime false

Listing A.3: Execution under UNIX-like systems

.. code::
  
  log-replayer --inputdirs
     ..\examples\userguide\ch5–trace-monitoring-aspectj\testdata\kieker-20100830-082225522-UTC
     --keep-logging-timestamps true
     --realtime false

Listing A.4: Execution under Windows

