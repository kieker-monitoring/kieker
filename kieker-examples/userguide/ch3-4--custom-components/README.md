# Custom Components

This directory contains a pre-compiled Jar file which can be started with 
the 'java' tool using the following commands:

- UNIX-like systems:
  - unzip build/distributions/ch3-4--custom-components.zip
  - ch3-4--custom-components/bin/ch3-4--custom-components
- Windows:
  - Unpack build/distributions/ch3-4--custom-components.zip
  - Run ch3-4--custom-components/bin/ch3-4--custom-components.bat

Alternatively, you can compile the sources and start the application using
'gradle' with the given 'build.gradle' file:

Run example:
 $ ./gradlew runMonitoringAndAnalysis

In order to re-compile the sources into BookstoreApplicationMonitoringAnalysis.jar, run the 
following command:
 $ ./gradlew build
