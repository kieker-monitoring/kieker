# General Setup of Jetty and the included JPetStore

This file contains inforation on Jetty and the preinstalled and 
configurated JPetStore ready for monitoring with Kieker.


## Jetty

The Jetty project is a 100% Java HTTP Server, HTTP Client
and Servlet Container.

The Jetty @ eclipse project is based on the Jetty project at codehaus

  `http://jetty.codehaus.org`

Ongoing development is now at the eclipse foundation

  `http://www.eclipse.org/jetty/`

Jetty @ eclipse is open source and is dual licensed using the apache 2.0
and eclipse public license 1.0.   You may choose either license when
distributing jetty.


## Directory Structure

- `about.html` Jetty about page
- `bin/` Start scripts for Jetty which can be used alternatively to directly running the `start.jar`
- `contexts/` 
- `contexts-available/` 
- `etc/` configuration directory for Jetty
- `lib/` library directory
- `LICENSE-APACHE-2.0.txt` apache license
- `LICENSE-ECLIPSE-1.0.html` eclipse license
- `logs/` log directory
- `notice.html` Jetty legal notice
- `overlays/` 
- `README.md` the readme you are currently reading
- `resources/` global configuration setup for webapps
- `start.ini` configuration for Jetty
- `start.jar` main jar for Jetty
- `start.original.ini` original Jetty `start.ini`
- `VERSION.txt` 
- `webapps/` directory containing all web apps including the JPetStore


## Configuring the Example

The default setup of the Java EE Servlet Container Example logs data via
JMX to an host called `JMXServer`. The configuration is located in 
`jetty/webapps/jpetstore/WEB-INF/classes/META-INF/kieker.monitoring.properties`

In case you want to use a JMX server for logging, setup a JMX service
and replace `JMXServer` in the configuration with the correct host name.
In addition check whether the port number is correct.

In case you want to use another Kieker writer to store the logging
information in a file or send the data via TCP you can use the proper
Kieker writer configurations.

You may find further information on the available writers in the Wiki
and a documented property file in
`https://github.com/kieker-monitoring/kieker/blob/stable/kieker-monitoring/src-resources/META-INF/kieker.monitoring.default.properties`
and in the Kieker bundle.


## Kieker Probe Injection

You may find in `webapps/jpetstore/WEB-INF/` a `web.xml` and an 
`applicationContext.xml` which contain configuration parameters for the
monitoring.

- `web.xml` includes the proper Kieker configuration examples for
  Servlet filters and listeners marked by comments. We also provide the
  original `web.original.xml` to help understand the additions.

- The `applicationContext.xml` has been altered to support Spring AOP.
  To support the understand of the alteration to original file is also
  provided (`applicationContext.original.xml`).

- The `start.ini` in the Jetty root directory contains additional
  options to activate AspectJ-based monitoring within Jetty and the
  JPetStore. However, the addition is commented out by default and must
  be enabled on purpos. As before, we provide also the original ini-file
  with `start.original.ini`.


## Running the Example

Please note: There have been reports that Servlet container do not work
properly in Java 8 (`JDK_1.8.0_92`). We had this issue with `1.8.0_111-b14`.

`http://stackoverflow.com/questions/36963248/the-type-java-io-objectinputstream-cannot-be-resolved-it-is-indirectly-referenc`

The issue does not arise with Java 7. Therefore, we recommend using
Java 7 for this example. 

Testy your Java version with:

  `java -version`

Executing the pre-configured Jetty with the deployed JPeTStore, type in
the directory where this `README.md` is located one of the following
commands. In case your default Java is not Java 7, you must replace the
word Java with the proper fully qualified path, e.g., 
`/usr/lib/jvm/java-7-oracle/bin/java` 

In case you are running other web services on the same machine, you need
to specify an alternate port for Jetty (see below).

To run with the default options:

  `java -jar start.jar`

To run with properties: 

  `java -jar start.jar jetty.port=8081`

To run with extra configuration file(s) appended, eg SSL:

  `java -jar start.jar etc/jetty-ssl.xml`

To run with extra configuration file(s) prepended, e.g., logging & jmx:

  `java -jar start.jar --pre=etc/jetty-logging.xml --pre=etc/jetty-jmx.xml` 

To run without the args from `start.ini`:

  `java -jar start.jar --ini OPTIONS=Server,websocket etc/jetty.xml etc/jetty-deploy.xml etc/jetty-ssl.xml`

To list the know OPTIONS:

  `java -jar start.jar --list-options`

To see the available options and the default arguments
provided by the start.ini file:

  `java -jar start.jar --help`

