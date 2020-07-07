.. _instrumenting-software-servlet:

Servlet Instrumentation 
=======================

Servlets can be instrumented utilizing ``javax.servlet.ServletContextListerner``,
``javax.servlet.http.HttpSessionListener`` and  ``javax.servlet.Filter``. 

The first is triggered when a Servlet context is created (instantiation
of the Servlet) and destroyed. The second is triggered every time a new
session is created. And the last is invoked every time a request is sent
to the Servlet. In the following we will address all three types.

Please note that Servlets can also use other listeners which could in
principle also used to trigger monitoring. However, such probes do not
exist within **Kieker**, but can be build easily with **Kieker**
framework functionality..

Servlet Context Listener
------------------------

HTTP Session Listener
---------------------

Servlet Filter
--------------

The Java Servlet API includes the ``javax.servlet.Filter`` and interface.
It can be used to implement interceptors for incoming HTTP requests.
**Kieker** uses this interface to implement different probes. To add
such interceptor to a Servlet, you have to edit the ``web.xml`` file in
your Servlet project. For example:

.. code-block:: xml
	
	<filter>
		<filter−name>sessionAndTraceRegistrationFilter</filter−name>
		<filter−class>kieker.monitoring.probe.servlet.SessionAndTraceRegistrationFilter</filter−class>
		<init−param>
			<param−name>logFilterExecution</param−name>
			<param−value>true</param−value>
		</init−param>
	</filter>
	<filter−mapping>
		<filter−name>sessionAndTraceRegistrationFilter</filter−name>
		<url−pattern>/∗</url−pattern>
	</filter−mapping>

This configuration adds the
``kieker.monitoring.probe.servlet.SessionAndTraceRegistrationFilter``
interceptor to the Servlet configuration and identifies it with
``sessionAndTraceRegistrationFilter``. It sets one parameter
``logFilterExecution`` to ``true``. In the filter mapping, the
``sessionAndTraceRegistrationFilter`` is mapped to all URLs, i.e., to
all Servlet in the project.

Related Information
-------------------

Kieker comes with many different Servlet filters.

.. todo::
   Add list to filters and listeners here
