This directory contains a pre-compiled Jar file which can be started with 
the 'java' tool using the following commands ('5' starts five requests):

-UNIX-like systems:
 $ java -cp BookstoreApplication.jar:lib/commons-logging-1.1.1.jar:lib/kieker-1.4-RC2.jar bookstoreTracing.BookstoreStarter 5
- Windows:
 $ java -cp BookstoreApplication.jar;lib\commons-logging-1.1.1.jar;lib/kieker-1.4-RC2.jar bookstoreTracing.BookstoreStarter 5

Alternatively, you can compile the sources and start the application using
'ant' with the given 'build.xml' file:

 $ ant run

Please see Chapter 5 of the Kieker User Guide for details on this example.
