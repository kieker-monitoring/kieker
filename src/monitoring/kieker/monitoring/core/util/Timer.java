package kieker.monitoring.core.util;

public final class Timer {
	/**
	 * Offset used to determine the number of nanoseconds since 1970-1-1. This
	 * is necessary since System.nanoTime() returns the elapsed nanoseconds
	 * since *some* fixed but arbitrary time.)
	 */
	private static final long offsetA = System.currentTimeMillis() * 1000000 - System.nanoTime();
	
	/**
	 * Returns the timestamp for the current time. The value corresponds to the
	 * number of nanoseconds elapsed since January 1, 1970 UTC.
	 */
	public final static long currentTimeNanos() {
		return System.nanoTime() + offsetA;
	}
	
	/**
	 * Returns a human-readable string with the current date and time.
	 * 
	 * @return the date/time string.
	 */
	public final static String getDateString() {
		return java.util.Calendar.getInstance().getTime().toString();
	}
	
	private Timer() {}
}
