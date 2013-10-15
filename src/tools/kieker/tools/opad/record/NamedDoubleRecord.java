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

package kieker.tools.opad.record;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;

/**
 * This class represents responsetime data from a measured application,
 * stored as an double value.
 * 
 * @author Tom Frotscher
 * 
 */
public class NamedDoubleRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory {

	private static final long serialVersionUID = 1768657580333390199L;

	private static final Class<?>[] TYPES = { String.class, long.class, double.class };

	// Attributes
	private final String applicationName;
	private final long timestamp;
	private final double responseTime;

	/**
	 * Creates an instance of this class based on the parameters.
	 * 
	 * @param application
	 *            Application that is the source of the data
	 * @param timest
	 *            Timestamp
	 * @param response
	 *            Responsetime stored in this record
	 */
	public NamedDoubleRecord(final String application, final long timest, final double response) {
		this.applicationName = application;
		this.timestamp = timest;
		this.responseTime = response;
	}

	/**
	 * Creates an Instance of this class based on a single object array.
	 * 
	 * @param values
	 *            Object array containing the applicationname, timestamp and responsetime
	 */
	public NamedDoubleRecord(final Object[] values) {
		AbstractMonitoringRecord.checkArray(values, NamedDoubleRecord.TYPES);

		this.applicationName = (String) values[0];
		this.timestamp = (Long) values[1];
		this.responseTime = (Double) values[2];

	}

	/**
	 * {@inheritDoc}
	 */
	public Class<?>[] getValueTypes() {
		return NamedDoubleRecord.TYPES.clone();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.Factory} mechanism. Hence, this method is not implemented.
	 */
	@Deprecated
	public void initFromArray(final Object[] arg0) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public Object[] toArray() {
		return new Object[] { this.applicationName, this.timestamp, this.responseTime };
	}

	/**
	 * Returns the application name.
	 * 
	 * @return
	 *         Apllication name
	 */
	public String getApplication() {
		return this.applicationName;
	}

	/**
	 * Returns the timestamp.
	 * 
	 * @return
	 *         Timestamp
	 */
	public long getTimestamp() {
		return this.timestamp;
	}

	/**
	 * Returns the Value.
	 * 
	 * @return
	 *         Value
	 */
	public double getValue() {
		return this.responseTime;
	}

}
