package kieker.common.record;

/**
 * @author Andre van Hoorn, Jan Waller
 */
public abstract class AbstractMonitoringRecord implements IMonitoringRecord {
	private static final long serialVersionUID = 1L;
	// private static final Log log = LogFactory.getLog(AbstractMonitoringRecord.class);

	private volatile long loggingTimestamp = -1;

	@Override
	public final long getLoggingTimestamp() {
		return this.loggingTimestamp;
	}

	@Override
	public final void setLoggingTimestamp(final long timestamp) {
		this.loggingTimestamp = timestamp;
	}

	/**
	 * Creates a string representation of this record.
	 * 
	 * Matthias should not use this method for serialization purposes
	 * since this is not the purpose of Object's toString method.
	 */
	@Override
	public final String toString() {
		final Object[] recordVector = this.toArray();
		final StringBuilder sb = new StringBuilder();
		sb.append(this.loggingTimestamp);
		for (final Object curStr : recordVector) {
			sb.append(";");
			sb.append(curStr.toString());
		}
		return sb.toString();
	}
}
