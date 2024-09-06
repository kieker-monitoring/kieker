/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.common.record.jvm;

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;

/**
 * @author Nils Christian Ehmke
 *         API compatibility: Kieker 1.15.0
 *
 * @since 1.10
 */
public class UptimeRecord extends AbstractJVMRecord {
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // AbstractJVMRecord.timestamp
			+ TYPE_SIZE_STRING // AbstractJVMRecord.hostname
			+ TYPE_SIZE_STRING // AbstractJVMRecord.vmName
			+ TYPE_SIZE_LONG; // UptimeRecord.uptimeMS

	public static final Class<?>[] TYPES = {
		long.class, // AbstractJVMRecord.timestamp
		String.class, // AbstractJVMRecord.hostname
		String.class, // AbstractJVMRecord.vmName
		long.class, // UptimeRecord.uptimeMS
	};

	/** property name array. */
	public static final String[] VALUE_NAMES = {
		"timestamp",
		"hostname",
		"vmName",
		"uptimeMS",
	};

	private static final long serialVersionUID = 5233115844046765277L;

	/** property declarations. */
	private final long uptimeMS;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param timestamp
	 *            timestamp
	 * @param hostname
	 *            hostname
	 * @param vmName
	 *            vmName
	 * @param uptimeMS
	 *            uptimeMS
	 */
	public UptimeRecord(final long timestamp, final String hostname, final String vmName, final long uptimeMS) {
		super(timestamp, hostname, vmName);
		this.uptimeMS = uptimeMS;
	}

	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException
	 *             when the record could not be deserialized
	 */
	public UptimeRecord(final IValueDeserializer deserializer) throws RecordInstantiationException {
		super(deserializer);
		this.uptimeMS = deserializer.getLong();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putLong(this.getTimestamp());
		serializer.putString(this.getHostname());
		serializer.putString(this.getVmName());
		serializer.putLong(this.getUptimeMS());
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

		final UptimeRecord castedRecord = (UptimeRecord) obj;
		if ((this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) || (this.getTimestamp() != castedRecord.getTimestamp()) || !this.getHostname().equals(castedRecord.getHostname()) || !this.getVmName().equals(castedRecord.getVmName())) {
			return false;
		}
		if (this.getUptimeMS() != castedRecord.getUptimeMS()) {
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
		code += ((int) this.getTimestamp());
		code += this.getHostname().hashCode();
		code += this.getVmName().hashCode();
		code += ((int) this.getUptimeMS());

		return code;
	}

	public final long getUptimeMS() {
		return this.uptimeMS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String result = "UptimeRecord: ";
		result += "timestamp = ";
		result += this.getTimestamp() + ", ";

		result += "hostname = ";
		result += this.getHostname() + ", ";

		result += "vmName = ";
		result += this.getVmName() + ", ";

		result += "uptimeMS = ";
		result += this.getUptimeMS() + ", ";

		return result;
	}
}
