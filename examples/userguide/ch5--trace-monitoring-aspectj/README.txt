This directory contains a pre-compiled Jar file which can be started with 
the 'java' tool using the following commands ('5' starts five requests):

-UNIX-like systems:
 $ java -javaagent:lib/kicker-1.10-SNAPSHOT_aspectj.jar -jar BookstoreApplication.jar 5
- Windows:
 $ java -javaagent:lib\kicker-1.10-SNAPSHOT_aspectj.jar -jar BookstoreApplication.jar 5

Alternatively, you can compile the sources and start the application using
'ant' with the given 'build.xml' file:

 $ ant run
 
Please see Chapter 5 of the Kicker User Guide for details on this example.
