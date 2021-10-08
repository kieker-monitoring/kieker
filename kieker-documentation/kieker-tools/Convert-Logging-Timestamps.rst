.. _kieker-tools-convert-logging-timestamps:

Convert Logging Timestamps 
==========================

The script converts Kieker.Monitoring logging timestamps, representing
the number of nanoseconds since 1 Jan 1970 00:00 UTC, to a
human-readable textual representation in the UTC and local timezones.

Usage
-----

kieker.tools.loggingTimestampConverter.LoggingTimestampConverterTool
[−d] [−h] [−t <timestamp1 ... timestampN>] [−v]

== ======================================== ===== =======================================================================
−d −−debug                                  false prints additional debug information
−h −−help                                   false prints the usage information for the tool , including available options
−t −−timestamps <timestamp1 ... timestampN>       List of timestamps (UTC timezone) to convert
−v −−verbose                                false verbosely prints additional information
== ======================================== ===== =======================================================================

Example
-------

The following listing shows the command to convert two logging
timestamps as well as the resulting output.

``convert-logging-timestamp --timestamps 1283156545581511026 1283156546127117246``

.. code::
  
  1283156545581511026: Mo, 30 Aug 2010 08:22:25 +0000 (UTC) (Mo, 30 Aug 2010 10:22:25 +0200 (local time))
  1283156546127117246: Mo, 30 Aug 2010 08:22:26 +0000 (UTC) (Mo, 30 Aug 2010 10:22:26 +0200 (local time))

Listing A.1: Execution under UNIX-like systems

``convert-logging-timestamp.bat --timestamps 1283156545581511026 1283156546127117246``

.. code::
  
  1283156545581511026: Mo, 30 Aug 2010 08:22:25 +0000 (UTC) (Mo, 30 Aug 2010 10:22:25 +0200 (local time))
  1283156546127117246: Mo, 30 Aug 2010 08:22:26 +0000 (UTC) (Mo, 30 Aug 2010 10:22:26 +0200 (local time))

Listing A.2: Execution under Windows
