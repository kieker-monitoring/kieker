.. _tutorials-how-to-pass-the-monitoring-configuration-to-kieker:

How to pass the monitoring configuration to Kieker 
==================================================

The Kieker Monitoring Controller checks several locations for the kieker
configuration. Initially, Kieker tries to
read ``META-INF/kieker.monitoring.default.properties`` file. If it
cannot read this file it uses the built in defaults for the
configuration. Subsequently, Kieker checks whether the
``kieker.monitoring.configuration`` JVM parameter is set and tries to
load the configuration from there.

To provide an alternative location for a Kieker configuration in context
of command line applications, please
add ``-Dkieker.monitoring.configuration=FULL_PATH_TO_LOCATION`` to the
java set of parameters, e.g.,
``java -Dkieker.monitoring.configuration=/myconfiguration -jar MyApplication.jar``

For war file, add your configuration to the ``META-INF`` folder or pass
the property to the server, e.g., tomcat.
 
