.. _instrumenting-software-kieker4net:

Kieker4NET 
==========

Kieker4NET is a Kieker adapter supporting monitoring of programming
languages based on Microsoft's .NET
platform\ ` <https://en.wikipedia.org/wiki/Component_Object_Model>`__.
The adapter has been developed as a part of the
`DynaMod <http://kosse-sh.de/dynamod>`__ research project. It has been
tested particularly with C#.

Installation of Kieker4NET (work in progress)
=============================================

Requirements
------------

1. ` JNBridgePro <http://www.jnbridge.com/>`__ license
2. PostSharp license

JNBridge Download and Licensing
-------------------------------

JNBridePro can be downloaded from
` http://www.jnbridge.com/bin/downloads.php?pr=1&id=0 <http://www.jnbridge.com/bin/downloads.php?pr=1&id=0>`__.
You'll receive an evaluation version with a trial license. which will
remain functional for 30 days. After having submitted a registration
form, JNBridgePro is available for 32-bit and 64-bit (untested with
Kieker4NET) versions.

The following JNBridePro license types are available:

1. *Developer license:* Required for developing Kieker4NET.
2. *Deployment license:* Required for distributing/installing
   Kieker4NET. Note, that deployment license are only available if a
   developer license has been purchased before. Please note that non-OEM
   (default) and OEM licenses are available.

For details on the JNBridgePro licensing, see
` http://www.jnbridge.com/store.htm <http://www.jnbridge.com/store.htm>`__.

Installing the JNBridge License
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Having registered via the ` JNBridge download
page <http://www.jnbridge.com/bin/downloads.php?pr=1&id=0>`__, you
should receive an e-mail with more information on the product, including
the *activation key*.

Install JNBridgePro by running the setup wizard provided by the
downloaded JNBSetup6_0_x86.msi file. During the setup wizard, you'll
have to select one of the following configurations:

1. *Development configuration*: deployment configuration + proxy
   generation tool and demos.
2. *Deployment configuration*, including only the Java and .NET runtime
   components.

The installer installs two JNBridgePro versions, one for .NET
2.0/3.0/3.5 and a second for .NET 4.0. The development configuration is
required on the machine creating the .NET proxy for the
kieker-<version>.jar. It also include the JNBridePro plugins for Visual
Studio 2005, 2008, and 2010.

Download PostSharp license
--------------------------

-  ...
