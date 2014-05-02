package kieker.tools.dataexchange;

/**
 * Tuple that keeps a anomaly score and the corresponding timestamp.
 * 
 * @author Dominik Olp, Yannic Noller
 * @version 0.2
 */
public class AnomalyRecord {

	private double anomalyScore;
	private long timestamp;

	/**
	 * Directly sets all attributes of an anomaly record.
	 * 
	 * @param anomalyScore
	 *            - real value of anomaly score
	 * @param timestamp
	 *            - calculation time stamp
	 */
	public AnomalyRecord(final double anomalyScore, final long timestamp) {
		this.anomalyScore = anomalyScore;
		this.timestamp = timestamp;
	}

	/**
	 * Creates the object based on the given String appended values.
	 * 
	 * @param inputData
	 *            - object attributes in form: "timestamp;anomalyScore"
	 */
	public AnomalyRecord(final String inputData) {		
		final String[] values = inputData.split(";");
		this.timestamp = Long.parseLong(values[0]);
		this.anomalyScore = Double.parseDouble(values[1]);
	}

	public double getAnomalyScore() {
		return this.anomalyScore;
	}

	public void setAnomalyScore(final double anomalyScore) {
		this.anomalyScore = anomalyScore;
	}

	public long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(final long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(this.timestamp);
		sb.append("; ");
		sb.append(this.anomalyScore);
		sb.append("; ");
		return sb.toString();
	}
}
