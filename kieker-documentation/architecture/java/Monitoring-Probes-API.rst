.. _java_probe-api:

Monitoring Probes
=================

The probes are responsible for collecting the monitoring data and
passing it to the monitoring controller. Monitoring probes are highly
dependant on the technology used to implement them. Still, there is
a general pattern on how to implement probes.

In order to avoid multiple calls to the ``getInstance`` method of the     
**MonitoringController** class, a singleton instance should be stored     
in a final static variable.

.. code-block:: java
  
  private final static IMonitoringController CTRL =
     MonitoringController.getInstannce();

The probe code usually comes with a check whether monitoring and the
probe is active, followed by a sequence where data is collected, and
finished by a

.. code-block:: java
  
  if (CTRL.isMonitoringEnabled()) {
     final String signature = <get method signature>
     if (CTRL.isProbeActivated(signature)) {
        int entryTime = CTRL.getTimeSource().getTime();
        // collect further parameters
        
        CTRL.newMonitoringRecord(new Record(<parameters>));
     }
  }
  
  <continue call>

There are many predefined probes available for Kieker which can be found
in <https://github.com/kieker-monitoring/kieker/tree/master/kieker-monitoring/src/kieker/monitoring/probe>.
These can also be used as a starting point for self built probes.

