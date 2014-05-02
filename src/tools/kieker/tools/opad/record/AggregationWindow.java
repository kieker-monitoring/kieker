package kieker.tools.opad.record;

/**
 * Contains the beginning and end of an aggregation window.
 * @author Thomas Düllmann
 * @version 0.1
 */
public class AggregationWindow {
	private final long windowStart;
	private final long windowEnd;
	
	/**
	 * Constructor.
	 * @param start beginning of the aggregation window
	 * @param end end of the aggregation window
	 */
	public AggregationWindow(final long start, final long end) {
		this.windowStart = start;
		this.windowEnd = end;
	}
	
	public long getWindowStart() {
		return this.windowStart;
	}

	public long getWindowEnd() {
		return this.windowEnd;
	}
	
	/**
	 * Checks whether the given timestamp is within the current aggregation window.
	 * @param timestamp timestamp to check
	 * @return true if timestamp is in window, else false
	 */
	public boolean isWithinWindow(final long timestamp) {
		boolean result = false;
		if (timestamp >= this.windowStart && timestamp <= this.windowEnd) {
			result = true;
		}
		return result;
	}
	
	/**
	 * Checks whether the given timestamp is before the current aggregation window.
	 * @param timestamp timestamp to check
	 * @return true if timestamp is before window, else false
	 */
	public boolean isBeforeWindow(final long timestamp) {
		return timestamp < this.windowStart;
	}
	
	/**
	 * Checks whether the given timestamp is after the current aggregation window.
	 * @param timestamp timestamp to check
	 * @return true if timestamp is after window, else false
	 */
	public boolean isAfterWindow(final long timestamp) {
		return timestamp > this.windowEnd;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(this.windowStart);
		sb.append(" -> ");
		sb.append(this.windowEnd);
		return sb.toString();
	}
}
