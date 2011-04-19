package kieker.monitoring.core.configuration;

/**
 * @author Andre van Hoorn, Jan Waller
 */
interface Keys {
	// TODO: if this changes, the default config file has to be adjusted! Ideally it would be created using this file!

	/** prefix used for all kieker.monitoring components */
	public final static String PREFIX = "kieker.monitoring.";

	/** Location of the custom properties file (in classpath) */
	public final static String CUSTOM_PROPERTIES_LOCATION_CLASSPATH = "META-INF/" + PREFIX + "properties";
	/** Location of the default properties file (in classpath) */
	public final static String DEFAULT_PROPERTIES_LOCATION_CLASSPATH = CUSTOM_PROPERTIES_LOCATION_CLASSPATH + ".default";

	/** JVM-parameter to specify a custom properties file */
	public final static String CUSTOM_PROPERTIES_LOCATION_JVM = PREFIX + "configuration";

	// these MUST be declared in the file DEFAULT_PROPERTIES_LOCATION_CLASSPATH

	// Monitoring Controller
	public final static String MONITORING_ENABLED = PREFIX + "enabled";
	public final static String CONTROLLER_NAME = PREFIX + "name";
	public final static String HOST_NAME = PREFIX + "hostname";
	public final static String EXPERIMENT_ID = PREFIX + "initialExperimentId";
	public final static String ACTIVATE_MBEAN = PREFIX + "MBean";
	public final static String ACTIVATE_MBEAN_DOMAIN = PREFIX + "MBean.domain";
	public final static String ACTIVATE_MBEAN_TYPE = PREFIX + "MBean.name";
	// Replay Controller
	public final static String REPLAY_MODE = PREFIX + "replayMode";
	// Writer Controller
	public final static String WRITER_CLASSNAME = PREFIX + "writer";
	public final static String TIMER_CLASSNAME = PREFIX + "timer";
	// Sampling Controller
	public final static String PERIODIC_SENSORS_EXECUTOR_POOL_SIZE = PREFIX + "periodicSensorsExecutorPoolSize";
}
