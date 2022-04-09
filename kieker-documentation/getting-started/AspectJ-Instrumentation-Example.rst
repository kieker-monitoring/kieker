.. _gt-aspectj-instrumentation-example:

AspectJ Instrumentation Example 
===============================

`AspectJ <https://www.eclipse.org/aspectj/>`__ allows to weave code into
the byte code of Java applications and libraries without requiring
manual modifications of the source code. We show below how to use Java
annotation to instrument code and how to do this without any change of
the source code. **Kieker** includes the AspectJ-based monitoring probes

-  ``OperationExecutionAspectAnnotation,``
-  ``OperationExecutionAspectAnnotationServlet,``
-  ``OperationExecutionAspectFull``, and
-  ``OperationEx-ecutionAspectFullServlet``

which can be woven into Java applications at compile time and load time.
These probes monitor method executions and corresponding trace and
timing information. The probes with the postfix\ ``Servlet``\ store a
session identifier within the\ ``OperationExecutionRecord``. When the
probes with name element\ ``Annotation``\ are used, methods to be
monitored must be annotated by the **Kieker**
annotation\ ``@OperationExecutionMonitoringProbe``.

This section demonstrates how to use the AspectJ-based probes to monitor
traces based on the Bookstore application.

The Java sources of the example presented in this section, as well as a
pre-compiled binary, can be found in
the\ ``examples/userguide/ch5-trace-monitoring-aspectj/``\ directory of
the binary release.

The directory structure used in this example is as follows:

-  ``examples``

   -  ``userguide/``

      -  ``ch5–trace-monitoring-aspectj/``

         -  ``build/`` Directory for the Java class files
         -  ``libs/``

            -  ``BookstoreApplication.jar``

         -  ``gradle/``

            -  ``wrapper/`` Directory for the gradle wrapper
            -  ``lib/``\ Directory for the needed libraries

               -  ``task ':aspectJJar' property 'archiveFileName'``

            -  ``src/kieker/examples/userguide/ch5bookstore/`` Directory
               for the source code files

               -  ``Bookstore.java``
               -  ``BookstoreHostnameRewriter.java``
               -  ``BookstoreStarter.java``
               -  ``Catalog.java``
               -  ``CRM.java``

            -  ``src-resources/META-INF/`` Directory for the
               configuration files

               -  ``aop.xml``
               -  ``aop-event.xml``
               -  ``aop-full.xml``
               -  ``kieker.monitoring.adaptiveMonitoring.conf``
               -  ``kieker.monitoring.properties``
               -  ``build.gradle``

         -  ``gradlew``
         -  ``gradlew.bat``
         -  ``README.txt``

The jar-file\ ``task ':aspectJJar' property 'archiveFileName'``\ already includes the
AspectJ weaver, which is registered with the JVM and weaves the
monitoring instrumentation into the Java classes. It will be configured
based on the configuration file\ ``aop.xml``, for which a working sample
file is provided in the example’s\ ``META-INF/``\ directory. Instead of
registering the\ ``task ':aspectJJar' property 'archiveFileName'``\ as an agent to
the JVM, the\ ``aspectjweaver-1.8.2.jar``\ can be used. In this case,
the\ ``task ':mainJar' property 'archiveFileName'``\ needs to be added to the classpath.

Instrumentation by Annotation
-----------------------------

Once the necessary files have been copied to the example directory, the
source code can be instrumented with the
annotation ``OperationExecutionMonitoringProbe``. The below listing
shows how the annotation is used.

.. code-block:: java
   :linenos:

   public class Bookstore {
   
        private final Catalog catalog = new Catalog();
        private final CRM crm = new CRM(this.catalog);
   
        @OperationExecutionMonitoringProbe
        public void searchBook() {
             this.catalog.getBook(false);
             this.crm.getOffers();
        }
   
   }

As a first example, each method of the Bookstore application will be
annotated. The annotation can be used to instrument all methods except
for constructors.The ``aop.xml`` file has to be modified to specify
the classes to be considered for instrumentation by the AspectJ weaver.
The listing below shows the modified configuration file.

.. code-block:: xml
   :linenos:
   
   <!DOCTYPE aspectj PUBLIC "-//AspectJ//DTD//EN" "http://www.aspectj.org/dtd/aspectj_1_5_0.dtd">
   <aspectj>
      <weaver options="">
         <include within="kieker.examples.userguide.ch5bookstore..*"/>
      </weaver>
      <aspects>
         <aspect name="kieker.monitoring.probe.aspectj.operationExecution.OperationExecutionAspectAnnotation"/>
      </aspects>
   </aspectj>

