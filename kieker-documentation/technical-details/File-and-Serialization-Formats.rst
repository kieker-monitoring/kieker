.. _technical-details-file-and-serialization-formats:

File and Serialization Formats
==============================

While the Kieker architectures discuss the specific architectures of
Kieker in different programming languages, this section discusses the
language independent file formats and serialization formats, which helps
to implement new readers and writers for new and already supported
languages. As Kieker works with predefined records called events, these
types must be mentioned in serialization.


Type Mapping
------------

=========== ======= =============== =============
Kieker Type Java    C               Python
=========== ======= =============== =============
boolean     boolean (unsigned) char int          
byte        byte    char            int          
short       short   short           int          
int         int     int             int          
long        long    long            int          
float       float   float           float (64bit)
double      double  double          float (64bit)
char        char    short           int          
string      String  const char*     str          
=========== ======= =============== =============

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

=========== ====================================================================
Kieker Type Serialization                                                       
=========== ====================================================================
boolean     signed 8-bit (0 = false, 1 = true)                                          
byte        signed 8-bit                                                                
short       signed 16-bit, big endian                                                   
int         signed 32-bit, big endian                                                     
long        signed 64-bit, big endian                                                    
float       32-bit IEEE 754 floating point                                      
double      64-bit IEEE 754 floating point                                      
char        16-bit unicode character (unsigned)                                           
string      signed 32-bit, referring to the id of the string in the lookup table          
=========== ====================================================================

Fixed size arrays are serialized by a sequence of entries with the exact
number of entries.

Variable arrays are prefixed by a int size indicator followed by as many
entries as specified in the size indicator.

Binary String Registry Serialization
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Each string or fully qualified event type name is serialized as follows:

-  int (signed) fixed value -1 to indicate a string value
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

.. todo::
   
   Missing text format description.

