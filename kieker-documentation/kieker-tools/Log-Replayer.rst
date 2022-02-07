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

usage: log-replayer -i <dir1...dirN> -h <hostname> -p <port> [-n] [-d <factor>] [-c <count>] [-V] [-r] [-bd <yyyyMMdd-HHmmss>] [-ad <yyyyMMdd-HHmmss>]

===== ============================ ======== ===================================================================================================================================
Short Long Option                  Required Description
===== ============================ ======== ===================================================================================================================================
-i    --input                      true     Log directories to read data from
-h    --host                       true     Name or IP address of the host where the data is send to
-p    --port                       true     Output port where the records as send to
-n    --no-delay                   false    Read and send events as fast as possible. (default send in the defined speed)
-d    --delay                      false    Delay factor. Default is 1 = realtime, 2 = twice the speed/half of the delay.
-c    --count                      false    Show count of events sent. Display count every n-th event.
-V    --verbose                    false    Verbosely prints additional information
-r    --time-rewrite               false    Set event timestamps relative to present time.
-bd   --ignore-records-before-date false    Records logged before this date (UTC timezone) are ignored (disabled by default) yyyyMMdd-HHmmss
-ad   --ignore-records-after-date  false    Records logged after this date (UTC timezone) are ignored (disabled by default) yyyyMMdd-HHmmss", converter = DateConverter.class)
===== ============================ ======== ===================================================================================================================================

Example
-------

The following command replays the monitoring testdata included in the
binary release to another directory:

.. code::
  
  log-replayer --input
     examples/userguide/ch5–trace-monitoring-aspectj/testdata/kieker-20100830-082225522-UTC
     -h localhost -p 5678

Listing A.3: Execution under UNIX-like systems

.. code::
  
  log-replayer --input
     ..\examples\userguide\ch5–trace-monitoring-aspectj\testdata\kieker-20100830-082225522-UTC
     -h localhost -p 5678

Listing A.4: Execution under Windows

