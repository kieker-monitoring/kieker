# Kieker Observability Framework

The internal behavior of large-scale software systems cannot be determined on the
basis of static (e.g., source code) analysis alone. Kieker provides complementary
dynamic analysis capabilities, i.e., tracing, monitoring and analyzing a software
system’s runtime behavior — enabling application performance monitoring and
architecture discovery.

Detailed information about Kieker is provided at http://kieker-monitoring.net/
and its documentation site https://kieker-monitoring.readthedocs.io/en/latest/

## Citation

If you use this software, please cite

- Wilhelm Hasselbring and André van Hoorn (2020) "Kieker: A monitoring framework for software engineering research". Software Impacts, 5. https://doi.org/10.1016/j.simpa.2020.100019

- André van Hoorn, Jan Waller, and Wilhelm Hasselbring (2012) "Kieker: A Framework for Application Performance Monitoring and Dynamic Software Analysis". In: 3rd joint ACM/SPEC International Conference on Performance Engineering (ICPE 2012), April 22-25, 2012, Boston, Massachusetts, USA. https://doi.org/10.1145/2188286.2188326

BibTeX entries are provides as [Kieker BibTeX file](Kieker.bib)

## Usage

Kieker releases (stable, nightly, etc.) can be downloaded from our website's [Download Section](http://kieker-monitoring.net/download).

Documentation on how to use Kieker can be found here: [Kieker Documentation](http://kieker-monitoring.net/documentation)

## Development and Contribution

Gradle is used as the build tool. A `build.gradle` file is provided. From the command-line, please use the provided Gradle wrapper script, e.g., on Unix-based systems, run `./gradlew clean build -x check -x test` to build without executing checks and tests.

For issue tracking and bug reports, JIRA is used at <https://kieker-monitoring.atlassian.net/>.

Further instructions for developers are available at
https://kieker-monitoring.atlassian.net/wiki/display/DEV/

### Eclipse Setup for Contributors

- Get Gradle support by installing the Eclipse plugin "Buildship: ..." in version 2 or above.
- If you have already imported Kieker in Eclipse, delete it
- Import Kieker in Eclipse by importing it as gradle project (Eclipse will also import all submodules automatically)
- Whenever you change a build.gradle file, regenerate the .project and .classpath files for Eclipse by using "Gradle->Refresh Gradle Project"

Read our [Confluence pages](https://kieker-monitoring.atlassian.net/wiki/spaces/DEV/pages/5865685/Local+Development+Environment) for more information.

### Code Conventions
Read and follow our [code conventions](https://kieker-monitoring.atlassian.net/wiki/spaces/DEV/pages/24215585/Kieker+Coding+Conventions+in+Eclipse)

### Debugging and Logging Kieker's Execution

Kieker uses the [Simple Logging Facade for Java (SLF4J)](https://www.slf4j.org/) to support the logging framework of your choice. In order to see or store log messages, you need to [bind a logging framework](https://www.slf4j.org/manual.html#swapping) at deployment time.

A fast and flexible logging framework that can be used with SLF4J is [Logback](https://logback.qos.ch). In order to use it, you have to [download](https://logback.qos.ch/download.html) it and add the following Jar files to the classpath:
- `logback-classic-<version>.jar`
- `logback-core-<version>.jar`

Moreover, you have to set up a `logback.xml` file for configuration and add its containing folder to the classpath. An example of such a file is provided below:

````xml
<configuration>
<!-- log to console -->
<appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
  <encoder>
  <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
  </encoder>
</appender>
<!-- for logging to files see: https://logback.qos.ch/manual/appenders.html -->

<!-- standard log level is "warn" -->
<root level="warn">
  <appender-ref ref="STDOUT" />
</root>
<!-- set log level for TCP writer down to "info" -->
<logger name="kieker.monitoring.writer.tcp" level="INFO" />
</configuration>
````

[Logbacks official documentation](https://logback.qos.ch/manual/index.html) provides more information on how to use and configure it.
