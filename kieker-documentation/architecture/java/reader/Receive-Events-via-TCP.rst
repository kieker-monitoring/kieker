.. _architecture-receive-events-via-tcp:

Receive Events via TCP from Multiple Sources (MultipleConnectionTcpSourceCompositeStage) 
========================================================================================

Created by Reiner Jung, last modified on Nov 18, 2019

Allows multiple probes from different execution environments to send
their events to one stage.

-  All data is received in a binary format using network byte order (big
   endian)
-  Strings and class names are transmitted once for each kind and are
   stored in a registry with an id. This allows to encode events with
   numerical values only.
-  New registry entries are transmitted before the first event using it

Format of registry entries

-  Indicator that this is not a record (value is -1) : int (32bit)
-  Registry id : int (32bit)
-  Length of the string in bytes : int (32bit) ; the length in bytes may
   not correspond with the number of characters, depending on the
   encoding (e.g. utf-8)
-  Bytes representing the string in a specific encoding (default is
   utf-8) : byte[] (8bit)

Format of a record

-  Id of the record type : int (32bit) ; corresponds to the id of the
   class name stored in the string registry
-  All values are in binary using the default Java representation
-  Strings are represented as int (32bit) referring to a registry entry

Further details can be found

-  Source code
   `MultipleConnectionTcpSourceCompositeStage.java <https://github.com/kieker-monitoring/kieker/blob/master/kieker-tools/src/kieker/tools/source/MultipleConnectionTcpSourceCompositeStage.java>`_
-  Java Doc
