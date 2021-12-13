/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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
import kieker.common.record.jvm.AbstractJVMRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;

/**
 * @author Nils Christian Ehmke
 *         API compatibility: Kieker 1.15.0
 * 
 * @since 1.10
 */
public class MemoryRecord extends AbstractJVMRecord {
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // AbstractJVMRecord.timestamp
			+ TYPE_SIZE_STRING // AbstractJVMRecord.hostname
			+ TYPE_SIZE_STRING // AbstractJVMRecord.vmName
			+ TYPE_SIZE_LONG // MemoryRecord.heapMaxBytes
			+ TYPE_SIZE_LONG // MemoryRecord.heapUsedBytes
			+ TYPE_SIZE_LONG // MemoryRecord.heapCommittedBytes
			+ TYPE_SIZE_LONG // MemoryRecord.heapInitBytes
			+ TYPE_SIZE_LONG // MemoryRecord.nonHeapMaxBytes
			+ TYPE_SIZE_LONG // MemoryRecord.nonHeapUsedBytes
			+ TYPE_SIZE_LONG // MemoryRecord.nonHeapCommittedBytes
			+ TYPE_SIZE_LONG // MemoryRecord.nonHeapInitBytes
			+ TYPE_SIZE_INT; // MemoryRecord.objectPendingFinalizationCount

	public static final Class<?>[] TYPES = {
		long.class, // AbstractJVMRecord.timestamp
		String.class, // AbstractJVMRecord.hostname
		String.class, // AbstractJVMRecord.vmName
		long.class, // MemoryRecord.heapMaxBytes
		long.class, // MemoryRecord.heapUsedBytes
		long.class, // MemoryRecord.heapCommittedBytes
		long.class, // MemoryRecord.heapInitBytes
		long.class, // MemoryRecord.nonHeapMaxBytes
		long.class, // MemoryRecord.nonHeapUsedBytes
		long.class, // MemoryRecord.nonHeapCommittedBytes
		long.class, // MemoryRecord.nonHeapInitBytes
		int.class, // MemoryRecord.objectPendingFinalizationCount
	};

	/** property name array. */
	public static final String[] VALUE_NAMES = {
		"timestamp",
		"hostname",
		"vmName",
		"heapMaxBytes",
		"heapUsedBytes",
		"heapCommittedBytes",
		"heapInitBytes",
		"nonHeapMaxBytes",
		"nonHeapUsedBytes",
		"nonHeapCommittedBytes",
		"nonHeapInitBytes",
		"objectPendingFinalizationCount",
	};

	private static final long serialVersionUID = -9025858519361306011L;

