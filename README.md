# Kieker Monitoring Framework

The internal behavior of large-scale software systems cannot be determined on the
basis of static (e.g., source code) analysis alone. Kieker provides complementary
dynamic analysis capabilities, i.e., monitoring and analyzing a software system’s
runtime behavior — enabling application performance monitoring and architecture
discovery.

Detailed information about Kieker is provided at http://kieker-monitoring.net/

## Usage

Kieker releases (stable, nightly, etc.) can be downloaded from our website's [Download Section](http://kieker-monitoring.net/download).

Documentation on how to use Kieker can be found here: [Kieker Documentation](http://kieker-monitoring.net/documentation)

## Development and Contribution

Gradle is used as the build tool. A `build.gradle` file is provided.

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
