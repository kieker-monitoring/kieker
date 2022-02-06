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

Create tooling
--------------

.. toctree::
   :maxdepth: 1
   
   Writing-Tools-and-Services.rst
   Writing-UI-and-Web-Tools.rst


Extend Kieker
-------------

.. toctree::
   :maxdepth: 1
   
   Event-Types-and-Records.rst
   How-to-Write-Probes.rst
   How-to-Write-Tests-for-Your-own-Kieker-Probes.rst
   How-to-Write-Stages.rst
   General-Language-and-Platform-Support.rst
 
Related information:
  * :ref:`technical-details-file-and-serialization-formats`


