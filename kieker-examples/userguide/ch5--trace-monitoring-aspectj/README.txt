This directory contains a pre-compiled Jar file which can be started with 
the 'java' tool using the following commands ('5' starts five requests):

-UNIX-like systems:
 $ java -javaagent:lib/kieker-1.11-aspectj.jar -jar BookstoreApplication.jar 5
- Windows:
 $ java -javaagent:lib\kieker-1.11-aspectj.jar -jar BookstoreApplication.jar 5

Alternatively, you can compile the sources and start the application using
'gradle' with the given 'build.gradle' file:

Run example:
 $ gradle runMonitoringAndAnalysis

In order to re-compile the sources into BookstoreApplication.jar, run the 
following command:
 $ gradle jar
 
Please see Chapter 5 of the Kieker User Guide for details on this example.
