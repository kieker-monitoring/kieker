You can compile the sources and start the application using 
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

Please see Appendix D of the Kieker User Guide for details on this example.
