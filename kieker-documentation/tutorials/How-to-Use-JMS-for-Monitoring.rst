How to use JMS Reader and Writer
================================

This is a short introduction to the JMS reader and writer of **Kieker**
named **AsyncJmsWriter** and **JmsReaderStage**. The directory
``examples/userguide/appendix-JMS/`` contains the sources, gradle
scripts etc. used in this example. It is based on the Bookstore
application with manual instrumentation presented getting-started_.

The following sections provide step-by-step instructions for the
ActiveMQ JMS server implementation.
The general procedure for this example is the following:

#. Download and prepare the respective JMS server implementation
#. Copy required libraries to the example directory
#. Start the JMS server
#. Start the analysis instance which receives records via JMS
#. Start the monitoring instance which sends records via JMS

.. note::
   
   Due to a bug in some JMS servers, avoid paths including white spaces.


ActiveMQ
--------

Download and Prepare ActiveMQ
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Download an ActiveMQ archive from <http://activemq.apache.org/download.html>
and decompress it to the base directory of the example. Note, that there
are two different distributions, one for Unix/Linux/Cygwin and another
one for Windows.

Under Unix-like systems, you need to set the executable-bit of the start
script:

.. code-block:: shell
   
   # chmod +x bin/activemq

Also under Unix-like systems, make sure that the file ``bin/activemq``
includes Unix line endings (e.g., using your favorite editor or the
`dos2unix` tool).

Copy ActiveMQ Libraries
~~~~~~~~~~~~~~~~~~~~~~~

Copy the following files from the ActiveMQ release to the
``lib/`` directory of this example:

#. ``activemq-all-<version>.jar`` (from ActiveMQ's base directory)
#. ``slf4j-log4j<version>.jar`` (from ActiveMQ's ``lib/optional`` directory)
#. ``log4j-<version>.jar`` (from ActiveMQ's ``lib/optional`` directory)


Kieker Monitoring Configuration for ActiveMQ
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

The file ``src-resources/META-INF/kieker.monitoring.properties-activeMQ``
is already configured to use the **JmsWriter** via ActiveMQ.
The important properties are the definition of the provider URL and the
context factory:

.. code-block:: shell
  
  kieker.monitoring.writer.jms.JmsWriter.ProviderUrl=tcp://127.0.0.1:61616/
  kieker.monitoring.writer.jms.JmsWriter.ContextFactoryType=org.apache.activemq.jndi.ActiveMQInitialContextFactory


Running the Example
~~~~~~~~~~~~~~~~~~~

The execution of the example is performed by the following three steps:

#. Start the JMS server (you may have to set your **JAVA_HOME** variable first):
   - ``bin/activemq start`` Start of the JMS server under UNIX-like systems
   - ``bin/activemq start`` Start of the JMS server under Windows
#. Start the analysis part (in a new terminal):
   - ``./gradlew runAnalysisActiveMQ`` Start the analysis part under UNIX-like systems
   - ``gradlew.bat runAnalysisActiveMQ``Start the analysis part under Windows
#. Start the instrumented Bookstore (in a new terminal):
   - ``./gradlew runMonitoringActiveMQ`` Start the analysis part under UNIX-like systems
   - ``gradlew.bat runMonitoringActiveMQ`` Start the analysis part under Windows

