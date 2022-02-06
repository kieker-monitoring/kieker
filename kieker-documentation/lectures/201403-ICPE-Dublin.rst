.. _lectures-icpe-dublin:

201403-ICPE-Dublin 
==================

Information
-----------

The 2-hour tutorial was held on March 23, 2014 at the ` 5th ACM/SPEC
International Conference on Performance
Engineering <http://icpe2014.ipd.kit.edu/>`_ in Dublin, Irland.

Presenters:

1.  Andre van Hoorn, University of Stuttgart, Germany
2.  Nils Ehmke, Kiel University, Germany

For this tutorial, Kieker version 1.8 has been used.

Material provided outside this Wiki page:

-  Kieker and Kieker.WebGUI release archives (binary and doc):
   http://kieker-monitoring.net/download/
-  Kieker user guide:
   `kieker-1.8_userguide.pdf <http://eprints.uni-kiel.de/16537/37/kieker-1.8_userguide.pdf>`_
-  Presentation slides:

   1. Original slides with overlays:
      `20140323-ICPE-Tutorial-slides.pdf <http://eprints.uni-kiel.de/23928/2/20140323-ICPE-Tutorial-slides.pdf>`_
   2. Handout versions:
      `20140323-ICPE-Tutorial-handout.pdf <http://eprints.uni-kiel.de/23928/1/20140323-ICPE-Tutorial-handout.pdf>`_

Schedule
--------

