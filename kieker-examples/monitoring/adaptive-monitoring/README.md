# Adaptive Monitoring

This example shows how to use the adaptive monitoring with configuration
during runtime utilizing a configuration file. There are other options
available for runtime configuration of the adaptive monitoring feature
utilizing the `TCPController`. See the documentation for more details.

1. Copy the `kieker-<version>-aspectj.jar` from the distribution
   to the `lib` folder.

2. Start the request loop by invoking
   - Windows: `gradlew.bat runMonitoring`
   - Unix-like: `./gradlew runMonitoring`
   Now you should see an output like this:

```
Bookstore.main: Starting request 0
Bookstore.main: Starting request 1
Bookstore.main: Starting request 2
Bookstore.main: Starting request 3
Bookstore.main: Starting request 4
Bookstore.main: Starting request 5
Bookstore.main: Starting request 6
Bookstore.main: Starting request 7
Bookstore.main: Starting request 8
Bookstore.main: Starting request 9
Bookstore.main: Starting request 10
Bookstore.main: Starting request 11
Bookstore.main: Starting request 12
Bookstore.main: Starting request 13
Bookstore.main: Starting request 14
Bookstore.main: Starting request 15
Bookstore.main: Starting request 16
...
```

3. To change the monitoring configuration, you can edit the configuration file 
   `config/kieker.adaptiveMonitoring.conf` during runtime.
   Further information about the format of the config file can be found in the
   kieker base folder under
   `kieker-examples/kieker.monitoring.adaptiveMonitoring.example.conf`

4. The monitored data can be viewed in the 
   `monitoring-logs/kieker-*-KIEKER-SINGLETON/kieker-*-Thread-1.dat` 
   file.
