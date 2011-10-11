This directory contains a pre-compiled Jar file which can be started with 
the 'java' tool using the following commands:

-UNIX-like systems:
 $ java -cp BookstoreApplication.jar:lib/commons-logging-1.1.1.jar:lib/kieker-1.4-RC2.jar bookstoreApplication.Starter
- Windows:
 $ java -cp BookstoreApplication.jar;lib\commons-logging-1.1.1.jar;lib\kieker-1.4-RC2.jar bookstoreApplication.Starter

Alternatively, you can compile the sources and start the application using
'ant' with the given 'build.xml' file:

 $ ant run

Please see Chapters 3 and 4 of the Kieker User Guide for details on this example.
