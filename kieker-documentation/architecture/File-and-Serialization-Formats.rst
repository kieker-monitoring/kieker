.. _architecture-file-and-serialization-formats.

File and Serialization Formats 
==============================

While the Kieker architectures discuss the specific architectures of
Kieker in different programming languages, this section discusses the
language independent file formats and serialization formats, which helps
to implement new readers and writers for new and already supported
languages. As Kieker works with predefined records called events, these
types must be mentioned in serialization.

Binary Format Description
-------------------------

The binary serialization differs slightly from data streams to the file
system, as the file system uses two file types to store the information.
One to store all data type names and string, and one to store the binary
record information. The stream variant uses only one stream and sends
out all data type and string lookup data over the same stream as the
event payload. However, general serialization is done following the same
rules.

Binary Value Serialization
~~~~~~~~~~~~~~~~~~~~~~~~~~

Basic types are serialized as follows:

=======
====================================================================
======= =============== =============
boolean signed 8-bit (0 = false, 1 = true)                                   boolean (unsigned) char int          
byte    signed 8-bit                                                         byte    char            int          
short   signed 16-bit, big endian                                            short   short           int          
int     signed 32-bit, big endian                                            int     int             int          
long    signed 64-bit, big endian                                            long    long            int          
float   32-bit IEEE 754 floating point                                       float   float           float (64bit)
double  64-bit IEEE 754 floating point                                       double  double          float (64bit)
char    16-bit unicode character (unsigned)                                  char    short           int          
string  signed 32-bit, referring to the id of the string in the lookup table String  const char\*    str          
=======
====================================================================
======= =============== =============

Fixed size arrays are serialized by a sequence of entries with the exact
number of entries.

Variable arrays are prefixed by a int size indicator followed by as many
entries as specified in the size indicator.

Binary String Registry Serialization
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Each string or fully qualified event type name is serialized as follows:

-  int (signed) indicating the id number for the string or type name
-  int (signed) the length of the string in bytes
-  a sequence of bytes describing the string (note that the default
   encoding for strings is UTF-8)

Binary Event Type Serialization
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Each event is prefixed by

-  int (signed) for the type id
-  long (signed) for the logging time stamp
-  followed by all data

Text Format Description
-----------------------

| 
