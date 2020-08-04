.. _tutorials-how-to-apply-kieker-in-java-ee-environments:

How to apply Kieker in Java EE Environments 
===========================================

Depending on the configuration of the JavaEE application to be
monitored, different instrumentation technologies (e.g., Spring AOP and
SOAP/CXF interceptors) can and should be used. In this page, we show how
to use AspectJ to monitor method calls in different JavaEE environments.

Jetty
-----

Copy ``kieker-1.10_aspectj.jar`` into a directory, where it can be accessed
by Jetty, e.g. ``jetty/kieker/``.

Jetty is usually shipped with a configuration file start.ini in which
start parameters are defined. Add the following snippet to this file:

.. code::
	
	--exec
	-javaagent:kieker/kieker-1.10_aspectj.jar
	-Dkieker.monitoring.skipDefaultAOPConfiguration=true
	-Daj.weaving.verbose=true

To use a custom Kieker configuration at the given location, the
following parameter can be added:

``-Dkieker.monitoring.configuration=kieker/kieker.monitoring.properties``

**Important:** Due to a bug in the parser of Jetty, a line ending with
.properties is misinterpreted and leads to an exception. We recommend to
rename the extension of Kieker's configuration file.

To use a custom AspectJ configuration at the given location, the
following parameter can be added:

``-Dorg.aspectj.weaver.loadtime.configuration=file://c:/jetty/kieker/aop.xml``

**Important:** There seems to be problems with relative paths for
AspectJ in JavaEE environments. We recommend to use URIs instead.

*Tested with JPetStore 6 and Jetty 9.2.2.*

JBoss
-----

-  Needs documentation but ` NovaTec's blog
   post <http://blog.novatec-gmbh.de/analysing-kieker-with-jboss-dvdstore-sample-application/>`__
   may serve as a starting point

Tomcat
======

Copy ``kieker-1.10_aspectj.jar`` into a directory, where it can be accessed
by Tomcat, e.g. ``tomcat/kieker/``.

Tomcat is usually shipped with start scripts ``bin/catalina.(sh|.bat)``. Add
one of the following snippets to the correct file, depending on your
operation system:

.. code::
	
	set JAVA_OPTS=%JAVA_OPTS%
		-javaagent:%CATALINA_BASE%\kieker\kieker-1.10_aspectj.jar
		-Dkieker.monitoring.skipDefaultAOPConfiguration=true
		-Daj.weaving.verbose=true
		-Dkieker.monitoring.configuration=%CATALINA_BASE%/kieker/kieker.monitoring.properties
		-Dorg.aspectj.weaver.loadtime.configuration=...
	
	set JAVA_OPTS=${JAVA_OPTS}
		-javaagent:${CATALINA_BASE}\kieker\kieker-1.10_aspectj.jar
		-Dkieker.monitoring.skipDefaultAOPConfiguration=true
		-Daj.weaving.verbose=true
		-Dkieker.monitoring.configuration=${CATALINA_BASE}/kieker/kieker.monitoring.properties
		-Dorg.aspectj.weaver.loadtime.configuration=...

...

-  Needs documentation but
   `ticket/566 <http://kieker.uni-kiel.de/trac/ticket/566#comment:8>`__
   may serve as a starting point

Glassfish
=========

On Glassfish 4, this can be achieved with adding properties to the
domain.xml. The default domain in glassfish is normally called domain0.
Let further assume that glassfish was installed in /opt/glassfish-4.0
then the domain.xml file will be located in
/opt/glassfish-4.0/glassfish/domains/domain0/config. In that file search
for jvm-options. You will find multiple such entries between

.. code::
	
	<java-config ...>
		<jvm-options>...</jvm-options>
	</java-config>

After the last entry in that XML environment, add the following lines
and adapt the paths to your situation.

.. code::
	
	<jvm-options>-javaagent:${com.sun.aas.installRoot}/lib/kieker-1.10-SNAPSHOT_aspectj.jar</jvm-options>
	<jvm-options>-Dkieker.monitoring.skipDefaultAOPConfiguration=true</jvm-options>
	<jvm-options>-Daj.weaving.verbose=true</jvm-options>
	<jvm-options>-Dkieker.monitoring.configuration=${com.sun.aas.installRoot}/kieker/kieker.monitoring.properties</jvm-options>
	<jvm-options>-Dorg.aspectj.weaver.loadtime.configuration=${com.sun.aas.installRoot}/kieker</jvm-options>

