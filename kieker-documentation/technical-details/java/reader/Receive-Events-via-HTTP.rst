.. _technical-details-java-reader-receive-events-via-http:

Receive Events via HTTP/JSON (RestServiceCompositeStage) 
========================================================

The ``RestServiceCompositeStage`` allows to receive events via HTTP in
form of a REST request. At its core, it uses the ``RestServiceStage``
and has three parameters.

**Parameter**

Please note that parameters must be prefixed by the canonical name of
the composite stage ``kieker.tools.source.RestServiceCompositeStage``

-  hostname : hostname to listen for when accessed (aka virtual host
   name); can be null
-  port : TCP port to listen to
-  accessRestrictionHandler : class name of a access restriction
   handler. There are currently two access restriction handler available
   implementing (``IAccessHandler``)

   -  ``AllAccessHandler``
   -  ``NetworkAccessHandler`` (experimental)

**Format of the events**

[ "RecordClassName", all parameters ]

**Further reading**

-  Source code
   `RestServiceCompositeStage.java <https://github.com/kieker-monitoring/kieker/blob/master/kieker-tools/src/kieker/tools/source/RestServiceCompositeStage.java>`_

