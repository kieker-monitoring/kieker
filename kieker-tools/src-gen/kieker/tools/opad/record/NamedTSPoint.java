/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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
import kieker.common.util.Version;


/**
 * @author Tillmann Carlos Bielefeld
 * 
 * @since 1.10
 */
public class NamedTSPoint extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory {
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // NamedTSPoint.timestamp
			 + TYPE_SIZE_DOUBLE // NamedTSPoint.value
			 + TYPE_SIZE_STRING // NamedTSPoint.name
	;
	private static final long serialVersionUID = 2928953478026943314L;
	
	public static final Class<?>[] TYPES = {
		long.class, // NamedTSPoint.timestamp
		double.class, // NamedTSPoint.value
		String.class, // NamedTSPoint.name
	};
	
	/* user-defined constants */
	/* default constants */
	public static final String NAME = "";
	/* property declarations */
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
		this.name = name == null?"":name;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public NamedTSPoint(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.timestamp = (Long) values[0];
		this.value = (Double) values[1];
		this.name = (String) values[2];
	}
	
	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected NamedTSPoint(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.timestamp = (Long) values[0];
		this.value = (Double) values[1];
		this.name = (String) values[2];
	}

	/**
	 * This constructor converts the given array into a record.
	 * 
	 * @param buffer
	 *            The bytes for the record.
	 * 
	 * @throws BufferUnderflowException
	 *             if buffer not sufficient
	 */
	public NamedTSPoint(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		this.timestamp = buffer.getLong();
		this.value = buffer.getDouble();
		this.name = stringRegistry.get(buffer.getInt());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return new Object[] {
			this.getTimestamp(),
			this.getValue(),
			this.getName()
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		buffer.putLong(this.getTimestamp());
		buffer.putDouble(this.getValue());
		buffer.putInt(stringRegistry.get(this.getName()));
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
	protected boolean equalsInternal(final kieker.common.record.IMonitoringRecord record) {
		final NamedTSPoint castedRecord = (NamedTSPoint) record;
		if (this.timestamp != castedRecord.timestamp) return false;
		if (isEqual(this.value, castedRecord.value)) return false;
		if (!this.name.equals(castedRecord.name)) return false;
		return super.equalsInternal(castedRecord);
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
