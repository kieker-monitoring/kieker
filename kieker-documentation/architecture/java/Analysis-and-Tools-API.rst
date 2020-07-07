.. _architecture-java-analysis-and-tools-api:

Analysis and Tools API (Java) 
=============================

The Kieker analysis part supports a wide range of readers to receive
events via network and from written log files. It also provides stages
(filters) to perform a wide range of analyses. To use these stages you
can either use one of the tools provided in the tools section or create
your own. Therefore, we support building tools with our standard
architecture for services. Depending on you needs you can use our
readers and receivers in three different ways.

-  Directly use the respective TeeTime-based filters from
   kieker-analysis (see `kieker-analysis source
   stages) <https://github.com/kieker-monitoring/kieker/tree/master/kieker-analysis/src/kieker/analysis/source>`__.
   They can be configured in Java via parameters.
-  Use the composite stages provided by the kieker-tools (see
   `kieker-tools composite
   stages) <https://github.com/kieker-monitoring/kieker/tree/master/kieker-tools/src/kieker/tools/source>`__.
   They take all their configuration via a Kieker configuration object.
-  Use the receiver instantiation factory (see
   `SourceStageFactory) <https://github.com/kieker-monitoring/kieker/blob/master/kieker-tools/src/kieker/tools/source/SourceStageFactory.java>`__.
   It allows to select and configure a receiver stage based on a
   configuration object. In case you use the configuration file feature
   for your tool (see `Writing Tools and
   Services <Writing-Tools-and-Services.rst>`__) then you can
   use the factory to setup the event receiving part in one line. The
   `collector <https://github.com/kieker-monitoring/kieker/tree/master/kieker-tools/collector/src/kieker/tools/collector>`__
   is a good example to implement such tool.

-  `Receive Events from Log
   Files <Receive-Events-from-Log-Files.rst>`__
-  `Receive Events via TCP from Multiple Sources
   (MultipleConnectionTcpSourceCompositeStage) <Receive-Events-via-TCP.rst>`__
-  `Receive Events via AMQP (AMQPReaderStage) <Receive-Events-via-AMQP.rst>`__
-  `Receive Events via JMS (JMSReaderStage) <Receive-Events-via-JMS.rst>`__
-  `Receive Events via HTTP/JSON
   (RestServiceCompositeStage) <Receive-Events-via-HTTP.rst>`__
-  `Writing Tools and
   Services <Writing-Tools-and-Services.rst>`__
-  `Writing UI and Web
   Tools <Writing-UI-and-Web-Tools.rst>`__
