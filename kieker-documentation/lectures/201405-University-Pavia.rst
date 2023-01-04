.. _lectures-university-pavia:
 
201405-University-Pavia 
=======================

Information
-----------

The tutorial was held on May 21, 2014 as a 3-hour guest lecture in the
` course "Enterprise Digital
Infrastructure" <http://eecs.unipv.it/degrees/computer-engineering/enterprise-digital-infrastructure/>`_,
` University of Pavia <http://eecs.unipv.it/>`_, Pavia, Italy.

Title: Dynamic Analysis of Software Systems with Kieker: A Hands-On
Lecture

Presenter:

1.  Andre van Hoorn, University of Stuttgart, Germany

For this tutorial, Kieker version 1.9 has been used.

Material provided outside this Wiki page:

-  Kieker and Kieker.WebGUI release archives (binary and doc):
   http://kieker-monitoring.net/download/
-  Kieker user guide:
   ` kieker-1.9_userguide.pdf <http://eprints.uni-kiel.de/16537/49/kieker-1.9_userguide.pdf>`_
-  Presentation slides:

   1. Original slides with overlays:
      ` 20140521-KiekerLectureUPavia-slides.pdf <http://eprints.uni-kiel.de/24622/2/20140521-KiekerLectureUPavia-slides.pdf>`_
   2. Handout versions:
      ` 20140521-KiekerLectureUPavia-handout.pdf <http://eprints.uni-kiel.de/24622/1/20140521-KiekerLectureUPavia-handout.pdf>`_

Schedule
--------

-  09:30 Introduction and Overview of Approach
-  09:50 *Interactive: Quick Start*
-  10:10 Introduction and Overview of Approach (cont'd)
-  10:15 Use Cases in Research and Practice
-  10:35 Kieker's Monitoring Component
-  10:50 *Coffee Break*
-  11:10 Kieker's Monitoring Component (cont'd)
-  11:20 Kieker's Analysis Component & WebGUI
-  11:40 *Interactive: Java EE Monitoring*
-  12:00 A Detailed Look at Selected Use Cases
-  12:30 End

Instructions for the Interactive Parts
--------------------------------------

Preparation
~~~~~~~~~~~

1. Unzip source and binary release archives

   201405-KiekerLectureUPavia $ l kieker-1.9-release/

   kieker-1.9-bin/ kieker-1.9_binaries.zip kieker-1.9_sources.zip
   kieker-1.9-src/ kieker-1.9_userguide.pdf kieker-webgui-1.9_binaries/
   kieker-webgui-1.9_binaries.zip

2. Start Eclipse with new/empty workspace
   201405-KiekerLectureUPavia/eclipse-workspace/

Quick Start
~~~~~~~~~~~

-  Presentation of Release Archive (Binary, Source, User Guide)
-  Import the following examples (to be found in examples/userguide/ of
   the binary release) into Eclipse:

   -  ch2--bookstore-application/
   -  ch3-4--custom-components/
   -  ch5--trace-monitoring-aspectj/

-  Bookstore example with AspectJ from the user guide

   1. Bookstore without Instrumentation (ch2--bookstore-application)

      1. Briefly present Bookstore application
      2. Start without instrumentation: BookstoreStarter.java -> run as
         -> Java application

   2. Instrumentation with AspectJ (Full instrumentation without
      annotations)

      1. (Partially workaround due to
         `#1292 <http://kieker.uni-kiel.de/trac/ticket/1292>`_)
      2. Copy lib/ folder (including kieker-1.9_aspectj.jar) from
         ch5--trace-monitoring-aspectj/ to ch2--bookstore-application
      3. Create directory src/META-INF
      4. Copy aop-full.xml from ch5--trace-monitoring-aspectj/ to
         ch2--bookstore-application/src/META-INF/aop.xml
      5. run with -javaagent:lib/kieker-1.9_aspectj.jar

   3. Filesystem-Log

      1. Show console output
      2. Show .map and .dat file in Konqueror
      3. Run trace-analysis{-gui}.(sh|bat)
      4. Convert .dot files to .pdf (e.g., with
         convertLoggingTimestamp.{sh|bat})
      5. Show output

         1. HTML system model
         2. Dependency graphs (Deployment-Operation + ggf. mehr)
         3. Call tree variants
         4. Sequence diagrams -> "if possible"

