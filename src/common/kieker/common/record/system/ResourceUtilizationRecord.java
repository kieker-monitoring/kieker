/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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
 * @author Andre van Hoorn
 * 
 */
public final class ResourceUtilizationRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory {
	private static final long serialVersionUID = -7386791367687652550L;
	private static final Class<?>[] TYPES = {
		long.class,
		String.class,
		String.class,
		double.class,
	};
	private static final String DEFAULT_VALUE = "N/A";

	/**
	 * Date/time of measurement. The value should be interpreted as the number
	 * of nano-seconds elapsed since Jan 1st, 1970 UTC.
	 */
	private volatile long timestamp = -1;

	/**
	 * Name of the host, the resource belongs to.
	 */
	private volatile String hostname = DEFAULT_VALUE;

	/**
	 * Name of the resource.
	 */
	private volatile String resourceName = DEFAULT_VALUE;

	/**
	 * Value of utilization. The value should be in the range <code>[0,1]</code>
	 */
	private volatile double utilization = -1;

	/**
	 * 
	 */
	public ResourceUtilizationRecord() {
		// nothing to do
	}

	/**
	 * @param timestamp
	 * @param hostname
	 * @param resourceName
	 * @param utilization
	 */
	public ResourceUtilizationRecord(final long timestamp, final String hostname, final String resourceName, final double utilization) {
		this.timestamp = timestamp;
		this.hostname = hostname;
		this.resourceName = resourceName;
		this.utilization = utilization;
	}

	public ResourceUtilizationRecord(final Object[] values) {
		final Object[] myValues = values.clone();
		AbstractMonitoringRecord.checkArray(myValues, TYPES);
		try {
			this.timestamp = (Long) myValues[0];
			this.hostname = (String) myValues[1];
			this.resourceName = (String) myValues[2];
			this.utilization = (Double) myValues[3];
		} catch (final Exception exc) { // NOPMD NOCS (IllegalCatchCheck)
			throw new IllegalArgumentException("Failed to init", exc);
		}
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
	 * @param timestamp
	 *            the timestamp to set
	 */
	public final void setTimestamp(final long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the hostname
	 */
	public final String getHostname() {
		return this.hostname;
	}

	/**
	 * @param hostname
	 *            the hostname to set
	 */
	public final void setHostname(final String hostname) {
		this.hostname = hostname;
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
