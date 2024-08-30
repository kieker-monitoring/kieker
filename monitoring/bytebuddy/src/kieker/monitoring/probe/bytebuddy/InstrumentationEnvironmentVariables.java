package kieker.monitoring.probe.bytebuddy;

/**
 * Stores the environment variables that are used for instrumentation. Should be moved to core and also used in javassist in the future
 * 
 * @author DaGeRe
 */
public class InstrumentationEnvironmentVariables {
	public static final String KIEKER_SIGNATURES_INCLUDE = "KIEKER_SIGNATURES_INCLUDE";
	public static final String KIEKER_SIGNATURES_EXCLUDE = "KIEKER_SIGNATURES_EXCLUDE";
}
