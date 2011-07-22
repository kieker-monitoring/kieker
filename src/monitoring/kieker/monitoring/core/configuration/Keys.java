package kieker.monitoring.core.configuration;

/**
 * @author Andre van Hoorn, Jan Waller
 */
interface Keys {
	// TODO: if this changes, the default config file has to be adjusted! Ideally it would be created using this file!

	/** prefix used for all kieker.monitoring components */
	public final static String PREFIX = "kieker.monitoring.";

	/** Location of the custom properties file (in classpath) */
	public final static String CUSTOM_PROPERTIES_LOCATION_CLASSPATH = "META-INF/" + Keys.PREFIX + "properties";
	/** Location of the default properties file (in classpath) */
	public final static String DEFAULT_PROPERTIES_LOCATION_CLASSPATH = Keys.CUSTOM_PROPERTIES_LOCATION_CLASSPATH + ".default";

	/** JVM-parameter to specify a custom properties file */
	public final static String CUSTOM_PROPERTIES_LOCATION_JVM = Keys.PREFIX + "configuration";

	// these MUST be declared in the file DEFAULT_PROPERTIES_LOCATION_CLASSPATH

	// Monitoring Controller
	public final static String MONITORING_ENABLED = Keys.PREFIX + "enabled";
	public final static String CONTROLLER_NAME = Keys.PREFIX + "name";
	public final static String HOST_NAME = Keys.PREFIX + "hostname";
	public final static String EXPERIMENT_ID = Keys.PREFIX + "initialExperimentId";
	
	// JMX
	public final static String ACTIVATE_JMX = Keys.PREFIX + "jmx";
	public final static String ACTIVATE_JMX_DOMAIN = Keys.PREFIX + "jmx.domain";
	public final static String ACTIVATE_JMX_CONTROLLER = Keys.PREFIX + "jmx.MonitoringController";
	public final static String ACTIVATE_JMX_CONTROLLER_NAME = Keys.PREFIX + "jmx.MonitoringController.name";
	public final static String ACTIVATE_JMX_REMOTE = Keys.PREFIX + "jmx.remote";
	public final static String ACTIVATE_JMX_REMOTE_PORT = Keys.PREFIX + "jmx.remote.port";
	public final static String ACTIVATE_JMX_REMOTE_NAME = Keys.PREFIX + "jmx.remote.name";

	// Writer Controller
	public final static String AUTO_SET_LOGGINGTSTAMP = Keys.PREFIX + "setLoggingTimestamp";
	public final static String WRITER_CLASSNAME = Keys.PREFIX + "writer";
	public final static String TIMER_CLASSNAME = Keys.PREFIX + "timer";
	// Sampling Controller
	public final static String PERIODIC_SENSORS_EXECUTOR_POOL_SIZE = Keys.PREFIX + "periodicSensorsExecutorPoolSize";
}
