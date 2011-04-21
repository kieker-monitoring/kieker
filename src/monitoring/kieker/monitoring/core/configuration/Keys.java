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
	public final static String ACTIVATE_MBEAN = Keys.PREFIX + "MBean";
	public final static String ACTIVATE_MBEAN_DOMAIN = Keys.PREFIX + "MBean.domain";
	public final static String ACTIVATE_MBEAN_TYPE = Keys.PREFIX + "MBean.name";

	// Writer Controller
	public final static String AUTO_SET_LOGGINGTSTAMP = Keys.PREFIX + "setLoggingTimestamp";
	public final static String WRITER_CLASSNAME = Keys.PREFIX + "writer";
	public final static String TIMER_CLASSNAME = Keys.PREFIX + "timer";
	// Sampling Controller
	public final static String PERIODIC_SENSORS_EXECUTOR_POOL_SIZE = Keys.PREFIX + "periodicSensorsExecutorPoolSize";
}
