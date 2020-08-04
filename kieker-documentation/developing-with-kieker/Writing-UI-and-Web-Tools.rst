.. _developing-with-kieker-writing-ui-and-web-tools:

Writing UI and Web Tools 
========================

Writing tools with a nice user interface either as a desktop or a
web-based application requires to find a neat way to embed Kieker
analysis tools. For that purpose we provide the class
``AbstractEmbeddableService.`` The class provides two main control
methods ``run`` and ``terminate``. We do not provide a progress
mechanism, as this is analysis specific and should be realized through a
progress indicating stage.

Beside the control methods, each service requires three methods to work
properly. In detail they are

-  ``logError`` to log exceptions during setup
-  ``shutdownService`` comprising tasks to perform after a service has
   finished its task
-  ``createTeetimeConfiguration`` used to prepare and instantiate the
   Teetime configuration

| 

