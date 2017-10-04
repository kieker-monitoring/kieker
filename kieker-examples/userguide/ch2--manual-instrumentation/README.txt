This directory contains a pre-compiled Jar file which can be started with 
the 'java' tool using the following commands:
 
Monitoring:

-UNIX-like systems:
 $ java -cp build/libs/BookstoreApplicationMonitoringAnalysis.jar:lib/kieker-1.13-emf.jar kieker.examples.userguide.ch2bookstore.manual.BookstoreStarter
- Windows:
 $ java -cp build/libs/BookstoreApplicationMonitoringAnalysis.jar;lib\kieker-1.13-emf.jar kieker.examples.userguide.ch2bookstore.manual.BookstoreStarter

Analysis:

-UNIX-like systems:
 $ java -cp build/libs/BookstoreApplicationMonitoringAnalysis.jar:lib/kieker-1.13-emf.jar kieker.examples.userguide.ch2bookstore.manual.BookstoreAnalysisStarter </path/to/monitoring-log/>
- Windows:
 $ java -cp build/libs/BookstoreApplicationMonitoringAnalysis.jar;lib\kieker-1.13-emf.jar kieker.examples.userguide.ch2bookstore.manual.BookstoreAnalysisStarter <path\to\monitoring-log\>

Alternatively, you can compile the sources and start the application using
'gradle' with the given 'build.gradle' file:

Monitoring:
 $ gradle runMonitoring

Analysis:
 $ gradle runAnalysis -Danalysis.directory=</path/to/monitoring-log/>

In order to re-compile the sources into BookstoreApplicationMonitoringAnalysis.jar, run the 
following command:
 $ gradle jar

The example includes the Gradle wrapper scripts `gradlew{.bat}` for UNIX-like and
Windows systems. These scripts automatically download and install the right 
Gradle version. They can be used just like the gradle command, e.g., `./gradlew jar`

Please see Chapter 2 of the Kieker User Guide for details on this example.