Line 5 tells the AspectJ weaver to consider all classes inside the
example package. AspectJ allows to use wildcards for the definition of
classes to include, e.g.,
``<include within=”bookstoreTracing.Bookstore∗”/>`` to weave all classes
with the prefix ``Bookstore`` located in
apackage ``bookstoreTracing``. Line 9 specifies the aspect to be woven
into the classes. In this case, the Kieker
probe ``OperationExecutionAspectAnnotation`` is used. It requires that
methods intended tobe instrumented are annotated by
``@OperationExecutionMonitoringProbe``, as mentioned before. Below we
show how to compile and run the annotated Bookstore application.
The ``aop.xml`` must be located in a ``META-INF/`` directory in the
classpath – in this case the ``build/`` directory. The AspectJ weaver
has to be loaded as a so-called Java-agent. It weaves the monitoring
aspect into the byte code of the Bookstore application. Additionally,
a ``kieker.monitoring.properties`` is copied to
the ``META-INF/`` directory. This configuration file may be adjusted
as desired.

Unix version:

.. code-block:: shell
   :linenos:
   
   mkdir build/META−INF
   javac src/kieker/examples/userguide/ch5bookstore/∗.java \
      -d build/ -classpath lib/task ':aspectJJar' property 'archiveFileName'

   cp src−resources/META−INF/aop.xml build/META−INF/
   cp src−resources/META−INF/kieker.monitoring.properties build/META−INF/

   java -javaagent:lib/task ':aspectJJar' property 'archiveFileName' \
      -classpath build/ kieker.examples.userguide.ch5bookstore.BookstoreStarter

Windows version:

.. code-block:: shell
   :linenos:
   
   mkdir build\META−INF

   javac src\kieker\examples\userguide\ch5bookstore\∗.java
      -d build -classpathlib\task ':aspectJJar' property 'archiveFileName'

   copy src−resources\META−INF\aop.xml build\META−INF\
   copy src−resources\META−INF\kieker.monitoring.properties build\META−INF\

   java -javaagent:lib\task ':aspectJJar' property 'archiveFileName'
      -classpath build\kieker.examples.userguide.ch5bookstore.BookstoreStarter

After a complete run of the application, the monitoring files should
appear in the same way as described in manual instrumentation including
the additional trace information.

Instrumentation without Changing the Code
-----------------------------------------

Instrumentation without annotations AspectJ-based instrumentation
without using annotations is quite simple. It is only necessary to
modify the file\ ``aop.xml``, as shown in the following listing. In the
example directory a prepared version is provided named ``aop-full.xml``.

.. code-block:: xml
   :linenos:

   <!DOCTYPE aspectj PUBLIC "-//AspectJ//DTD//EN" "http://www.aspectj.org/dtd/aspectj_1_5_0.dtd">
   <aspectj>
      <weaver options="">
         <include within="kieker.examples.userguide.ch5bookstore..*"/>
      </weaver>
      <aspects>
         <aspect name="kieker.monitoring.probe.aspectj.operationExecution.OperationExecutionAspectFull"/>
      </aspects>
   </aspectj>

The alternative aspect ``OperationExecutionAspectFull`` is being
activated in line 9. As indicated by its name, this aspect makes sure
that every method within the included classes/packages will be
instrumented and monitored. Line 5 illustrates how to limit the
instrumented methods to those of the class\ ``BookstoreStarter``.

This configuration file may be adjusted as desired. Please note here the
``aop-full.xml`` is copied to the ``META-INF`` folder and renamed to
``aop.xml``, as this is necessary for AspectJ to find the aspect
configuration.

Unix version:

.. code-block:: shell
   :linenos:
   
   mkdir build/META−INF
   
   javac src/kieker/examples/userguide/ch5bookstore/∗.java \
      -d build/ -classpath lib/task ':aspectJJar' property 'archiveFileName'
   
   cp src−resources/META−INF/aop-full.xml build/META−INF/aop.xml
   cp src−resources/META−INF/kieker.monitoring.properties build/META−INF/
   
   java -javaagent:lib/task ':aspectJJar' property 'archiveFileName' \
      -classpath build/ kieker.examples.userguide.ch5bookstore.BookstoreStarter

Windows version:

.. code-block:: shell
   :linenos:

   mkdir build\META−INF

   javac src\kieker\examples\userguide\ch5bookstore\∗.java
      -d build -classpathlib\task ':aspectJJar' property 'archiveFileName'
   
   copy src−resources\META−INF\aop-full.xml build\META−INF\aop.xml
   copy src−resources\META−INF\kieker.monitoring.properties build\META−INF\

   java -javaagent:lib\task ':aspectJJar' property 'archiveFileName'
      -classpath build\kieker.examples.userguide.ch5bookstore.BookstoreStarter

After a complete run of the application, the monitoring files should
appear in the same way as described in manual instrumentation including
the additional trace information.
