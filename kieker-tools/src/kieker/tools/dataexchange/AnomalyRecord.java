/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.tools.dataexchange;

/**
 * Tuple that keeps a anomaly score and the corresponding timestamp.
 * 
 * @author Dominik Olp, Yannic Noller
 * @version 0.2
 * @since 1.10
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
