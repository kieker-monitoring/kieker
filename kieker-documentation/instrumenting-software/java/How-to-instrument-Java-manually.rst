.. _tutorial-how-to-instrument-java-manually:

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

```gradle

repositories {
   mavenCentral()
}

dependencies {
   [...]
   implementation "net.kieker-monitoring:kieker:1.16-SNAPSHOT"
}
```

Adding dependencies in Maven
----------------------------

In Maven you have to add a maven dependency in the dependencies section. Be
aware to specify the version you are really interested in.

```xml
<dependencies>
   <!-- https://mvnrepository.com/artifact/net.kieker-monitoring/kieker -->
   <dependency>
      <groupId>net.kieker-monitoring</groupId>
      <artifactId>kieker</artifactId>
      <version>1.16-SNAPSHOT</version>
   </dependency>
</dependencies>
```

Applying Kieker In Java
-----------------------

To add Kieker instrumentation you can follow this example:

```java
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

```

q
