.. _java-monitoring-api:

Kieker Java Monitoring API
==========================

The framework provides a set of APIs for monitoring in Java. The core
component ist the **Monitoring Controller** which utilizes 
**Monitoring Writers** to store or transmit monitoring data collected by
**Monitoring Probes** based on **Monitoring Records**.

.. note::
   Maybe add an overview graphic here.

.. toctree::
  :maxdepth: 1
  
  Monitoring-Controller-API.rst
  Monitoring-Records-API.rst
  Monitoring-Probes-API.rst
  Analysis-and-Tools-API.rst
  writer/Java-Writer-API.rst
  writer/File-Writer.rst
  writer/Single-Socket-TCP-Writer.rst
  reader/Receive-Events-from-Log-Files.rst
  reader/Receive-Events-via-AMQP.rst
  reader/Receive-Events-via-HTTP.rst
  reader/Receive-Events-via-JMS.rst
  reader/Receive-Events-via-TCP.rst