-  14:30 Introduction and Overview of Approach (avh)
-  14:45 *Interactive: Quick Start* (nie)
-  15:00 Intro (cont'd) (avh)
-  15:05 Use Cases in Research and Practice (avh)
-  15:20 Kieker's Monitoring Component (avh)
-  15:35 Kieker's Analysis Component & WebGUI (nie)
-  15:50 *Interactive: Java EE Monitoring* (avh)
-  16:05 A Detailed Look at Selected Use Cases (nie/avh)
-  16:30 End

Instructions for the Interactive Parts
--------------------------------------

It makes sense to put the following parts to separate pages.

Preparation
~~~~~~~~~~~

1. Unzip source and binary release archives

   20140323-ICPE-Tutorial/kieker-1.8-release <master>\* $ unzip
   kieker-1.8_binaries.zip && mv kieker-1.8 kieker-1.8-bin

   20140323-ICPE-Tutorial/kieker-1.8-release <master>\* $ unzip
   kieker-1.8_sources.zip && mv kieker-1.8 kieker-1.8-src

1. Start Eclipse with new/empty workspace
   20140323-ICPE-Tutorial/workspace-avh

Quick Start (nie)
~~~~~~~~~~~~~~~~~

-  Preparation:

   -  Create shortlink to directory 20140323-ICPE-Tutorial in Konqueror
   -  Create new workspace in eclipse, located in
      20140323-ICPE-Tutorial/eclipse-workspace

-  Download -> s. Folie

   -  Files to be found in dir:
      20140323-ICPE-Tutorial/download-kieker-1.8/
   -  User Guide vorstellen
   -  Extract kieker-1.8_binaries.zip to 20140323-ICPE-Tutorial/
   -  ... and rename to kieker-1.8-binaries

-  Presentation of Release Archive (Binary, Source, User Guide)
-  Bookstore-Beispiel mit AspectJ aus dem User Guide

   1. Bookstore ohne Instrumentierung

      1. Import example project ch2--bookstore-application into
         workspace
      2. Bookstore zunächst kurz vorstellen
      3. Starten ohne Instrumentierung: BookstoreStarter.java -> run as
         -> Java application

   2. Instrumentierung mit AspectJ (Vollinstrumentierung ohne
      Annotationen)

      1. Create directory lib/
      2. Copy kieker-1.8_aspectj.jar
      3. Create directory src/META-INF
      4. Copy aop.example.xml to src/META-INF/aop.xml
      5. Select full aspect and include "*"
      6. run with -javaagent:lib/kieker-1.8_aspectj.jar

   3. Filesystem-Log

      1. Show console output
      2. Show .map and .dat file in Konqueror

   4. TraceAnalyse mit Visualisierung

      1. Add kieker-1.8_emf.jar to build path
      2. Add commons-cli-1.2.jar to build path
      3. Run \*TraceAnalysisTool\* (run kieker-1.8_emf.jar as "Java
         Application")
      4. Run configuration with program arguments:

         -  -i
            /tmp/kieker-20121121-150448579-UTC-pc-vanhoorn-KIEKER-SINGLETON
         -  --plot-Deployment-Operation-Dependency-Graph
         -  -o .

      5. Show output

         1. HTML-Systemmodell
         2. Abhaengigkeitsgraph (Deployment-Operation + ggf. mehr)
         3. Call-Tree-Varianten
         4. Sequenzdiagramm -> "if possible"

Monitoring (avh)
~~~~~~~~~~~~~~~~

1. Import Kieker source project
   (20140323-ICPE-Tutorial/kieker-1.8-release/kieker-1.8-src/) into the
   workspace ("Import..."->"Import existing project into workspace")
2. Show example record CPUUtilizationRecord
3. Show example probe CPUsDetailedPercSampler

Analysis (nie)
~~~~~~~~~~~~~~

1. ...

WebGUI (nie)
~~~~~~~~~~~~

1. Mem/swap example without CPU (to be prepared)
2. Open analysis editor
3. Explain example
4. Add CPU filter
5. Start analysis
6. Cockpit

JavaEE (avh)
~~~~~~~~~~~~

Quick start
^^^^^^^^^^^

1. Change dir to prepared Jetty
   20140323-ICPE-Tutorial/kieker-1.8-release/kieker-1.8-bin/examples/JavaEEServletContainerExample/jetty-hightide-jpetstore
2. Start instrumented JPetStore

   jetty-hightide-jpetstore <master>\* $ java -jar start.jar

3. Explain console output and tail -f on monitoring log
4. Access JPetStore http://localhost:8080/jpetstore/ and click around
5. Create and show plots
   ::
      
      jetty-hightide-jpetstore <master>\* $ mkdir plots
      
      jetty-hightide-jpetstore <master>\* $ ../../../bin/trace-analysis.sh
      -i
      /tmp/kieker-20140319-150803890-UTC-avh-ThinkPad-RSS-KIEKER-EXAMPLE-JAVAEE/
      -o plots/ --plot-Deployment-Component-Dependency-Graph --plot-Assembl
      
      y-Component-Dependency-Graph
      --plot-Deployment-Operation-Dependency-Graph responseTimes
      --plot-Assembly-Operation-Dependency-Graph responseTimes
      --print-System-Model
      
      jetty-hightide-jpetstore <master>\* $
      ../../../bin/dotPic-fileConverter.sh plots/ pdf
      
      jetty-hightide-jpetstore <master>\* $ acroread plots/*.pdf&

 

Advanced
^^^^^^^^

1. Explain instrumentation (Spring, Servlet)
2. Use a custom Kieker configuration:

   1. Copy META-INF from binary release to JavaEE example

      jetty-hightide-jpetstore <master>\* $ cp -R ../../../META-INF/ .

   2. Edit kieker.monitoring.properties:

      1. hostname=ICPE14-SRV
      2. jmx=true
      3. adaptiveMonitoring.enabled=true
      4. kieker.monitoring.writer.filesystem.AsyncFsWriter.customStoragePath=kieker-logs

   3. Create output dir

      jetty-hightide-jpetstore <master>\* $ mkdir kieker-logs

   4. Activate configuration in start.ini

      -  --exec
      -  -Dkieker.monitoring.configuration=META-INF/kieker.monitoring.properties

3. Sigar Sampler for CPU and MEM

   1. Copy Sigar Jar and {dll|so|...} to webapps/WEB-INF/lib/

      jetty-hightide-jpetstore <master> $ cp
      ../../../lib/sigar-1.6.4.jar ../../../lib/sigar-native-libs/\*
      webapps/jpetstore/WEB-INF/lib/

   2. Activate
      kieker.monitoring.probe.servlet.CPUMemUsageServletContextListener
      in webapps/jpetstore/WEB-INF/web.xml

1. Restart Jetty
2. Click around a bit
3. Show log

1. Attach to Monitoring Controller via JConsole:

   /usr/lib/jvm/sun-jdk1.6.0_38/bin/jconsole &

   1. toString
   2. Demonstrate adaptive Monitoring

      1. Disable public void
         kieker.monitoring.probe.servlet.SessionAndTraceRegistrationFilter.doFilter(javax.servlet.ServletRequest,
         javax.servlet.ServletResponse, javax.servlet.FilterChain)

   3. enable/disable/terminate
   4. Restart Jetty
   5. Click around

Bonus:
^^^^^^

1. Sigar analysis from User Guide

   1. Fix project
   2. Add kieker.jar and sigar.jar to build path
   3. run

2. AspectJ-based instrumentation
