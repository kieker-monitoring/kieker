This directory contains a pre-compiled Jar file which can be started with 
the 'java' tool using the following commands ('5' starts five requests):

-UNIX-like systems:
 $ java -javaagent:lib/kieker-1.12-SNAPSHOT-aspectj.jar -jar build/libs/BookstoreApplication.jar 5
- Windows:
 $ java -javaagent:lib\kieker-1.12-SNAPSHOT-aspectj.jar -jar build/libs/BookstoreApplication.jar 5

Alternatively, you can compile the sources and start the application using
'gradle' with the given 'build.gradle' file:

Run example:
 $ gradle runMonitoringAndAnalysis

In order to re-compile the sources into BookstoreApplication.jar, run the 
following command:
 $ gradle jar

The example includes the Gradle wrapper scripts `gradlew{.bat}` for UNIX-like and
Windows systems. These scripts automatically download and install the right 
Gradle version. They can be used just like the gradle command, e.g., `./gradlew jar`

 
Please see Chapter 5 of the Kieker User Guide for details on this example.
