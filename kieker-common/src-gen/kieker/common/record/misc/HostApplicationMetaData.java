/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.IRegistry;


/**
 * @author Christian Wulf
 * 
 * @since 1.13
 */
public class HostApplicationMetaData extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory {
	private static final long serialVersionUID = 7989655300501494980L;

	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_STRING // HostApplicationMetaData.systemName
			 + TYPE_SIZE_STRING // HostApplicationMetaData.ipAddress
			 + TYPE_SIZE_STRING // HostApplicationMetaData.hostName
			 + TYPE_SIZE_STRING // HostApplicationMetaData.applicationName
	;
	
	public static final Class<?>[] TYPES = {
		String.class, // HostApplicationMetaData.systemName
		String.class, // HostApplicationMetaData.ipAddress
		String.class, // HostApplicationMetaData.hostName
		String.class, // HostApplicationMetaData.applicationName
	};
	
	/** user-defined constants. */
	
	/** default constants. */
	public static final String SYSTEM_NAME = "";
	public static final String IP_ADDRESS = "";
	public static final String HOST_NAME = "";
	public static final String APPLICATION_NAME = "";
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"systemName",
		"ipAddress",
		"hostName",
		"applicationName",
	};
	
	/** property declarations. */
	private final String systemName;
	private final String ipAddress;
	private final String hostName;
	private final String applicationName;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param systemName
	 *            systemName
	 * @param ipAddress
	 *            ipAddress
	 * @param hostName
	 *            hostName
	 * @param applicationName
	 *            applicationName
	 */
	public HostApplicationMetaData(final String systemName, final String ipAddress, final String hostName, final String applicationName) {
		this.systemName = systemName == null?"":systemName;
		this.ipAddress = ipAddress == null?"":ipAddress;
		this.hostName = hostName == null?"":hostName;
		this.applicationName = applicationName == null?"":applicationName;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public HostApplicationMetaData(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.systemName = (String) values[0];
		this.ipAddress = (String) values[1];
		this.hostName = (String) values[2];
		this.applicationName = (String) values[3];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected HostApplicationMetaData(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.systemName = (String) values[0];
		this.ipAddress = (String) values[1];
		this.hostName = (String) values[2];
		this.applicationName = (String) values[3];
	}

	/**
	 * This constructor converts the given buffer into a record.
	 * 
	 * @param buffer
	 *            The bytes for the record
	 * @param stringRegistry
	 *            The string registry for deserialization
	 * 
	 * @throws BufferUnderflowException
	 *             if buffer not sufficient
	 */
	public HostApplicationMetaData(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		this.systemName = stringRegistry.get(buffer.getInt());
		this.ipAddress = stringRegistry.get(buffer.getInt());
		this.hostName = stringRegistry.get(buffer.getInt());
		this.applicationName = stringRegistry.get(buffer.getInt());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return new Object[] {
			this.getSystemName(),
			this.getIpAddress(),
			this.getHostName(),
			this.getApplicationName()
		};
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getSystemName());
		stringRegistry.get(this.getIpAddress());
		stringRegistry.get(this.getHostName());
		stringRegistry.get(this.getApplicationName());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		buffer.putInt(stringRegistry.get(this.getSystemName()));
		buffer.putInt(stringRegistry.get(this.getIpAddress()));
		buffer.putInt(stringRegistry.get(this.getHostName()));
		buffer.putInt(stringRegistry.get(this.getApplicationName()));
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?>[] getValueTypes() {
		return TYPES; // NOPMD
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getValueNames() {
		return PROPERTY_NAMES; // NOPMD
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSize() {
		return SIZE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.Factory} mechanism. Hence, this method is not implemented.
	 */
	@Override
	@Deprecated
	public void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.BinaryFactory} mechanism. Hence, this method is not implemented.
	 */
	@Override
	@Deprecated
	public void initFromBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		if (obj.getClass() != this.getClass()) return false;
		
		final HostApplicationMetaData castedRecord = (HostApplicationMetaData) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) return false;
		if (!this.getSystemName().equals(castedRecord.getSystemName())) return false;
		if (!this.getIpAddress().equals(castedRecord.getIpAddress())) return false;
		if (!this.getHostName().equals(castedRecord.getHostName())) return false;
		if (!this.getApplicationName().equals(castedRecord.getApplicationName())) return false;
		return true;
	}
	
	public final String getSystemName() {
		return this.systemName;
	}	
	
	public final String getIpAddress() {
		return this.ipAddress;
	}	
	
	public final String getHostName() {
		return this.hostName;
	}	
	
	public final String getApplicationName() {
		return this.applicationName;
	}	
}
