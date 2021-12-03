.. _kieker-tools-kdb:

Kieker Data Bridge 
==================

.. note::
  
  The Kieker Data Bridge is deprecated. Use instead the collector.

The Kieker Data Bridge (KDB) is designed to support different sources of
monitoring records and to be embeddable in other tools such as Eclipse.
In general the KDB receives records online through a connector, converts
them to Kieker IMonitoringRecords and serializes them with a Kieker
writer.

Architecture
------------

The architecture of the KDB comprises the ServiceContainer and various
connectors implementing the IServiceConnector interface.



The IServiceConnector interface has three methods

-  initialize() which initializes a remote connection
-  close() which terminates the connection
-  deserializeNextRecord() which receives one new record

The initialize() method may block until the connection is established or
an error occurs. However, it can also be implemented in a non-blocking
way. The deserializeNextRecord() method must be blocking until a record
is received, an error occurs, or the connection is terminated.

The ServiceContainer comprises of a constructor and six service methods.
The constructor requires a Kieker Configuration, a connector and a
respawn flag. The latter flag is a debatable construct, but is allows to
trigger reconnects for simple connectors.

CLIServerMain
-------------

The KDB comes with a command line server including the bridge with a
wide set of parameters:

usage: cli-kieker-service [-c <configuration>] [-d] [-h <hostname>] -L

<paths> [-l <jms-url>] -m <map-file> [-p <number>] [-s] -t <type>

[-u <username>] [-v <arg>] [-w <password>]

--c,--configuration <configuration>

kieker configuration file

--d,--daemon

detach from console; TCP server allows multiple connections

--h,--host <hostname>

connect to server named <hostname>

--L,--libraries <paths>

List of library paths separated by :

--l,--url <jms-url>

URL for JMS server

--m,--map <map-file>

Class name to id (integer or string) mapping

--p,--port <number>

listen at port (tcp-server or jms-embedded) or connect to port

(tcp-client)

--s,--stats

output performance statistics

--t,--type <type>

select the service type: tcp-client, tcp-server,

tcp-single-server, jms-client, jms-embedded

--u,--user <username>

user name for a JMS service

--v,--verbose <arg>

output processing information

--w,--password <password>

password for a JMS service

In addition the connectors can be initialized via a Configuration object
determined by the -c option.

Connectors
----------

The KDB supports five different connectors which are described here
briefly. The selection of the connector can by done via the --type
property on command line or via a Kieker-configuration file entry:

kieker.tools.bridge.connector=FullQualifiedClassName

TCPClientConnector
~~~~~~~~~~~~~~~~~~

Connects to a remote site specified by a host name and a port. The
connector reads then binary data and reconstructs records. The two
configuration properties can either be specified as a command line
option or via the Kieker-configuration file by:

kieker.tools.bridge.connector.tcp.TCPClientConnector.hostname=HOSTNAME

kieker.tools.bridge.connector.tcp.TCPClientConnector.port=PORT

TCPSingleServerConnector
~~~~~~~~~~~~~~~~~~~~~~~~

Sets up a server for a single connection in initialize() and waits for a
connection. After the connections is established the method terminates,
and the ServiceContainer calls repeatedly the deserializeNextRecord
method. The connector requires a port number for its port. The
configuration property can either be specified by a command line option
or via the Kieker-configuration file by:

kieker.tools.bridge.connector.tcp.TCPSingleServerConnector.port=PORT

TCPMultiServerConnector
~~~~~~~~~~~~~~~~~~~~~~~

Sets up a server for multiple connection in initialize(). The method
starts a port listener thread and exits immediately afterwards. If a
connection is established from a client, a connection listener thread is
started to handle the incoming data and create records. Completed
records are transferred into a queue, which emptied by repeated calles
to the deserializeNextRecord method. The connector requires a port
number for its port. The configuration property can either be specified
by a command line option or via the Kieker-configuration file by:

kieker.tools.bridge.connector.tcp.TCPMultiServerConnector.port=PORT

JMSClientConnector
~~~~~~~~~~~~~~~~~~

The JMSClientConnector supports text and binary messages.

kieker.tools.bridge.connector.jms.JMSClientConnector.username=USERNAME

kieker.tools.bridge.connector.jms.JMSClientConnector.password=PASSWORD

kieker.tools.bridge.connector.jms.JMSClientConnector.uri=ServiceURI

JMSEmbeddedConnector
~~~~~~~~~~~~~~~~~~~~

the JMSEmbeddedConnector supports text and binary messages. Its primary
difference to the normal JMSClientConnector is its integrated JMS
service. However, the connector is dysfunctional at the moment.

Network Transport Format
------------------------

