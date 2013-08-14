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
import kieker.common.util.Version;

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
		String.class, // version
		String.class, // controllerName
		String.class, // hostname
		int.class, // experimentId
		boolean.class, // debugMode
		long.class, // timeOffset
		String.class, // timeUnit
		long.class, // numberOfRecords
	};

	private final String version;
	private final String controllerName;
	private final String hostname;
	private final int experimentId;
	private final boolean debugMode;
	private final long timeOffset;
	private final String timeUnit;
	private final long numberOfRecords;

	/**
	 * Creates a new instance of this class.
	 * 
	 * @param version
	 *            The Kieker version (can be null).
	 * @param controllerName
	 *            The name of the controller (can be null).
	 * @param hostname
	 *            The name of the host (can be null).
	 * @param experimentId
	 *            The experiment ID.
	 * @param timeUnit
	 *            The time unit (can be null).
	 * @param timeOffset
	 *            The time offset.
	 * @param debugMode
	 *            Whether debug mode is enabled or not.
	 * @param numberOfRecords
	 *            The number of records.
	 */
	public KiekerMetadataRecord(final String version, final String controllerName, final String hostname, final int experimentId, final boolean debugMode,
			final long timeOffset, final String timeUnit, final long numberOfRecords) {
		this.version = (version == null) ? Version.getVERSION() : version; // NOCS
		this.controllerName = (controllerName == null) ? NO_CONTROLLERNAME : controllerName; // NOCS
		this.hostname = (hostname == null) ? NO_HOSTNAME : hostname; // NOCS
		this.experimentId = experimentId;
		this.debugMode = debugMode;
		this.timeOffset = timeOffset;
		this.timeUnit = (timeUnit == null) ? NO_TIMEUNIT : timeUnit; // NOCS
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
		this.version = (String) values[0];
		this.controllerName = (String) values[1];
		this.hostname = (String) values[2];
		this.experimentId = (Integer) values[3];
		this.debugMode = (Boolean) values[4];
		this.timeOffset = (Long) values[5];
		this.timeUnit = (String) values[6];
		this.numberOfRecords = (Long) values[7];
	}

	/**
	 * {@inheritDoc}
	 */
	public Object[] toArray() {
		return new Object[] { this.version, this.controllerName, this.hostname, this.experimentId, this.debugMode, this.timeOffset, this.timeUnit,
			this.numberOfRecords, };
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<?>[] getValueTypes() {
		return TYPES; // NOPMD
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

	public String getVersion() {
		return this.version;
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

	public boolean isDebugMode() {
		return this.debugMode;
	}

	public long getTimeOffset() {
		return this.timeOffset;
	}

	public String getTimeUnit() {
		return this.timeUnit;
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
		sb.append("Kieker metadata: version='");
		sb.append(this.version);
		sb.append("', controllerName='");
		sb.append(this.controllerName);
		sb.append("', hostname='");
		sb.append(this.hostname);
		sb.append("', experimentId='");
		sb.append(this.experimentId);
		sb.append("', debugMode='");
		sb.append(this.debugMode);
		sb.append("', timeOffset='");
		sb.append(this.timeOffset);
		sb.append("', timeUnit='");
		sb.append(this.timeUnit);
		sb.append("', numberOfRecords='");
		sb.append(this.numberOfRecords);
		sb.append('\'');
		return sb.toString();
	}
}
