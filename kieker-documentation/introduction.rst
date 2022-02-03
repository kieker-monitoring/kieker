.. _introduction:

Introduction
============

The figure below the framework's composition based on the two main
components *KiekerMonitoringPart* and *KiekerAnalysisPart*.

.. image:: images/kieker-architecture-overview.svg
   :width: 600

- The *KiekerMonitoringPart* component is responsible for program
  instrumentation, data collection, and logging. Its core is the
  *MonitoringController*.
- The component *KiekerAnalysisPart* is responsible for reading,
  analyzing, and visualizing the monitoring data. Its core is the
  *AnalysisController* which manages the life-cycle of the
  pipe-and-filter architecture of analysis plugins, including monitoring
  readers and analysis filters.

Please note that older programs might use a *AnalysisController* setup
while new analyses and tools reply on :ref:`technical-details-java-analysis-and-tools-api`.

-  In case you want to learn how to apply Kieker to a Java application,
   you find an tutorial under :ref:`getting-started`.
-  Tutorials and instruction on specific topics can be found here:

   - :ref:`instrumenting-software`
   - :ref:`collecting-data`
   - :ref:`analyzing-monitoring-data`
   - :ref:`contributing-to-kieker`
   - :ref:`technical-details`

-  All tools are documented under :ref:`kieker-tools`
-  More documentation and API and other programming languages can be
   found below
   

Framework Components and Extension Points
-----------------------------------------

.. figure:: images/framework-figure.svg
   :width: 600
   :alt: Kieker Framework Overview
   
   Kieker framework components and extension points for custom components

The Figure above depicts the possible extension points for custom
components as well as the components which are already included in the
**Kieker** distribution and detailed below. 

- **Monitoring writers and corresponding readers** for file systems
  and SQL databases, for in-memory record streams (named pipes), as well
  writers and readers employing Java Management Extensions (JMX) and
  Java Messaging Service (JMS) technology. A special reader allows to
  replay existing persistent monitoring logs, for example to emulate
  incoming monitoring data---also in real-time.
- **Time sources** utilizing Java's ``System.nanoTime()`` (default) or
  ``System.current\-TimeMillis()`` methods.
- **Monitoring record types** allowing to store monitoring data about
  operation executions (including timing, control-flow, and session
  information), CPU and resource utilization, memory/swap usage, as well
  as a record type which can be used to store the current time.
- **Monitoring probes**: A special feature of **Kieker** is the ability
  to monitor (distributed) traces of method executions and corresponding
  timing information. For monitoring this data, Kieker includes
  monitoring probes employing AspectJ, Java EE, Servlet, Spring, and
  Apache CXF technology.
  Additionally, Kieker includes probes for (periodic) system-level
  resource monitoring employing OSHi.
- **Analysis/Visualization plugins** can be assembled to pipe-and-filter
  architectures based on input and output ports. The
  **KiekerTraceAnalysis** tool is itself implemented based on Kieker
  Analysis filters allowing to reconstruct and visualize architectural
  models of the monitored systems, e.g., as dependency graphs,
  sequence diagrams, and call trees.


