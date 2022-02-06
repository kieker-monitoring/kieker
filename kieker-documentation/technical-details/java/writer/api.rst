.. _technical-details-java-writer-api:

Java Writer API
===============

Monitoring writers serialize monitoring records to the monitoring 
log/stream and persist the recorded informations into files and databases
or send the events to an analysis service.
All writers must implement the interface **IMonitoringWriter**. The
monitoring controller passes the received records to an internal queue
which is connected to the **MonitoringWriterThread** instance. The
latter calls the ``writeMonitoringRecord`` method of the writer.

All writers must inherit **AbstractMonitoringWriter** which provides
basic functionality and defines the interface of all writers. The
API introduces a simple protocol for task to be performed on start and
when terminating, i.e.:

- Construction and configuration
- ``onStarting``
- ``writeMonitoringRecord`` (multiple calls)
- ``onTerminating``

The constructor can be used to access the **Kieker** configuration
object and configure parameters of the writer, e.g., buffer sizes, file
names etc.

.. code-block:: java
  
  public AbstractMonitoringWriter(final Configuration configuration) {
     this.configuration = configuration;
  }

On startup specific tasks must be performed. For example, in a file
writer, the file can be initialized.
 
.. code-block:: java
  
  public abstract void onStarting();

The main function is 
.. code-block:: java
  
  public abstract void writeMonitoringRecord(IMonitoringRecord record);

which is called every time a new record must be send or stored.

The last method is 
.. code-block:: java
  
  public abstract void onTerminating();

it is called at the end of the monitoring. In a file writer, this 
function would close the file.

There are several different writers available. Most prominent are the
**FileWriter** for text and binary files, and the **TcpWriter** to send
file to a logging or analysis service.

List of Writers:

.. toctree::
   :maxdepth: 1
   
   File-Writer.rst
   Single-Socket-TCP-Writer.rst

JavaDoc:

- `AmqpWriter <https://github.com/kieker-monitoring/kieker/blob/master/kieker-monitoring/src/kieker/monitoring/writer/amqp/AmqpWriter.java>`
- `ChunkingAmqpWriter <https://github.com/kieker-monitoring/kieker/blob/master/kieker-monitoring/src/kieker/monitoring/writer/amqp/ChunkingAmqpWriter.java>`
- `DumpWriter <https://github.com/kieker-monitoring/kieker/blob/master/kieker-monitoring/src/kieker/monitoring/writer/dump/DumpWriter.java>` (does not log anything)
- `ExplorVizTcpWriter <https://github.com/kieker-monitoring/kieker/blob/master/kieker-monitoring/src/kieker/monitoring/writer/explorviz/ExplorVizTcpWriter.java>` writer for ExplorViz
- `JmsWriter <https://github.com/kieker-monitoring/kieker/blob/master/kieker-monitoring/src/kieker/monitoring/writer/jms/JmsWriter.java>`
- `JmxWriter <https://github.com/kieker-monitoring/kieker/blob/master/kieker-monitoring/src/kieker/monitoring/writer/jmx/JmxWriter.java>`
- `PipeWriter <https://github.com/kieker-monitoring/kieker/blob/master/kieker-monitoring/src/kieker/monitoring/writer/namedRecordPipe/PipeWriter.java>`
- `PrintStreamWriter <https://github.com/kieker-monitoring/kieker/blob/master/kieker-monitoring/src/kieker/monitoring/writer/print/PrintStreamWriter.java>`

Current Kieker writer API uses always a fast pipe implementation to
decouple the writers from the data collection. This has been shown to
be the fastest setup for monitoring.

.. todo::
  
  Move these snippets to separate files in future.
  
JmsWriter and JmxWriter
-----------------------

The **JmsWriter* and **JmxWriter** write records to a
`Java Messaging Service (JMS) <https://en.wikipedia.org/wiki/Java_Message_Service>`
queue and `Java Management Extensions (JMX) <https://www.oracle.com/java/technologies/javase/javamanagement.html>`
queue respectively.

PipeWriter
----------

The **PipeWriter** allows to pass records via in-memory record streams
(named pipes). These writers allow to implement on-the-fly analysis in
distributed systems, i.e., analysis while continuously receiving new
monitoring data from an instrumented application potentially running
on another machine.

