package kieker.common.record;

/**
 * Record type which can be used to store the current time 
 * in the field {@link #currentTime}.
 * 
 * @author Andre van Hoorn
 */
public class CurrentTimeRecord extends AbstractMonitoringRecord {

    private static final long serialVersionUID = 112213L;
    private static int numRecordFields = 1;
    private volatile long currentTime = -1;

    /**
     * Returns the current time.
     * 
     * @return
     */
    public long getCurrentTime() {
		return this.currentTime;
	}

    /**
     * Sets the current time to the given value.
     * 
     * @param currentTime
     */
	public void setCurrentTime(final long currentTime) {
		this.currentTime = currentTime;
	}

	/**
	 * Constructs a new {@link CurrentTimeRecord} with the 
	 * without setting the current time value. 
	 */
	public CurrentTimeRecord () { };

    public CurrentTimeRecord (final long timestamp) {
        this.currentTime = timestamp;
    }

    @Override
	public Class<?>[] getValueTypes() {
        return new Class[] {
          long.class, // timestamp
        };
    }

    @Override
	public void initFromArray(final Object[] values) throws IllegalArgumentException {
        try {
            if (values.length != CurrentTimeRecord.numRecordFields) {
                throw new IllegalArgumentException("Expecting vector with "
                        + CurrentTimeRecord.numRecordFields + " elements but found:" + values.length);
            }
            this.currentTime = (Long) values[0];
        } catch (final Exception exc) {
            throw new IllegalArgumentException("Failed to init", exc);
        }
        return;
    }

    @Override
	public Object[] toArray() {
        return new Object[] {
            this.currentTime,
        };
    }
}
