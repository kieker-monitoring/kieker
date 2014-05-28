This directory contains a pre-compiled Jar file which can be started with 
the 'java' tool using the following commands:

-UNIX-like systems:
 $ java -cp BookstoreApplication.jar:lib/kicker-1.10-SNAPSHOT_emf.jar kicker.examples.userguide.ch3and4bookstore.Starter
- Windows:
 $ java -cp BookstoreApplication.jar;lib\kicker-1.10-SNAPSHOT_emf.jar kicker.examples.userguide.ch3and4bookstore.Starter

Alternatively, you can compile the sources and start the application using
'ant' with the given 'build.xml' file:

 $ ant run

Please see Chapters 3 and 4 of the Kicker User Guide for details on this example.
