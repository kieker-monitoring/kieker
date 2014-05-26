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

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.IRegistry;

/**
 * This class represents responsetime data from a measured application,
 * stored as an double value.
 * 
 * @author Tom Frotscher
 * @since 1.10
 * 
 */
public class NamedDoubleRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory {

	/**
	 * Serialization size.
	 */
	protected static final int SIZE = 20;

	/**
	 * Types used for serialization.
	 */
	static final Class<?>[] TYPES = {
		String.class, // applicationName
		long.class, // timestamp
		double.class, // responseTime
	};

	private static final long serialVersionUID = 1768657580333390199L;

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
		final Object[] localValue = values.clone();
		AbstractMonitoringRecord.checkArray(localValue, NamedDoubleRecord.TYPES);

		this.applicationName = (String) localValue[0];
		this.timestamp = (Long) localValue[1];
		this.responseTime = (Double) localValue[2];

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

	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		buffer.putInt(stringRegistry.get(this.getApplication()));
		buffer.putLong(this.getTimestamp());
		buffer.putDouble(this.getValue());
	}

	public void initFromBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		throw new UnsupportedOperationException(); // TODO: FIX
	}

	public int getSize() {
		return SIZE;
	}

}
