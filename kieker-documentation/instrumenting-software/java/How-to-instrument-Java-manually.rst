.. _instrumenting-software-java-how-to-instrument-java-manually:

How to instrument Java manually
===============================

For manual instrumentation you have to modify the code of your application.
This is usually only a fallback option or for testing purposes. To get Kieker
into a Java project you can either access a Maven repository in your, e.g.,
Maven or Gradle build, or add the dependency manually.

Adding dependencies in Gradle
-----------------------------

In Gradle you have to add one line to the list of dependencies and include 
Maven Central into your repositories. Be aware to specify the version you are
really interested in.

.. code-block:: gradle
   
   repositories {
      mavenCentral()
   }
   
   dependencies {
      [...]
      implementation "net.kieker-monitoring:kieker:1.16-SNAPSHOT"
   }

Adding dependencies in Maven
----------------------------

In Maven you have to add a maven dependency in the dependencies section. Be
aware to specify the version you are really interested in.

.. code-block:: xml

   <dependencies>
      <!-- https://mvnrepository.com/artifact/net.kieker-monitoring/kieker -->
      <dependency>
         <groupId>net.kieker-monitoring</groupId>
         <artifactId>kieker</artifactId>
         <version>1.16-SNAPSHOT</version>
      </dependency>
   </dependencies>

Applying Kieker In Java
-----------------------

To add Kieker instrumentation you can follow this example:

.. code-block:: java

   public class Bookstore {

        private static final IMonitoringController MONITORING_CONTROLLER =
                        MonitoringController.getInstance();
        private final Catalog catalog = new Catalog();
        private final CRM crm = new CRM(this.catalog);

        public void searchBook() {
                // 1.) Call Catalog.getBook() and log its entry and exit timestamps.
                final long tin = MONITORING_CONTROLLER.getTimeSource().getTime();
                this.catalog.getBook(false); // <-- the monitored execution
                final long tout = MONITORING_CONTROLLER.getTimeSource().getTime();

                final OperationExecutionRecord e = new OperationExecutionRecord(
                                "public void " + this.catalog.getClass().getName() + ".getBook(boolean)",
                                OperationExecutionRecord.NO_SESSION_ID,
                                OperationExecutionRecord.NO_TRACE_ID,
                                tin, tout, "myHost",
                                OperationExecutionRecord.NO_EOI_ESS,
                                OperationExecutionRecord.NO_EOI_ESS);
                MONITORING_CONTROLLER.newMonitoringRecord(e);

                // 2.) Call the CRM catalog's getOffers() method (without monitoring).
                this.crm.getOffers();
        }
   }


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

.. code-block::
    
  MONITORING_CONTROLLER.newMonitoringRecord(e);
