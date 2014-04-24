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

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.IRegistry;

/**
 * This class represents responsetime data from a measured application, stored as an double value.
 * 
 * @author Tom Frotscher
 * 
 * @since 1.9
 */
public class NamedDoubleRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory {

	public static final int SIZE = TYPE_SIZE_STRING + TYPE_SIZE_LONG + TYPE_SIZE_DOUBLE;
	public static final Class<?>[] TYPES = {
		String.class,
		long.class,
		double.class,
	};

	private static final long serialVersionUID = 1768657580333390199L;

	private final String applicationName;
	private final long timestamp;
	private final double responseTime;

	public NamedDoubleRecord(final String application, final long timest, final double response) {
		this.applicationName = application;
		this.timestamp = timest;
		this.responseTime = response;
	}

	public NamedDoubleRecord(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		this.applicationName = stringRegistry.get(buffer.getInt());
		this.timestamp = buffer.getLong();
		this.responseTime = buffer.getDouble();
	}

	public NamedDoubleRecord(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, NamedDoubleRecord.TYPES);

		this.applicationName = (String) values[0];
		this.timestamp = (Long) values[1];
		this.responseTime = (Double) values[2];
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
	public void initFromArray(final Object[] arg0) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public Object[] toArray() {
		return new Object[] { this.applicationName, this.timestamp, this.responseTime };
	}

	public String getApplication() {
		return this.applicationName;
	}

	public long getTimestamp() {
		return this.timestamp;
	}

	public double getValue() {
		return this.responseTime;
	}

	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		buffer.putInt(stringRegistry.get(this.getApplication()));
		buffer.putLong(this.getTimestamp());
		buffer.putDouble(this.getValue());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.BinaryFactory} mechanism. Hence, this method is not implemented.
	 */
	@Deprecated
	public void initFromBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		throw new UnsupportedOperationException();
	}

	public int getSize() {
		return SIZE;
	}

}
