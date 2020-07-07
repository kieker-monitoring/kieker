.. _instrumenting-software-manual:

Manual Instrumentation 
======================

Manual instrumentation is usually not the right way to instrument larger
applications. However, to inspect smaller portions of an application in
an ad-hoc manner or in cases aspect-weaving is not possible, manual
instrumentation can be a viable option.

To use Kieker with an Java application, you have to add the dependency
to your build system, e.g., (in gradle)

.. code:: shell
  
  compile 'net.kieker-monitoring:kieker:1.14'

See also https://mvnrepository.com/artifact/net.kieker-monitoring/kieker

Instrumentation requires three key elements:
- A ``MonitoringController``
- Data collection
- Logging of the data

Monitoring Controller
'''''''''''''''''''''

The ``MonitoringController`` provides basic facilities for monitoring
and logging, including a source for timestamps. It can be obtained in
any class by

.. code-block:: java
  
  private static final IMonitoringController MONITORING_CONTROLLER =
		MonitoringController.getInstance();
		
This returns a singleton instance of the monitoring controller.

Data Collection
'''''''''''''''

Usually in data collection you gather all information you want to store
and put that data into an instance of an event type.

.. code-block:: java
  
  final long tin = MONITORING_CONTROLLER.getTimeSource().getTime();
  final String operationName = "public void exampleOp()"
  final String className = this.getClass().getName();

In this example, the first line uses the time source facility of the
``MonitoringController`` to gain the current time. Subsequent, two
strings are defined which represent the name of the operation (method)
and the name of the class the method resides in. Finally, the data must
be packed into a event type.

.. code-block:: java
   
   final BeforeOperationEvent e = 
   	new BeforeOperationEvent(tin, className, operationName);


Logging of Data
'''''''''''''''

The last step uses the logging facility of the ``MonitoringController``.

.. code-block:: java
  
  MONITORING_CONTROLLER.newMonitoringRecord(e);

