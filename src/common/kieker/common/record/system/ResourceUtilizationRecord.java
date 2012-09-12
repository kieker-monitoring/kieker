/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

/**
 * 
 */
package kieker.common.record.system;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;

/**
 * @author Andre van Hoorn, Jan Waller
 */
public final class ResourceUtilizationRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory {
	private static final long serialVersionUID = 8412442607068036054L;
	private static final Class<?>[] TYPES = {
		long.class,
		String.class,
		String.class,
		double.class,
	};
	private static final String DEFAULT_VALUE = "N/A";

	/**
	 * Date/time of measurement.
	 */
	private final long timestamp;

	/**
	 * Name of the host, the resource belongs to.
	 */
	private final String hostname;

	/**
	 * Name of the resource.
	 */
	private final String resourceName;

	/**
	 * Value of utilization. The value should be in the range <code>[0,1]</code>
	 */
	private final double utilization;

	/**
	 * @param timestamp
	 * @param hostname
	 * @param resourceName
	 * @param utilization
	 */
	public ResourceUtilizationRecord(final long timestamp, final String hostname, final String resourceName, final double utilization) {
		this.timestamp = timestamp;
		this.hostname = (hostname == null) ? DEFAULT_VALUE : hostname; // NOCS
		this.resourceName = (resourceName == null) ? DEFAULT_VALUE : resourceName; // NOCS
		this.utilization = utilization;
	}

	public ResourceUtilizationRecord(final Object[] values) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.timestamp = (Long) values[0];
		this.hostname = (String) values[1];
		this.resourceName = (String) values[2];
		this.utilization = (Double) values[3];
	}

	public Object[] toArray() {
		return new Object[] { this.timestamp, this.hostname, this.resourceName, this.utilization, };
	}

	@Deprecated
	public void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}

	public Class<?>[] getValueTypes() {
		return TYPES.clone();
	}

	/**
	 * @return the timestamp
	 */
	public final long getTimestamp() {
		return this.timestamp;
	}

	/**
	 * @return the hostname
	 */
	public final String getHostname() {
		return this.hostname;
	}

	/**
	 * @return the resourceName
	 */
	public final String getResourceName() {
		return this.resourceName;
	}

	/**
	 * @return the utilization
	 */
	public final double getUtilization() {
		return this.utilization;
	}
}
