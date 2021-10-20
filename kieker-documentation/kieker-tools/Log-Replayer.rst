.. _kieker-tools-log-replayer:

Log Replayer 
============

Replays filesystem monitoring logs created by Kieker.Monitoring. Example
applications are:

-  Merging multiple directories containing monitoring data into a single
   output directory.
-  Importing a filesystem monitoring log to another monitoring log,
   e.g., a database. Therefore, an appropriate Kieker. Monitoring
   configuration file must be passed to the script.
-  Replaying a recorded filesystem monitoring log in real-time (or
   faster/slower) in order to simulate incoming monitoring data from a
   running system, e.g., via JMS.

Usage
-----

usage: kieker.tools.logReplayer.FilesystemLogReplayerStarter [-a
<factor>] [-c <\path\to\monitoring.properties>] [-d] [-h] [-i <dir1
...dirN>]

[--ignore-records-after-date <yyyyMMdd-HHmmss>]
[--ignore-records-before-date <yyyyMMdd-HHmmss>] [-k <true|false>]

[-n <num>] [-r <true|false>] [-v]

===== =========================================================== ======== =================================================================================================================================================================================================
Short Long Option                                                 Required Description
===== =========================================================== ======== =================================================================================================================================================================================================
-a    --realtime-acceleration-factor <factor>                              Factor by which to accelerate (>1.0) or slow down (<1.0) the replay in real time mode (defaults to 1.0, i .e ., no acceleration /slow down).
-c    --monitoring.configuration <\path\to\monitoring.properties>          Configuration to use for the Kieker monitoring instance
-d    --debug                                                     false    prints additional debug information
-h    --help                                                      false    prints the usage information for the tool , including available options
-i    --inputdirs <dir1 ... dirN>                                          Log directories to read data from
      --ignore-records-after-date <yyyyMMdd-HHmmss>                        Records logged after this date (UTC timezone) are ignored (disabled by default).
      --ignore-records-before-date <yyyyMMdd-HHmmss>                       Records logged before this date (UTC timezone) are ignored (disabled by default).
-k    --keep-logging-timestamps <true|false>                      true     Replay the original logging timestamps ( defaults to true )?
-n    --realtime-worker-threads <num>                             1        Number of worker threads used in realtime mode
-r    --realtime <true|false>                                              Replay log data in realtime
-v    --verbose                                                            verbosely prints additional information
===== =========================================================== ======== ============================================================================================================================================

Example
-------

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