WebSphere
=========

-  Needs documentation

JBoss (Wildfly)
===============

An alternative approach to run Kieker within a JBoss environment is
described
`here <https://blog.novatec-gmbh.de/analysing-kieker-with-jboss-dvdstore-sample-application/>`__.

Requires:

-  Kieker-1.13-SNAPSHOT or above (1.12 and below cause an error in JBoss
   environments)
-  Kieker packed as Wildfly module
-  AspectJ Weaver packed as Wildfly module

Kopiere beide Module einfach in das folgende Wildfly-Verzeichnis:

``modules/system/layers/base``

Kopiere die Dateien ``kieker.properties`` und ``aop.xml`` in das
(neue) Verzeichnis "kieker" auf oberster Ebene von Wildfly. Passe nun
den Ausgabepfad von Kieker an und schränke das Instrumentieren auf den
gewünschten Paketnamen ein.

[in der Datei standalone.xml]

-  Unter dem folgenden, vorhandenen Subsystem müssen die beiden, neuen
   Module als globale Module registriert werden:

.. code::
	
	<subsystem xmlns="urn:jboss:domain:ee:4.0">
	<global-modules>
		<module name="kieker"/>
		<module name="org.aspectj"/>
	</global-modules>

[in der Datei standalone.conf]

-  Dort, wo die Systempakete deklariert werden, müssen die folgenden
   ergänzt werden: org.jboss.logmanager, com.manageengine, org.aspectj,
   kieker. Du musst im Folgenden nur darauf achten, dass ich
   Windows-Syntax für die Skriptbefehle genutzt habe.

.. code::
	
	set "JAVA_OPTS=%JAVA_OPTS% -Djboss.modules.system.pkgs=org.jboss.byteman,org.jboss.logmanager,com.manageengine,org.aspectj,kieker"
	
	set "WILDFLY=I:\Software-Engineering\wildfly-10.1.0.Final"

-  Weiterhin muss der Aspectjweaver als Javaagent eingetragen werden und
   für Wildfly entsprechende notwendige Ergänzungen vorgenommen werden,
   die das Verwenden von AspectJ überhaupt erst ermöglichen:

.. code::
	
	set "JAVA_OPTS=%JAVA_OPTS% -javaagent:%WILDFLY%/modules/system/layers/base/org/aspectj/main/aspectjweaver.jar"
	
	set "JAVA_OPTS=%JAVA_OPTS% -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
	
	set "JAVA_OPTS=%JAVA_OPTS% -Xbootclasspath/p:%WILDFLY%/modules/system/layers/base/org/jboss/logmanager/main/jboss-logmanager-2.0.4.Final.jar;%WILDFLY%\modules\system\layers\base\kieker\main\kieker-1.13-SNAPSHOT.jar;%WILDFLY%\modules\system\layers\base\org\aspectj\main\aspectjweaver.jar"

-  Anschließend werden Einstellungen für das Monitoring durch Kieker
   vorgenommen:

.. code::
	
	set "JAVA_OPTS=%JAVA_OPTS% -Dkieker.monitoring.configuration=%WILDFLY%/kieker/kieker.monitoring.1.13.properties"
	
	set "JAVA_OPTS=%JAVA_OPTS% -Dkieker.monitoring.skipDefaultAOPConfiguration=true"
	
	set "JAVA_OPTS=%JAVA_OPTS% -Daj.weaving.verbose=true"
	
	set "JAVA_OPTS=%JAVA_OPTS% -Dorg.aspectj.weaver.loadtime.configuration=file:%WILDFLY%/kieker/aop.xml"

Wenn du nun Wildfly startest, sollten keine Fehler erscheinen. Da das
Instrumentieren erst beim Laden der entsprechenden Klassen erfolgt,
siehst du an dieser Stelle nur Konsolenausgaben von AspectJ. Erst wenn
du das Szenario ausführst, wird Kieker gestartet und der Log-Ordner
angelegt und mit Daten gefüllt.

