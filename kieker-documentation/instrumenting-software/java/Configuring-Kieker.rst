.. _instrumenting-software-java-configuration:

Configuring Kieker
==================

- Kieker uses a configuration file kieker.monitoring.properties
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
