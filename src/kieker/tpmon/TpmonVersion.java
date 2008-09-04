package kieker.tpmon;

/**
 * This class provides the method getVersion which returns the version number 
 * set during build (String replacement task within build.xml).
 *
 * @author Andr&eacute; van Hoorn 
 */
public class TpmonVersion {

	/*
	 * The VERSION string is updated by the Ant build file, which looks for the
	 * pattern: VERSION = <quote>.*<quote>
	 * 
	 * The string is made private so the compiler can't propagate it into
	 * JMeterUtils This ensures that JMeterUtils always gets the correct
	 * version, even if it is not re-compiled during the build.
	 */
	private static final String VERSION = "0.9-20080904";

	static final String COPYRIGHT = "Copyright (c) 2006-2008 Matthias Rohr and the Kieker Project";

        /**
         * Not instantiable.
         */
	private TpmonVersion() 
	{
		super();
	}

        /**
         * Returns the version String.
         *
         * @return the version String.
         */
	public static final String getVERSION() {
		return VERSION;
	}
}
