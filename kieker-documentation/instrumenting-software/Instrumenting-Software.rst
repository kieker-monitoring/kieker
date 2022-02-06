.. _instrumenting-software:

Instrumenting Software 
======================

**Kieker** allows to instrument various types of applications and
services utilizing different techniques to instrument and implement
probes. Yet the monitoring data produced can be analyzed by all
**Kieker** tools.

Kieker supports a ever growing variety of programming languages and
technologies to measure runtime information of your software systems. In
general **Kieker** uses probes to collect information which are then
send to a logging facility. To introduce the probes into your software
system, **Kieker** uses different techniques including aspect-oriented
programming. They allow the introduction of probes without changing the
source code. For rare cases, where no such technique is applicable,
**Kieker** can be introduced manually.

.. toctree::
   :maxdepth: 1
   
   java/Java.rst
   c/C.rst
   perl/Perl.rst
   python/Python.rst
   com/COM.rst
   vb/VB.rst
   net/NET.rst

Related Topics
--------------

.. toctree::
   :maxdepth: 1
   
   Monitoring-Resource-Consumption.rst
   Adaptive-Monitoring.rst
   Application-Traces-in-Java.rst

- Creating Probes
- Creating new Event Types

 
