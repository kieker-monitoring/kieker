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

package kieker.tools.opad.record;

import kieker.tools.dataexchange.OperationAnomalyInfo;

/**
 * Contains all output data opad can provide.
 * 
 * @author Thomas Düllmann
 * 
 * @since 1.10
 */
public class OpadOutputData extends OperationAnomalyInfo {

	/**
	 * Measured responseTime of an operation.
	 */
	private long responseTime;

	/**
	 * The threshold which indicates when a probe is an actual anomaly.
	 */
	private double anomalyThreshold;

	/**
	 * Constructor for full initialization.
	 * 
	 * @param timestamp
	 *            timestamp when the data has been monitored
	 * @param hostname
	 *            hostname of the system where the data has been monitored
	 * @param appname
	 *            name of the application that has been monitored
	 * @param operation
	 *            name of the operation that has been monitored
	 * @param responseTime
	 *            response time the monitored operation had
	 * @param anomalyScore
	 *            calculated anomaly score for the monitored operation
	 * @param anomalyThreshold
	 *            threshold that was set while analysis
	 */
	public OpadOutputData(final long timestamp, final String hostname, final String appname, final String operation, final long responseTime,
			final double anomalyScore, final double anomalyThreshold) {
		super(timestamp, hostname, appname, operation, anomalyScore);
		this.responseTime = responseTime;
		this.anomalyThreshold = anomalyThreshold;
	}

	public long getResponseTime() {
		return this.responseTime;
	}

	public void setResponseTime(final long responseTime) {
		this.responseTime = responseTime;
	}

	public double getAnomalyThreshold() {
		return this.anomalyThreshold;
	}

	public void setAnomalyThreshold(final double anomalyThreshold) {
		this.anomalyThreshold = anomalyThreshold;
	}

	/**
	 * Checks whether this output data object contains an anomaly.
	 * 
	 * @return true if anomaly, else false
	 */
	public boolean isAnomaly() {
		boolean result = false;
		if (this.getAnomalyScore() > this.anomalyThreshold) {
			result = true;
		}
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append(';');
		sb.append(this.responseTime);
		sb.append(';');
		sb.append(this.anomalyThreshold);
		return sb.toString();
	}
}
