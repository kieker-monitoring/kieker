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
package kieker.tools.opad.record;

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;

/**
 * @author Tillmann Carlos Bielefeld
 *         API compatibility: Kieker 1.15.0
 * 
 * @since 1.10
 */
public class NamedTSPoint extends AbstractMonitoringRecord {
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // NamedTSPoint.timestamp
			+ TYPE_SIZE_DOUBLE // NamedTSPoint.value
			+ TYPE_SIZE_STRING; // NamedTSPoint.name

	public static final Class<?>[] TYPES = {
		long.class, // NamedTSPoint.timestamp
		double.class, // NamedTSPoint.value
		String.class, // NamedTSPoint.name
	};

	/** property name array. */
	public static final String[] VALUE_NAMES = {
		"timestamp",
		"value",
		"name",
	};

	/** default constants. */
	public static final String NAME = "";
	private static final long serialVersionUID = 4302229080791508406L;

	/** property declarations. */
	private final long timestamp;
	private final double value;
	private final String name;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param value
	 *            value
	 * @param name
	 *            name
	 */
	public NamedTSPoint(final long timestamp, final double value, final String name) {
		this.timestamp = timestamp;
		this.value = value;
		this.name = name == null ? "" : name;
	}

	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException
	 *             when the record could not be deserialized
	 */
	public NamedTSPoint(final IValueDeserializer deserializer) throws RecordInstantiationException {
		this.timestamp = deserializer.getLong();
		this.value = deserializer.getDouble();
		this.name = deserializer.getString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putLong(this.getTimestamp());
		serializer.putDouble(this.getValue());
		serializer.putString(this.getName());
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

		final NamedTSPoint castedRecord = (NamedTSPoint) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (this.getTimestamp() != castedRecord.getTimestamp()) {
			return false;
		}
		if (isNotEqual(this.getValue(), castedRecord.getValue())) {
			return false;
		}
		if (!this.getName().equals(castedRecord.getName())) {
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
		code += ((int) this.getValue());
		code += this.getName().hashCode();

		return code;
	}

	public final long getTimestamp() {
		return this.timestamp;
	}

	public final double getValue() {
		return this.value;
	}

	public final String getName() {
		return this.name;
	}

}
