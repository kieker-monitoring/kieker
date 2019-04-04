/***************************************************************************
 * Copyright 2019 Kieker Project (http://kieker-monitoring.net)
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

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;


/**
 * @author Christian Wulf
 * API compatibility: Kieker 1.15.0
 * 
 * @since 1.13
 */
public class HostApplicationMetaData extends AbstractMonitoringRecord  {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_STRING // HostApplicationMetaData.systemName
			 + TYPE_SIZE_STRING // HostApplicationMetaData.ipAddress
			 + TYPE_SIZE_STRING // HostApplicationMetaData.hostname
			 + TYPE_SIZE_STRING; // HostApplicationMetaData.applicationName
	
	public static final Class<?>[] TYPES = {
		String.class, // HostApplicationMetaData.systemName
		String.class, // HostApplicationMetaData.ipAddress
		String.class, // HostApplicationMetaData.hostname
		String.class, // HostApplicationMetaData.applicationName
	};
	
	/** property name array. */
	public static final String[] VALUE_NAMES = {
		"systemName",
		"ipAddress",
		"hostname",
		"applicationName",
	};
	
	/** default constants. */
	public static final String SYSTEM_NAME = "";
	public static final String IP_ADDRESS = "";
	public static final String HOSTNAME = "";
	public static final String APPLICATION_NAME = "";
	private static final long serialVersionUID = 5425789809172379297L;
	
	/** property declarations. */
	private final String systemName;
	private final String ipAddress;
	private final String hostname;
	private final String applicationName;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param systemName
	 *            systemName
	 * @param ipAddress
	 *            ipAddress
	 * @param hostname
	 *            hostname
	 * @param applicationName
	 *            applicationName
	 */
	public HostApplicationMetaData(final String systemName, final String ipAddress, final String hostname, final String applicationName) {
		this.systemName = systemName == null?"":systemName;
		this.ipAddress = ipAddress == null?"":ipAddress;
		this.hostname = hostname == null?"":hostname;
		this.applicationName = applicationName == null?"":applicationName;
	}


	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public HostApplicationMetaData(final IValueDeserializer deserializer) throws RecordInstantiationException {
		this.systemName = deserializer.getString();
		this.ipAddress = deserializer.getString();
		this.hostname = deserializer.getString();
		this.applicationName = deserializer.getString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putString(this.getSystemName());
		serializer.putString(this.getIpAddress());
		serializer.putString(this.getHostname());
		serializer.putString(this.getApplicationName());
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
		return VALUE_NAMES; // NOPMD
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
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != this.getClass()) {
			return false;
		}
		
		final HostApplicationMetaData castedRecord = (HostApplicationMetaData) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (!this.getSystemName().equals(castedRecord.getSystemName())) {
			return false;
		}
		if (!this.getIpAddress().equals(castedRecord.getIpAddress())) {
			return false;
		}
		if (!this.getHostname().equals(castedRecord.getHostname())) {
			return false;
		}
		if (!this.getApplicationName().equals(castedRecord.getApplicationName())) {
			return false;
		}
		
		return true;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int code = 0;
		code += this.getSystemName().hashCode();
		code += this.getIpAddress().hashCode();
		code += this.getHostname().hashCode();
		code += this.getApplicationName().hashCode();
		
		return code;
	}
	
	public final String getSystemName() {
		return this.systemName;
	}
	
	
	public final String getIpAddress() {
		return this.ipAddress;
	}
	
	
	public final String getHostname() {
		return this.hostname;
	}
	
	
	public final String getApplicationName() {
		return this.applicationName;
	}
	
}
