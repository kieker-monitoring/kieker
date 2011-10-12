This directory contains a pre-compiled Jar file which can be started with 
the 'java' tool using the following commands:
 
Monitoring:

-UNIX-like systems:
 $ java -cp BookstoreMonitoring.jar:lib/commons-logging-1.1.1.jar:lib/kieker-1.4-RC2.jar bookstoreApplication.BookstoreStarter
- Windows:
 $ java -cp BookstoreMonitoring.jar;lib\commons-logging-1.1.1.jar;lib\kieker-1.4-RC2.jar bookstoreApplication.BookstoreStarter

Analysis:

-UNIX-like systems:
 $ java -cp BookstoreAnalysis.jar:lib/commons-logging-1.1.1.jar:lib/kieker-1.4-RC2.jar bookstoreApplication.BookstoreAnalysisStarter </path/to/monitoring-log/>
- Windows:
 $ java -cp BookstoreAnalysis.jar;lib\commons-logging-1.1.1.jar;lib\kieker-1.4-RC2.jar bookstoreApplication.BookstoreAnalysisStarter <path\to\monitoring-log\>

Alternatively, you can compile the sources and start the application using
'ant' with the given 'build.xml' file:

Monitoring:
 $ ant run-monitoring

Analysis:
 $ ant run-analysis -Danalysis.directory=</path/to/monitoring-log/>

Please see Chapter 2 of the Kieker User Guide for details on this example.
