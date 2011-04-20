package kieker.monitoring.timer;

public interface ITimeSource {

	/**
	 * Returns the timestamp for the current time.
	 */
	public long getTime();
}
