.. _developing-with-kieker:

Developing with Kieker
======================

Kieker provides a set of tools to monitor software and analyze its behavior and
its interactions with others. However, sometimes it might be helpful to compose
your own analysis or a Kieker probe or a Kieker stage (filter) may not provide
the necessary features you have in mind. In that case, you can extend Kieker.


In this section we discuss how to develop your own analyses with
Kieker and embed them in tools and services, as well as, extend Kieker where
necessary. We will reference architecture documentation and JavaDoc when needed.
As this is a living software project, there might be a discrepancy between
API documentation, architecture description and the documentation in this
section. In that case, always refer to the API, as it is generated from the
source code.

* Create tooling

  * :ref:`developing-with-kieker-java-writing-tools-and-services`
  * :ref:`developing-with-kieker-java-writing-ui-and-web-tools`
  * :ref:`developing-with-kieker-java-process-configuration-file-options`

* How to write new

  * :ref:`developing-with-kieker-event-types-and-records`
  * Probes
  * `Stages (external) <https://teetime-framework.github.io/wiki/home.html>`_
  * Features of Stages
  * Serializaion Formats
  
* How to support new programming languages
  
  * :ref:`developing-with-kieker-general-language-and-platform-support`
  * :ref:`architecture-file-and-serialization-formats`
  
* Kieker architecture

  * Java

    * :ref:`developing-with-kieker-java-architecture-monitoring-controller-api`
    * :ref:`developing-with-kieker-java-architecture-log-file-reader`
    * :ref:`developing-with-kieker-java-kieker-graph-api`

  * Python
  * C, C++, Fortran and similar languages
