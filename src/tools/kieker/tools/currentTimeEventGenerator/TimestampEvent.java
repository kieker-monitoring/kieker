package kieker.tools.currentTimeEventGenerator;

import kieker.analysis.plugin.IAnalysisEvent;

/**
 * Record type which can be used to store the current time in the field
 * {@link #timestamp}.
 * 
 * @author Andre van Hoorn
 * 
 */
public class TimestampEvent implements IAnalysisEvent {
	private volatile long timestamp = -1;

	/**
	 * Creates a new {@link TimestampEvent} with the given timestamp.
	 * 
	 * @param currenTime
	 */
	public TimestampEvent(final long currenTime) {
		this.timestamp = currenTime;
	}

	/**
	 * Returns the current time.
	 * 
	 * @return the current time.
	 */
	public long getTimestamp() {
		return this.timestamp;
	}

	/**
	 * Sets the current time to the given value.
	 * 
	 * @param timestamp
	 */
	public void setTimestamp(final long timestamp) {
		this.timestamp = timestamp;
	}
}
