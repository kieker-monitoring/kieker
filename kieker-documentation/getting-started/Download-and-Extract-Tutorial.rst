.. _gt-download-and-extract-tutorial:

Download and Extract Tutorial 
=============================

The **Kieker** download site http://kieker-monitoring.net/download/
provides archives of the binary and source distribution, special
bundles, the Javadoc~API, as well as additional examples. For this quick
start guide, **Kieker's** binary distribution, e.g.,
``kieker-2.0.0-SNAPSHOT-binaries.zip``, is required and must be downloaded. After
having extracted the archive, you'll find the directory structure and
contents shown below

-  ``kieker-2.0.0-SNAPSHOT-binaries/``

   -  ``bin`` Call scripts for old style **Kieker** tools
   -  ``build/libs`` **Kieker** framework libraries
   -  ``doc`` Old style documentation
   -  ``examples`` Example projects and configuration files

      -  ``userguide`` Source code of the examples in this document

   -  ``tools`` Packaged **Kieker** tools
   -  ``HISTORY``
   -  ``LICENSE``
   -  ``README``

The Java sources presented in this user guide, as well as pre-compiled
binaries, are included in the ``examples/userguide/`` directory. The
file ``kieker-1.15.1.jar`` contains the *Kieker.Monitoring* and
*Kieker.Analysis* components, as well as the *Kieker.Trace-Analysis*
tool. The sample *Kieker.Monitoring configuration*
file ``kieker.monitoring.ex-ample.properties`` will be detailed in
Chapter 3. In addition to the ``kieker-1.15.1.jar`` file,
the ``build/libs/`` directory includes variants of
this ``.jar`` files with integrated third-party libraries. Additional
information on these ``.jar`` files and when to use them will follow
later in this document.
