This directory contains a pre-compiled Jar file which can be started with 
the 'java' tool using the following commands:

-UNIX-like systems:
 $ java -cp build/libs/BookstoreApplicationMonitoringAnalysis.jar:lib/kieker-1.14-SNAPSHOT-emf.jar -Dkieker.monitoring.configuration=src-resources/META-INF/kieker.monitoring.properties kieker.examples.userguide.ch3and4bookstore.Starter 
- Windows:
 $ java -cp build/libs/BookstoreApplicationMonitoringAnalysis.jar;lib\kieker-1.14-SNAPSHOT-emf.jar -Dkieker.monitoring.configuration=src-resources/META-INF/kieker.monitoring.properties kieker.examples.userguide.ch3and4bookstore.Starter

Alternatively, you can compile the sources and start the application using
'gradle' with the given 'build.gradle' file:

Run example:
 $ gradle runMonitoringAndAnalysis

In order to re-compile the sources into BookstoreApplicationMonitoringAnalysis.jar, run the 
following command:
 $ gradle jar

The example includes the Gradle wrapper scripts `gradlew{.bat}` for UNIX-like and
Windows systems. These scripts automatically download and install the right 
Gradle version. They can be used just like the gradle command, e.g., `./gradlew jar`


Please see Chapters 3 and 4 of the Kieker User Guide for details on this example.
