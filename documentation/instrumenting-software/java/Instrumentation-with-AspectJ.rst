.. _instrumenting-software-aspectj:

Instrumentation with AspectJ
============================

The :ref:`tutorial-servlet-example` contains a some basic introduction to
using AspectJ probes.

AspectJ Configuration
---------------------

The easiest way to configure AspectJ with Kieker is by weaving the AspectJ probes into the SuT.
This requires three steps: Getting the latest Kieker AspectJ release, configuring the AspectJ
weaving, and adding Kieker to the  SuT start.

Downloading Kieker
~~~~~~~~~~~~~~~~~~

To download the agent, execute `wget https://repo1.maven.org/maven2/net/kieker-monitoring/kieker/2.0.2/kieker-2.0.2-aspectj.jar`

Configuring the AspectJ weaving
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

The AspectJ weaving is by default configured using an ``aop.xml`` file. AspectJ has a huge syntax for weaving; it is possible to specify classes and packages for inclusion and for exclusion. It is not possible to specify weaving at method level. Assuming that you want to trace the package ``my.application`` , but exclude ``my.application.subpackage``, the following ``aop.xml`` can be used:

.. code-block:: XML

  <!DOCTYPE aspectj PUBLIC "-//AspectJ//DTD//EN" "http://www.aspectj.org/dtd/aspectj_1_5_0.dtd">

  <aspectj>
      <weaver options="">
          <include within="my.application..*"/>
          <exclude within="my.application.subpackage.*"/>
      </weaver>

      <aspects>
        <aspect name="kieker.monitoring.probe.aspectj.operationExecution.OperationExecutionAspectAnnotation"/>
      </aspects>
  </aspectj>

The ``OperationExecutionAspectAnnotation`` specifies that ``OperationExecutionRecord`` should be created. A full list of all available probes can be found in https://github.com/kieker-monitoring/kieker/tree/main/monitoring/aspectj/src/kieker/monitoring/probe/aspectj

Details regarding the AspectJ instrumentation can be found in https://eclipse.dev/aspectj/doc/released/devguide/ltw-configuration.html.

Adding Kieker to the SuT Start
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

To add Kieker to the SuT start, the ``aop.xml`` location needs to be specified and the javaagent needs to be specified, for example using:

.. code-block:: bash

  java -Dorg.aspectj.weaver.loadtime.configuration=file:custom-aop.xml \
       -javaagent:benchmark/kieker-2.0.2-aspectj.jar \
       -jar sut.jar

Please note that Kieker relies on a logging backend that is compatible with log4j; if you don't have any in your project, please download wget https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/2.0.9/slf4j-simple-2.0.9.jar and change the start like follows:

.. code-block:: bash

  java -Dorg.aspectj.weaver.loadtime.configuration=file:custom-aop.xml \
       -javaagent:benchmark/kieker-2.0.2-aspectj.jar \
       -cp sut.jar:slf4j-simple-2.0.9.jar
       my.application.Main

References
----------

- `AspectJ probes <http://api.kieker-monitoring.org/2.0.2/>`_



