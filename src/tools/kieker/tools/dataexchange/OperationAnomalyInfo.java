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
 * Represents an extended operation signature which also contains information about calculated anomaly score.
 * 
 * @author Thomas Düllmann, Yannic Noller
 * @version 0.2
 * @since 1.10
 */
public class OperationAnomalyInfo {

	private final OperationSignature operationSignature;
	private AnomalyRecord anomalyRecord;

	/**
	 * Constructor of OperationAnomalyInfo which sets directly all attributes.
	 * 
	 * @param timestamp
	 *            - time stamp of calculation
	 * @param hostName
	 *            - name of the host
	 * @param applicationName
	 *            - name of the application
	 * @param fqOperationName
	 *            - full qualified operation name in form: package+.class.method(param*)
	 * @param anomalyScore
	 *            - real value of anomaly score
	 */
	public OperationAnomalyInfo(final long timestamp, final String hostName, final String applicationName, final String fqOperationName, final double anomalyScore) {
		this.operationSignature = new OperationSignature(hostName, applicationName, fqOperationName);
		this.anomalyRecord = new AnomalyRecord(anomalyScore, timestamp);
	}

	/**
	 * Copy constructor.
	 * 
	 * @param otherInfo
	 *            - other OperationAnomalyInfo which should be copied
	 */
	public OperationAnomalyInfo(final OperationAnomalyInfo otherInfo) {
		this(otherInfo.getTimestamp(), otherInfo.getOperationSignature().getHostname(), otherInfo.getOperationSignature().getApplicationName(), otherInfo
				.getOperationSignature().getStringSignature(), otherInfo.getAnomalyScore());
	}

	/**
	 * Creates an OperationSignature object from String which was created by local toString method.
	 * 
	 * @param inputData
	 *            - String of the data created by toString() in form: timeStamp;anomalyScore;host;application;fullqualifiedSignature
	 */
	public OperationAnomalyInfo(final String inputData) {
		final String[] splittedInputData = inputData.split(";");
		this.operationSignature = new OperationSignature(splittedInputData[2], splittedInputData[3], splittedInputData[4]);
		this.anomalyRecord = new AnomalyRecord(Double.parseDouble(splittedInputData[1]), Long.parseLong(splittedInputData[0]));
	}

	public long getTimestamp() {
		return this.anomalyRecord.getTimestamp();
	}

	public double getAnomalyScore() {
		return this.anomalyRecord.getAnomalyScore();
	}

	public AnomalyRecord getAnomalyRecord() {
		return this.anomalyRecord;
	}

	public void setAnomalyRecord(final AnomalyRecord anomalyRecord) {
		this.anomalyRecord = anomalyRecord;
	}

	/**
	 * Sets the score of this anomaly info.
	 * 
	 * @param anomalyScore
	 *            - The new score
	 */
	public void setAnomalyScore(final double anomalyScore) {
		this.anomalyRecord.setAnomalyScore(anomalyScore);
	}

	public OperationSignature getOperationSignature() {
		return this.operationSignature;
	}

	/**
	 * Returns this object as String.
	 * 
	 * @return String in form: timeStamp;anomalyScore;host;application;fullqualifiedSignature
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(this.anomalyRecord.getTimestamp());
		sb.append(';');
		sb.append(this.anomalyRecord.getAnomalyScore());
		sb.append(';');
		sb.append(this.operationSignature.getHostname());
		sb.append(';');
		sb.append(this.operationSignature.getApplicationName());
		sb.append(';');
		sb.append(this.operationSignature.getStringSignature());
		return sb.toString();
	}
}