At present the connectors of the KDB use either a binary or a textual
format. It is allowed to extend this by other formats if necessary.

Binary Format
~~~~~~~~~~~~~

The binary format uses network byte order (big-endian). Each record
starts with an initial record id coded in an integer (int32). Negative
numbers are reserved for system commands, while all IMonitoringRecord
type use positive user defined values (including 0). A record may
comprise various fields, which are encoded in big-endian for integer
values (byte, short, integer, long, char) and IEEE encoding for float
and double. Strings are represented by an integer (int32) defining the
length and a sequence of bytes representing the string.

Text Format
~~~~~~~~~~~

The text format encodes all properties in one string. Values are
separated by a semicolon (;). The record id is stored as the first value
in such string.

0;1253453456345;1523453256345;public myMethod()

This example shows a record with three values and a record id (0) as
prefix value.

Connector Interface
-------------------

The implementation of new connectors must adhere the IServiceConnector
interface providing methods to initialize, transmit and close the
connector. Furthermore it should inherit the AbstractConnector class for
basic setup. Finally each connector must be annotated with the
ConnectorProperty annotation to specify properties used in the command
line version or the Eclipse plugin.

.. code-block::
   
   @ConnectorProperty(cmdName = "my-service", name = "My Service Demo
   Connector", description = "example connector for documentation.")
   public class MyServiceConnector extends AbstractConnector {

As the connector uses the normal Kieker Configuration object for
configuration, the different settings require Configuration property
names and should use private properties in the class to hold the values.

.. code-block::
   
   /** Property name for the host name of the record source. */
   
   public static final String PROPERTY =
       MyServiceConnector.class.getCanonicalName() + ".property";
   
   private String property;

In the constructor, first the configuration is passed to the super
constructor and then the properties are setup.

.. code-block::
   
   /**
    * Create a MyServiceConnector. 
    * @param configuration
    *      Kieker configuration including setup for connectors
    * @param lookupEntityMap
    * I    MonitoringRecord constructor and TYPES-array to id map
    */
   public MyServiceConnector(final Configuration configuration, final
      ConcurrentMap<Integer, LookupEntity> lookupEntityMap) {
      super(configuration, lookupEntityMap);
      this.property =
          this.configuration.getStringProperty(MyServiceConnector.PROPERTY);
   }

The remaining connector comprises the three methods from the
IServiceConnector interface. The methods all may throw a
ConnectorDataTransmissionException indicating that some error occurred.
The real exception is added to the ConnectorDataTransmissionException on
creation in the connector. This allows to use a defined exception type
instead of Exception.

The initialize() method can be implemented blocking or non blocking. It
throws a ConnectorDataTransmissionException if no connection could be
established.

.. code-block::
   
   /**
    * Create the connection
    *
    * @throws ConnectorDataTransmissionException
    *     if the initialization fails
    */
   public void initialize() throws ConnectorDataTransmissionException {
      // initialization code, establish connection
   }

The close() method must terminate the connection. If queues must be
freed, then this routine has to do it. On error the method can produce a
ConnectorDataTransmissionException exception.

.. code-block::
   
   /**
    * Closes the connection
    * 
    * @throws ConnectorDataTransmissionException
    *     if an IOException occurs during the close operation
    */
   public void close() throws ConnectorDataTransmissionException {
      // terminate connection
   }

The deserializeNextRecord() method blocks until is able to read one new
record. If you want to implement a multi-record transmit channel, then
can do so, but must store the results in a buffer, which is then read on
every call of deserializeNextRecord() returning one received record
after another.

.. code-block::
   
   /**
    * De-serialize an object reading from the input stream.
    *
    * @return the de-serialized IMonitoringRecord object or null if the stream
    *     was terminated by the client.
    *
    * @throws ConnectorDataTransmissionException
    *     when a record is received that ID is unknown or 
    *     the deserialization fails
    * @throws ConnectorEndOfDataException
    *     when the other end hung up or the data stream ends of another reason
    */
   public IMonitoringRecord deserializeNextRecord() throws
       ConnectorDataTransmissionException, ConnectorEndOfDataException {
       // read structure ID
       try {
          final Integer id = ... ; // get id for the record final LookupEntity
          recordProperty = this.lookupEntityMap.get(id);
          if (recordProperty != null) {
             final Object[] values = new
                Object[recordProperty.getParameterTypes().length];
             
             // process and or receive record data 
             // - fill the values array. This could also be handled differently.
             // return new record return
             recordProperty.getConstructor().newInstance(values);
          } else {
            throw new ConnectorDataTransmissionException("Record type " + id + 
               " is not registered.");
          }
       } catch (... e) {
         throw new ConnectorEndOfDataException("End of stream", e);
       }
   }

