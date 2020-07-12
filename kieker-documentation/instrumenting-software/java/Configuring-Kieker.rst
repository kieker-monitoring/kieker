.. _instrumenting-software-java-configuration:

Configuring Kieker
==================

- Kieker uses a configuration file `kieker.monitoring.properties`
- Must be placed depending on application type (see related)
- Different options to log data (refer to real info where by crosslink)

.. todo::
   Add the following info to kieker configuration.

   -  CSV logging file (storage on the monitoring system)
   -  Binary logging file (storage on the monitoring system)
   -  Compressed logging file (binary and CSV, storage on the monitoring
      system)
   -  TCP binary stream (transfer to remote host)

      -  Kieker binary transport protocol (two TCP connections
         supporting a prioritized second channel)
      -  ExploreViz binary transport protocol (single TCP connection)
      -  Modern Kieker binary transport protocol (experimental)

   -  JMS object message transport (transfer to remote host)
   -  JMX transport via notifications (transfer to remote host)
   -  UNIX based named pipes (local transfer)
   -  Database writer (experimental)
   -  AMQP protocol message support (transfer to remote host)
   
**Kieker Monitoring** instances can be configured by properties files, 
**Configuration** objects, and by passing property values as JVM
arguments. If no configuration is specified, a default configuration is
used. 
The default configuration can be found here including documentation for
all properties. Additional information can be found within the
documentation of the **Monitoring Controller**, **Monitoring Probes**
and **Monitoring Writers**.
The default configuration properties file, which  can be used as a
template for custom configurations, is provided by the
file ``kieker.monitoring.example.properties`` in the directory
``examples/`` directory of the binary release.


Configurations for Singleton Instances
--------------------------------------

In order to use a custom configuration file, its location needs to be
passed to the JVM using the parameter *kieker.monitoring.configuration*
as follows:

.. code-block:: shell
  
  java -Dkieker.monitoring.configuration=<ANY-DIR>/my.kieker.monitoring.properties [...]

Alternatively, a file named ``kieker.monitoring.properties``
can be placed in a directory called ``META-INF/`` located in the classpath.
The available configuration properties can also be passed as JVM
arguments, e.g., ``-Dkieker.monitoring.enabled=true``.

Configurations for Non-Singleton Instances
------------------------------------------

The class **Configuration** provides factory methods to create
**Configuration** objects according to the default configuration
or loaded from a specified properties file: ``createDefaultConfiguration``,
``createConfigurationFromFile``, and ``createSingletonConfiguration``.
Note, that JVM parameters are only evaluated when using the factory method
``createSingletonConfiguration``.
The returned ``Configuration`` objects can be adjusted by setting
single property values using the method ``setProperty``.