Monitoring
~~~~~~~~~~

1. Show example records, e.g.,

   -  CPUUtilizationRecord
   -  MyResponseTimeRecord (from ch3-4--custom-components)

2. Show example probes, e.g.,

   -  CPUsDetailedPercSampler
   -  Manual instrumentation in Bookstore (from
      ch3-4--custom-components)

3. Show example writer, e.g., MyPipeWriter (from
   ch3-4--custom-components)

Analysis
~~~~~~~~

1. Show example reader, e.g., MyPipeReader (from
   ch3-4--custom-components)
2. Show example filters, e.g.,

   -  MyPipeWriter (from ch3-4--custom-components)
   -  MyResponseTimeFilter (from ch3-4--custom-components)

3. Show how to assemble and start pipes-and-filters configuration, e.g.,
   in Starter (from ch3-4--custom-components) -> motivation for WebGUI

WebGUI
~~~~~~

1. Mem/swap example without CPU (to be prepared)
2. Open analysis editor
3. Explain example
4. Add CPU filter
5. Start analysis
6. Cockpit

JavaEE
~~~~~~

JPetStore and Live Demo (via JMX)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

1. Change dir to prepared Jetty
   201405-KiekerLectureUPavia/kieker-1.9-release/kieker-1.9-bin/examples/JavaEEServletContainerExample/jetty-hightide-jpetstore
2. Start instrumented JPetStore

   jetty-hightide-jpetstore <master>\* $ java -jar start.jar

3. Access JPetStore http://localhost:8080/jpetstore/ and click around
4. Access live demo http://localhost:8080/demo/

Log to Filesystem and use TraceAnalysis
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

1. Activate file system writer and adaptive monitoring by editing
   jetty-hightide-jpetstore/webapps/jpetstore/WEB-INF/classes/META-INF/kieker.monitoring.properties

   ...

   ## Enable or disable adaptive monitoring.

   kieker.monitoring.adaptiveMonitoring.enabled=true

   #By comment out the next line, the FSWriter is used. This makes the
   demo inoperable.

   #kieker.monitoring.writer=kieker.monitoring.writer.jmx.JMXWriter

2. Restart Jetty
3. Click around a bit
4. Show file system monitoring log
5. Attach to Monitoring Controller via JConsole:

   /usr/lib/jvm/sun-jdk1.6.0_38/bin/jconsole &

   1. toString
   2. Demonstrate adaptive Monitoring

      1. Disable CPU (%CPU) and memory (%MEM_SWAP)
      2. Disable public void
         kieker.monitoring.probe.servlet.SessionAndTraceRegistrationFilter.doFilter(javax.servlet.ServletRequest,
         javax.servlet.ServletResponse, javax.servlet.FilterChain)
      3. Activate \*

   3. enable/disable

6. Create and show plots

   jetty-hightide-jpetstore <master>\* $ mkdir plots

   jetty-hightide-jpetstore <master>\* $ ../../../bin/trace-analysis.sh
   -i /tmp/kieker-<path-to-log>/ -o plots/
   --plot-Deployment-Component-Dependency-Graph --plot-Assembl

   y-Component-Dependency-Graph
   --plot-Deployment-Operation-Dependency-Graph responseTimes
   --plot-Assembly-Operation-Dependency-Graph responseTimes
   --print-System-Model

   jetty-hightide-jpetstore <master>\* $
   ../../../bin/dotPic-fileConverter.sh plots/ pdf

   jetty-hightide-jpetstore <master>\* $ acroread plots/*.pdf&
