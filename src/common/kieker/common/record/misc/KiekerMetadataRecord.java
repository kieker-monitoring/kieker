/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package kieker.common.record.misc;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;

/**
 * This records collects metadata for the monitoring session.
 * 
 * @author Jan Waller
 * 
 * @since 1.7
 */
public final class KiekerMetadataRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory {

	/**
	 * Constant to be used if no value available.
	 */
	public static final String NO_CONTROLLERNAME = "<no-controller-name>";

	/**
	 * Constant to be used if no value available.
	 */
	public static final String NO_HOSTNAME = "<no-hostname>";

	/**
	 * Constant to be used if no value available.
	 */
	public static final String NO_TIMESOURCE = "<no-timesource>";

	/**
	 * Constant to be used if no value available.
	 */
	public static final String NO_TIMEUNIT = "NANOSECONDS";

	private static final long serialVersionUID = 6867244598532769180L;
	private static final Class<?>[] TYPES = {
		String.class, // controllerName
		String.class, // hostname
		int.class, // experimentId
		String.class, // timeUnit
		String.class, // timeSource
		boolean.class, // debugMode
		long.class, // numberOfRecords
	};

	private final String controllerName;
	private final String hostname;
	private final int experimentId;
	private final String timeUnit;
	private final String timeSource;
	private final boolean debugMode;
	private final long numberOfRecords;

	/**
	 * Creates a new instance of this class.
	 * 
	 * @param controllerName
	 *            The name of the controller (can be null).
	 * @param hostname
	 *            The name of the host (can be null).
	 * @param experimentId
	 *            The experiment ID.
	 * @param timeUnit
	 *            The time unit (can be null).
	 * @param timeSource
	 *            The timesource (can be null).
	 * @param debugMode
	 *            Whether debug mode is enabled or not.
	 * @param numberOfRecords
	 *            The number of records.
	 */
	public KiekerMetadataRecord(final String controllerName, final String hostname, final int experimentId, final String timeUnit, final String timeSource,
			final boolean debugMode, final long numberOfRecords) {
		this.controllerName = (controllerName == null) ? NO_CONTROLLERNAME : controllerName; // NOCS
		this.hostname = (hostname == null) ? NO_HOSTNAME : hostname; // NOCS
		this.experimentId = experimentId;
		this.timeUnit = (timeUnit == null) ? NO_TIMEUNIT : timeUnit; // NOCS
		this.timeSource = (timeSource == null) ? NO_TIMESOURCE : timeSource; // NOCS
		this.debugMode = debugMode;
		this.numberOfRecords = numberOfRecords;
	}

	/**
	 * Creates a new instance of this class using the given parameter.
	 * 
	 * @param values
	 *            The array containing the values for the fields of this class. This should normally be the array resulting in a call to {@link #toArray()}.
	 */
	public KiekerMetadataRecord(final Object[] values) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.controllerName = (String) values[0];
		this.hostname = (String) values[1];
		this.experimentId = (Integer) values[2];
		this.timeUnit = (String) values[3];
		this.timeSource = (String) values[4];
		this.debugMode = (Boolean) values[5];
		this.numberOfRecords = (Long) values[6];
	}

	/**
	 * {@inheritDoc}
	 */
	public Object[] toArray() {
		return new Object[] { this.controllerName, this.hostname, this.experimentId, this.timeUnit, this.timeSource, this.debugMode, this.getNumberOfRecords() };
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<?>[] getValueTypes() {
		return TYPES.clone();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.Factory} mechanism. Hence, this method is not implemented.
	 */
	@Deprecated
	public void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}

	public String getControllerName() {
		return this.controllerName;
	}

	public String getHostname() {
		return this.hostname;
	}

	public int getExperimentId() {
		return this.experimentId;
	}

	public String getTimeUnit() {
		return this.timeUnit;
	}

	public String getTimeSource() {
		return this.timeSource;
	}

	public boolean isDebugMode() {
		return this.debugMode;
	}

	public long getNumberOfRecords() {
		return this.numberOfRecords;
	}

	/**
	 * Converts the current record into a formatted string.
	 * 
	 * @return A formatted string representation of this record.
	 */
	public String toFormattedString() {
		final StringBuilder sb = new StringBuilder(512);
		sb.append("Kieker metadata: controllerName='");
		sb.append(this.controllerName);
		sb.append("', hostname='");
		sb.append(this.hostname);
		sb.append("', experimentId='");
		sb.append(this.experimentId);
		sb.append("', timeUnit='");
		sb.append(this.timeUnit);
		sb.append("', timeSource='");
		sb.append(this.timeSource);
		sb.append("', debugMode='");
		sb.append(this.debugMode);
		sb.append("', numberOfRecords='");
		sb.append(this.numberOfRecords);
		sb.append('\'');
		return sb.toString();
	}
}
