.. _controlling-at-runime:

Controlling at Runtime
======================


JMX MBean Access to **MonitoringController**
--------------------------------------------

The **MonitoringController**'s interface methods can
be accessed as a JMX MBean. For example, this allows to control the
monitoring state using the methods described in
:ref:`technical-details-java-monitoring-controller-api`.
As a JMX-compliant graphical client that is included in the JDK,
``jconsole`` is probably the easiest way to get started. Just keep in
mind to add **Kieker** to the classpath when calling ``jconsole`` so
that the MBean works correctly. The Figure below
shows two screenshots of the MBean access using ``jconsole``.

.. figure:: ../../images/jmxbean-monitoringcontroller-attributes.png
   :width: 50%
   :align: left
   
   Attributes
   
.. figure:: ../../images/jmxbean-monitoringcontroller-operations.png
   :width: 50%
   :align: right

   Operations
   
Screenshots of the ``jconsole`` JMX client accessing the 
**MonitoringController**'s attributes and operations via the MBean
interface.

In order to enable JMX MBean access to the **MonitoringController**,
the corresponding configuration properties must be set to *true*
(listing below). The ``\monitoringPropertiesFile`` includes additional
JMX-related configuration properties.

.. code-block:: parameter

   ## Whether any JMX functionality is available
   kieker.monitoring.jmx=true
   ...

   ## Enable/Disable the MonitoringController MBean
   kieker.monitoring.jmx.MonitoringController=true
   ...

For remote access to the server, set *kieker.monitoring.jmx.remote=true*.
In this case it is recommended to set *com.sun.management.jmxremote.authenticate=true* as well.
More information can be found on Oracle's JMX Technology Home Page
<https://www.oracle.com/java/technologies/javase/javamanagement.html>.


