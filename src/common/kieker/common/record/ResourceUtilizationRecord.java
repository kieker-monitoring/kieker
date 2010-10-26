/**
 * 
 */
package kieker.common.record;

/**
 * @author Andre van Hoorn
 * 
 */
public class ResourceUtilizationRecord extends AbstractMonitoringRecord {

	private static final String DEFAULT_VALUE = "N/A";

	/**
	 * Date/time of measurement. The value should be interpreted as the number
	 * of nano-seconds elapsed since Jan 1st, 1970 UTC.
	 */
	private volatile long timestamp = -1;

	/**
	 * Name of the host, the resource belongs to.
	 */
	private volatile String hostName = ResourceUtilizationRecord.DEFAULT_VALUE;

	/**
	 * Name of the resource.
	 */
	private volatile String resourceName =
			ResourceUtilizationRecord.DEFAULT_VALUE;

	/**
	 * Value of utilization. The value should be in the range <code>[0,1]</code>
	 */
	private volatile double utilization = -1;

	/**
	 * 
	 */
	private static final long serialVersionUID = 17676L;

	/*
	 * {@inheritdoc}
	 */
	@Override
	public void initFromArray(final Object[] values)
			throws IllegalArgumentException {
		try {
			if (values.length != ResourceUtilizationRecord.VALUE_TYPES.length) {
				throw new IllegalArgumentException("Expecting vector with "
						+ ResourceUtilizationRecord.VALUE_TYPES.length
						+ " elements but found:" + values.length);
			}

			this.timestamp = (Long) values[0];
			this.hostName = (String) values[1];
			this.resourceName = (String) values[2];
			this.utilization = (Double) values[3];

		} catch (final Exception exc) {
			throw new IllegalArgumentException("Failed to init", exc);
		}
		return;
	}
	
	/**
	 * 
	 */
	public ResourceUtilizationRecord() {
	}



	/**
	 * @param timestamp
	 * @param hostName
	 * @param resourceName
	 * @param utilization
	 */
	public ResourceUtilizationRecord(final long timestamp, final String hostName,
			final String resourceName, final double utilization) {
		this.timestamp = timestamp;
		this.hostName = hostName;
		this.resourceName = resourceName;
		this.utilization = utilization;
	}

	/*
	 * {@inheritdoc}
	 */
	@Override
	public Object[] toArray() {
		return new Object[] { this.timestamp, this.hostName, this.resourceName,
				this.utilization };
	}

	private final static Class<?>[] VALUE_TYPES = { long.class, String.class,
			String.class, double.class };

	/*
	 * {@inheritdoc}
	 */
	@Override
	public Class<?>[] getValueTypes() {
		return ResourceUtilizationRecord.VALUE_TYPES;
	}

	/**
	 * @return the timestamp
	 */
	public final long getTimestamp() {
		return this.timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public final void setTimestamp(final long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the hostName
	 */
	public final String getHostName() {
		return this.hostName;
	}

	/**
	 * @param hostName
	 *            the hostName to set
	 */
	public final void setHostName(final String hostName) {
		this.hostName = hostName;
	}

	/**
	 * @return the resourceName
	 */
	public final String getResourceName() {
		return this.resourceName;
	}

	/**
	 * @param resourceName
	 *            the resourceName to set
	 */
	public final void setResourceName(final String resourceName) {
		this.resourceName = resourceName;
	}

	/**
	 * @return the utilization
	 */
	public final double getUtilization() {
		return this.utilization;
	}

	/**
	 * @param utilization
	 *            the utilization to set
	 */
	public final void setUtilization(final double utilization) {
		this.utilization = utilization;
	}
}
