package kieker.common.util;

/**
 * This class provides the method getVersion() which returns the version number
 * set during build (String replacement task within build.xml).
 * 
 * @author Andre van Hoorn
 */
public final class Version {
	/*
	 * The VERSION string is updated by the Ant build file, which looks for the
	 * pattern: VERSION = <quote>.*<quote>
	 */

	private static final String VERSION = "1.3-dev-20110422";
	private static final String COPYRIGHT = "Copyright (c) 2006-2011 Kieker Project";


	/**
	 * Returns the version String.
	 * 
	 * @return the version String.
	 */
	public final static String getVERSION() {
		return Version.VERSION;
	}
	
	/**
	 * Returns the version String.
	 * 
	 * @return the version String.
	 */
	public final static String getCOPYRIGHT() {
		return Version.COPYRIGHT;
	}

	private Version() {}
}
