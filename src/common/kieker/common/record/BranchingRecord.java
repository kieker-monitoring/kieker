package kieker.common.record;

/**
 * @author Andre van Hoorn
 */
public class BranchingRecord extends AbstractMonitoringRecord {

    /**
	 * @return the timestamp
	 */
	public final long getTimestamp() {
		return this.timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public final void setTimestamp(final long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the branchID
	 */
	public final int getBranchID() {
		return this.branchID;
	}

	/**
	 * @param branchID the branchID to set
	 */
	public final void setBranchID(final int branchID) {
		this.branchID = branchID;
	}

	/**
	 * @return the branchingOutcome
	 */
	public final int getBranchingOutcome() {
		return this.branchingOutcome;
	}

	/**
	 * @param branchingOutcome the branchingOutcome to set
	 */
	public final void setBranchingOutcome(final int branchingOutcome) {
		this.branchingOutcome = branchingOutcome;
	}

	private static final long serialVersionUID = 1113L;
    private static int numRecordFields = 3;
    private volatile long timestamp = -1;
    private volatile int branchID = -1;
    private volatile int branchingOutcome = -1;

    public BranchingRecord () { };

    public BranchingRecord (final long timestamp, final int branchID, final int branchingOutcome) {
        this.timestamp = timestamp;
        this.branchID = branchID;
        this.branchingOutcome = branchingOutcome;
    }

    @Override
	public Class<?>[] getValueTypes() {
        return new Class[] {
          long.class, // timestamp
          int.class,  // branchId
          int.class   // branchingOutcome
        };
    }

    @Override
	public void initFromArray(final Object[] values) throws IllegalArgumentException {
        try {
            if (values.length != BranchingRecord.numRecordFields) {
                throw new IllegalArgumentException("Expecting vector with "
                        + BranchingRecord.numRecordFields + " elements but found:" + values.length);
            }
            this.timestamp = (Long) values[0];
            this.branchID = (Integer) values[1];
            this.branchingOutcome = (Integer) values[2];
        } catch (final Exception exc) {
            throw new IllegalArgumentException("Failed to init", exc);
        }
        return;
    }

    @Override
	public Object[] toArray() {
        return new Object[] {
            this.timestamp,
            this.branchID,
            this.branchingOutcome
        };
    }
}
