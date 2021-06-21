# Sending Monitoring Data via TCP

This example is based on the manual probe example, as it is
fairly simple. The difference is that here we use the collector
to receive monitoring data and store it in a Kieker log.

To use this example, you have first to unpack the collector from the
binary distribution, set it up and then run the modified manual probe
example.

1. Setup collector
   - Create a directory for the log files, e.g., results
   - Replace %RESULTS% in `collector.conf` with the name of the log file directories 

1. Start the collector with
   - Linux: `collector-1.15-SNAPSHOT/bin/collector -c collector.conf`
   - Windows: `collector-1.15-SNAPSHOT/bin/collector.bat -c collector.conf`

1. Execute the gradle build
   - Linux: `./gradlew runMonitoring`
   - Windows: `gradlew.bat runMonitoring`
