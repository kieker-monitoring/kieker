.. _tutorials-how-to-configure-kieker-within-java-applications:

How to configure Kieker within Java-Applications and -Services 
==============================================================

There are three scenarios where Kieker configuration parameters can be
used Java applications and must be configured on command line or startup
scripts.

1. Normal Java application without Kieker parts which should be
   instrumented and observed
2. Java applications which has already Kieker integrated or woven in
   with AspectJ
3. Java application which uses Kieker directly and accepts an Kieker
   property file

Normal Java Application
-----------------------

This works largely like the second option. However, you have to add the
Kieker agent to the java invocation.

Application with integrated Kieker at Compile or Bundling Time
--------------------------------------------------------------

In case you have an application which need Kieker configuration
parameters set, but which does not provide a command line option for
such configuration file can add
``-Dkieker.monitoring.configuration=$CONFIGUATION_FILE`` to the java
invocation statement. In many
`gradle <https://docs.gradle.org/current/userguide/application_plugin.html>`_-based
builds this can be achieved by using the ``*_OPTS`` environment
variable. The * represents the name of the tool.

Kieker-based Application
------------------------

Add your configuration parameters to the application's configuration
file