	/** property declarations. */
	private final long heapMaxBytes;
	private final long heapUsedBytes;
	private final long heapCommittedBytes;
	private final long heapInitBytes;
	private final long nonHeapMaxBytes;
	private final long nonHeapUsedBytes;
	private final long nonHeapCommittedBytes;
	private final long nonHeapInitBytes;
	private final int objectPendingFinalizationCount;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param hostname
	 *            hostname
	 * @param vmName
	 *            vmName
	 * @param heapMaxBytes
	 *            heapMaxBytes
	 * @param heapUsedBytes
	 *            heapUsedBytes
	 * @param heapCommittedBytes
	 *            heapCommittedBytes
	 * @param heapInitBytes
	 *            heapInitBytes
	 * @param nonHeapMaxBytes
	 *            nonHeapMaxBytes
	 * @param nonHeapUsedBytes
	 *            nonHeapUsedBytes
	 * @param nonHeapCommittedBytes
	 *            nonHeapCommittedBytes
	 * @param nonHeapInitBytes
	 *            nonHeapInitBytes
	 * @param objectPendingFinalizationCount
	 *            objectPendingFinalizationCount
	 */
	public MemoryRecord(final long timestamp, final String hostname, final String vmName, final long heapMaxBytes, final long heapUsedBytes,
			final long heapCommittedBytes, final long heapInitBytes, final long nonHeapMaxBytes, final long nonHeapUsedBytes, final long nonHeapCommittedBytes,
			final long nonHeapInitBytes, final int objectPendingFinalizationCount) {
		super(timestamp, hostname, vmName);
		this.heapMaxBytes = heapMaxBytes;
		this.heapUsedBytes = heapUsedBytes;
		this.heapCommittedBytes = heapCommittedBytes;
		this.heapInitBytes = heapInitBytes;
		this.nonHeapMaxBytes = nonHeapMaxBytes;
		this.nonHeapUsedBytes = nonHeapUsedBytes;
		this.nonHeapCommittedBytes = nonHeapCommittedBytes;
		this.nonHeapInitBytes = nonHeapInitBytes;
		this.objectPendingFinalizationCount = objectPendingFinalizationCount;
	}

	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException
	 *             when the record could not be deserialized
	 */
	public MemoryRecord(final IValueDeserializer deserializer) throws RecordInstantiationException {
		super(deserializer);
		this.heapMaxBytes = deserializer.getLong();
		this.heapUsedBytes = deserializer.getLong();
		this.heapCommittedBytes = deserializer.getLong();
		this.heapInitBytes = deserializer.getLong();
		this.nonHeapMaxBytes = deserializer.getLong();
		this.nonHeapUsedBytes = deserializer.getLong();
		this.nonHeapCommittedBytes = deserializer.getLong();
		this.nonHeapInitBytes = deserializer.getLong();
		this.objectPendingFinalizationCount = deserializer.getInt();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putLong(this.getTimestamp());
		serializer.putString(this.getHostname());
		serializer.putString(this.getVmName());
		serializer.putLong(this.getHeapMaxBytes());
		serializer.putLong(this.getHeapUsedBytes());
		serializer.putLong(this.getHeapCommittedBytes());
		serializer.putLong(this.getHeapInitBytes());
		serializer.putLong(this.getNonHeapMaxBytes());
		serializer.putLong(this.getNonHeapUsedBytes());
		serializer.putLong(this.getNonHeapCommittedBytes());
		serializer.putLong(this.getNonHeapInitBytes());
		serializer.putInt(this.getObjectPendingFinalizationCount());
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

		final MemoryRecord castedRecord = (MemoryRecord) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (this.getTimestamp() != castedRecord.getTimestamp()) {
			return false;
		}
		if (!this.getHostname().equals(castedRecord.getHostname())) {
			return false;
		}
		if (!this.getVmName().equals(castedRecord.getVmName())) {
			return false;
		}
		if (this.getHeapMaxBytes() != castedRecord.getHeapMaxBytes()) {
			return false;
		}
		if (this.getHeapUsedBytes() != castedRecord.getHeapUsedBytes()) {
			return false;
		}
		if (this.getHeapCommittedBytes() != castedRecord.getHeapCommittedBytes()) {
			return false;
		}
		if (this.getHeapInitBytes() != castedRecord.getHeapInitBytes()) {
			return false;
		}
		if (this.getNonHeapMaxBytes() != castedRecord.getNonHeapMaxBytes()) {
			return false;
		}
		if (this.getNonHeapUsedBytes() != castedRecord.getNonHeapUsedBytes()) {
			return false;
		}
		if (this.getNonHeapCommittedBytes() != castedRecord.getNonHeapCommittedBytes()) {
			return false;
		}
		if (this.getNonHeapInitBytes() != castedRecord.getNonHeapInitBytes()) {
			return false;
		}
		if (this.getObjectPendingFinalizationCount() != castedRecord.getObjectPendingFinalizationCount()) {
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
		code += ((int) this.getHeapMaxBytes());
		code += ((int) this.getHeapUsedBytes());
		code += ((int) this.getHeapCommittedBytes());
		code += ((int) this.getHeapInitBytes());
		code += ((int) this.getNonHeapMaxBytes());
		code += ((int) this.getNonHeapUsedBytes());
		code += ((int) this.getNonHeapCommittedBytes());
		code += ((int) this.getNonHeapInitBytes());
		code += ((int) this.getObjectPendingFinalizationCount());

		return code;
	}

	public final long getHeapMaxBytes() {
		return this.heapMaxBytes;
	}

	public final long getHeapUsedBytes() {
		return this.heapUsedBytes;
	}

	public final long getHeapCommittedBytes() {
		return this.heapCommittedBytes;
	}

	public final long getHeapInitBytes() {
		return this.heapInitBytes;
	}

	public final long getNonHeapMaxBytes() {
		return this.nonHeapMaxBytes;
	}

	public final long getNonHeapUsedBytes() {
		return this.nonHeapUsedBytes;
	}

	public final long getNonHeapCommittedBytes() {
		return this.nonHeapCommittedBytes;
	}

	public final long getNonHeapInitBytes() {
		return this.nonHeapInitBytes;
	}

	public final int getObjectPendingFinalizationCount() {
		return this.objectPendingFinalizationCount;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String result = "MemoryRecord: ";
		result += "timestamp = ";
		result += this.getTimestamp() + ", ";

		result += "hostname = ";
		result += this.getHostname() + ", ";

		result += "vmName = ";
		result += this.getVmName() + ", ";

		result += "heapMaxBytes = ";
		result += this.getHeapMaxBytes() + ", ";

		result += "heapUsedBytes = ";
		result += this.getHeapUsedBytes() + ", ";

		result += "heapCommittedBytes = ";
		result += this.getHeapCommittedBytes() + ", ";

		result += "heapInitBytes = ";
		result += this.getHeapInitBytes() + ", ";

		result += "nonHeapMaxBytes = ";
		result += this.getNonHeapMaxBytes() + ", ";

		result += "nonHeapUsedBytes = ";
		result += this.getNonHeapUsedBytes() + ", ";

		result += "nonHeapCommittedBytes = ";
		result += this.getNonHeapCommittedBytes() + ", ";

		result += "nonHeapInitBytes = ";
		result += this.getNonHeapInitBytes() + ", ";

		result += "objectPendingFinalizationCount = ";
		result += this.getObjectPendingFinalizationCount() + ", ";

		return result;
	}
}
