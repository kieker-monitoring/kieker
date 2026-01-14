.. _instrumenting-software-bytebuddy:

Instrumentation with ByteBuddy
============================

ByteBuddy Configuration
---------------------

The easiest way to configure ByteBuddy with Kieker is by weaving the ByteBuddy probes into the SuT.
ByteBuddy is nown to produce the lowest overhead among the contemporary instrumentation frameworks
(see Reichelt, D. G., Bulej, L., Jung, R., & Van Hoorn, A. (2024, May): Overhead comparison of
instrumentation frameworks. In Companion of the 15th ACM/SPEC International Conference on Performance
Engineering (pp. 249-256).)

Weaving using ByteBuddy requires three steps: Getting the latest Kieker ByteBuddy release, configuring
the ByteBuddy weaving, and adding Kieker to the  SuT start.

Downloading Kieker ByteBuddy Agent
~~~~~~~~~~~~~~~~~~~~

To download the agent, execute `wget https://repo1.maven.org/maven2/net/kieker-monitoring/kieker/2.0.2/kieker-2.0.2-bytebuddy.jar`

Configuring the ByteBuddy Weaving
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

By default, ByteBuddy does not offer any syntax to specify which methods are woven. Therefore, Kieker contains a custom syntax in environment variables. This can be done using ``export KIEKER_SIGNATURES_INCLUDE="my.application.*"`` ; to exclude certain packages, use ``export KIEKER_SIGNATURES_EXCLUDE="my.application.subpackage.*"`` . It is also possible to define multiple patterns that are split by semicolons (e.g., ``KIEKER_SIGNATURES_INCLUDE="my.application.*;org.importedlibrary.*"`` ).

Adding Kieker to the SuT Start
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

To add Kieker to the SuT start, the javaagent needs to be specified, for example using:

.. code-block:: bash

    java -javaagent:benchmark/kieker-2.0.2-bytebuddy.jar \
         -jar sut.jar

Please note that Kieker relies on a logging backend that is compatible with log4j; if you don't have any in your project, please download wget https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/2.0.9/slf4j-simple-2.0.9.jar and change the start like follows:

.. code-block:: bash

    java -javaagent:benchmark/kieker-2.0.2-bytebuddy.jar \
         -cp sut.jar:slf4j-simple-2.0.9.jar \
         my.application.Main

References
----------

- `AspectJ probes <http://api.kieker-monitoring.org/2.0.2/>`_



